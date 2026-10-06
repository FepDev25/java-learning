package com.cultodeportivo.anotaciones;

import com.cultodeportivo.modelo.Estudiante;
import com.cultodeportivo.procesador.JsonSerializador;

import java.time.LocalDate;

// Demo final: el procesador lee las anotaciones con Reflection y serializa a JSON.
// Solo los campos con @JsonAtributo aparecen; los demás (fechaNacimiento, pais) se ignoran.
// El método @Init normaliza el nombre antes de la serialización.
public class EjemploAnotacion {
    public static void main(String[] args) {

        Estudiante felipe = new Estudiante(
                "felipe andres perez",      // nombre en minúsculas → @Init lo normaliza
                21,
                "ciencias de la computacion",
                9.1,
                LocalDate.of(2003, 7, 15),  // NO aparece en JSON (sin @JsonAtributo)
                "Ecuador"                   // NO aparece en JSON (sin @JsonAtributo)
        );

        System.out.println("Objeto original:");
        System.out.println("  " + felipe);

        String json = JsonSerializador.convertirJson(felipe);
        System.out.println("\nJSON serializado:");
        System.out.println("  " + json);

        // Segundo estudiante: nombre ya en TitleCase
        Estudiante ana = new Estudiante();
        ana.setNombre("ANA TORRES");
        ana.setEdad(20);
        ana.setCarrera("matematicas");
        ana.setPromedio(8.5);

        System.out.println("\nJSON de Ana:");
        System.out.println("  " + JsonSerializador.convertirJson(ana));

        // Esperado:
        // {"nombre":"Felipe Andres Perez", "edad_años":"21",
        //  "carrera":"Ciencias De La Computacion", "promedio_gpa":"9.1"}
    }
}
