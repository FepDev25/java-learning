package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Estudiante;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// map() → operación intermedia. Transforma cada elemento T → R.
// peek() → operación intermedia de depuración (no cambia elementos, solo los observa).
// collect() → operación terminal. Materializa el Stream en una colección.
public class EjemploStreamMap {
    public static void main(String[] args) {

        // Transformar Strings → Estudiante
        List<Estudiante> estudiantes = Stream
                .of("Felipe Perez", "Ana Torres", "Luis Mora", "Sara Vega")
                .map(nombre -> new Estudiante(
                        nombre.split(" ")[0],
                        nombre.split(" ")[1],
                        21, "Ecuador"))
                .peek(e -> System.out.println("creado: " + e))   // debug sin alterar el stream
                .map(e -> {
                    e.setNombre(e.getNombre().toUpperCase());     // segunda transformación
                    return e;
                })
                .collect(Collectors.toList());

        System.out.println("\nLista final:");
        estudiantes.forEach(System.out::println);
    }
}
