package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Estudiante;

import java.util.stream.Stream;

// distinct() → elimina duplicados usando equals() y hashCode().
// Para primitivos/String: funciona sin configuración.
// Para objetos:           REQUIERE @Override de equals() y hashCode().
public class EjemploStreamDistinct {
    public static void main(String[] args) {

        // Distinct con Strings (trivial)
        System.out.println("Materias únicas:");
        Stream.of("Algoritmos", "Redes", "Algoritmos", "BD", "Redes", "IA")
                .distinct()
                .forEach(System.out::println);

        System.out.println("---");

        // Distinct con objetos → necesita equals/hashCode en Estudiante
        System.out.println("Estudiantes únicos (con equals/hashCode):");
        Stream.of(
                "Felipe Perez", "Ana Torres", "Felipe Perez",   // Felipe duplicado
                "Luis Mora",    "Ana Torres",  "Sara Vega"       // Ana duplicada
        )
        .map(n -> new Estudiante(n.split(" ")[0], n.split(" ")[1], 21, "Ecuador"))
        .distinct()
        .forEach(System.out::println);
    }
}
