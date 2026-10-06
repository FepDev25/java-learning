package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Curso;
import com.cultodeportivo.modelo.Estudiante;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// parallel() → divide el stream en sub-streams procesados por el ForkJoinPool común.
// Ventaja:  acelera operaciones independientes costosas (I/O simulada, cálculo puro).
// Cuidado:  el ORDEN de procesamiento no está garantizado.
//           findAny() es más apropiado que findFirst() en streams paralelos.
//           NO usar si las lambdas tienen efectos secundarios (ej. escribir variables externas).
public class EjemploStreamParallel {
    public static void main(String[] args) {

        List<Estudiante> estudiantes = buildEstudiantes();

        // Versión secuencial
        long t1 = System.currentTimeMillis();
        String resSeq = estudiantes.stream()
                .map(e -> {
                    simularCarga();
                    return e.toString().toUpperCase();
                })
                .peek(n -> System.out.println("SEQ  | " + Thread.currentThread().getName() + " | " + n))
                .filter(n -> n.contains("FELIPE"))
                .findFirst().orElse("");
        long tSeq = System.currentTimeMillis() - t1;

        System.out.println("Secuencial encontrado: " + resSeq);
        System.out.println("Tiempo secuencial:     " + tSeq + " ms");

        System.out.println("---");

        // Versión paralela — misma lógica, solo se agrega .parallel()
        long t2 = System.currentTimeMillis();
        Optional<String> resPar = estudiantes.stream()
                .parallel()
                .map(e -> {
                    simularCarga();
                    return e.toString().toUpperCase();
                })
                .peek(n -> System.out.println("PAR  | " + Thread.currentThread().getName() + " | " + n))
                .filter(n -> n.contains("FELIPE"))
                .findAny();      // findAny es más natural en paralelo
        long tPar = System.currentTimeMillis() - t2;

        System.out.println("Paralelo encontrado: " + resPar.orElse("no encontrado"));
        System.out.println("Tiempo paralelo:     " + tPar + " ms");
    }

    private static void simularCarga() {
        try { TimeUnit.MILLISECONDS.sleep(500); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static List<Estudiante> buildEstudiantes() {
        return Arrays.asList(
                new Estudiante("Felipe", "Perez",    21, "Ecuador"),
                new Estudiante("Ana",    "Torres",   20, "Ecuador"),
                new Estudiante("Luis",   "Mora",     22, "Colombia"),
                new Estudiante("Sara",   "Vega",     19, "Peru"),
                new Estudiante("Carlos", "Diaz",     23, "Ecuador"),
                new Estudiante("Maria",  "Lopez",    21, "México"),
                new Estudiante("Jorge",  "Sanchez",  24, "Colombia"),
                new Estudiante("Diana",  "Ramirez",  20, "Ecuador")
        );
    }
}
