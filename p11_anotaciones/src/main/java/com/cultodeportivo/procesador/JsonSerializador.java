package com.cultodeportivo.procesador;

import com.cultodeportivo.anotaciones.Init;
import com.cultodeportivo.anotaciones.JsonAtributo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

// Procesador que usa la API de Reflection para leer las anotaciones en tiempo de ejecución.
// Reflection: inspeccionar y manipular clases, campos y métodos en runtime.
//
// Flujo:
//   1. inicializarObjeto() → busca métodos con @Init y los invoca
//   2. convertirJson()     → busca campos con @JsonAtributo y los serializa
public class JsonSerializador {

    // Paso 1: invocar todos los métodos marcados con @Init
    public static void inicializarObjeto(Object objeto) {
        if (Objects.isNull(objeto)) {
            throw new AnotacionException("El objeto no puede ser null");
        }

        Method[] metodos = objeto.getClass().getDeclaredMethods();
        Arrays.stream(metodos)
                .filter(m -> m.isAnnotationPresent(Init.class))
                .forEach(m -> {
                    m.setAccessible(true);  // permite invocar métodos private
                    try {
                        m.invoke(objeto);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new AnotacionException("Error al invocar @Init: " + e.getMessage());
                    }
                });
    }

    // Paso 2: serializar a JSON solo los campos anotados con @JsonAtributo
    public static String convertirJson(Object objeto) {
        if (Objects.isNull(objeto)) {
            throw new AnotacionException("El objeto no puede ser null");
        }

        inicializarObjeto(objeto);  // ejecutar @Init antes de leer campos

        Field[] campos = objeto.getClass().getDeclaredFields();

        return Arrays.stream(campos)
                .filter(f -> f.isAnnotationPresent(JsonAtributo.class))
                .map(f -> {
                    f.setAccessible(true);  // permite leer/escribir campos private
                    JsonAtributo meta = f.getAnnotation(JsonAtributo.class);

                    // Si nombre = "" en la anotación → usar nombre del campo
                    String clave = meta.nombre().isEmpty() ? f.getName() : meta.nombre();

                    try {
                        Object valor = f.get(objeto);

                        // capitalizar: convertir a TitleCase si el valor es String
                        if (meta.capitalizar() && valor instanceof String) {
                            String s = (String) valor;
                            valor = Arrays.stream(s.split(" "))
                                    .map(p -> p.substring(0, 1).toUpperCase() + p.substring(1).toLowerCase())
                                    .collect(Collectors.joining(" "));
                            f.set(objeto, valor);   // actualizar el campo
                        }

                        return "\"" + clave + "\":\"" + valor + "\"";

                    } catch (IllegalAccessException e) {
                        throw new AnotacionException("Error leyendo campo " + f.getName() + ": " + e.getMessage());
                    }
                })
                // reduce: construye el JSON acumulando pares clave:valor
                .reduce("{", (acum, par) -> "{".equals(acum) ? acum + par : acum + ", " + par)
                .concat("}");
    }
}
