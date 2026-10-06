package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Curso;

import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// IntStream / LongStream / DoubleStream → versiones primitivas (sin boxing).
// mapToInt() → convierte Stream<T> en IntStream.
// summaryStatistics() → calcula sum, max, min, avg, count en una sola pasada.
// IntStream.range(a,b)    → [a, b)  exclusivo en b
// IntStream.rangeClosed(a,b) → [a, b]  inclusivo en b
public class EjemploStreamEstadisticas {
    public static void main(String[] args) {

        // mapToInt + summaryStatistics sobre objetos
        Stream<Curso> cursos = Stream.of(
                new Curso("Algoritmos",     4, 9.1),
                new Curso("Redes",          3, 8.5),
                new Curso("BD",             4, 8.8),
                new Curso("SO",             3, 7.9),
                new Curso("IA",             4, 9.3),
                new Curso("Inglés Técnico", 2, 8.0)
        );

        IntSummaryStatistics stats = cursos
                .peek(c -> System.out.println("  Procesando: " + c))
                .mapToInt(Curso::getCreditos)
                .summaryStatistics();

        System.out.println("\nEstadísticas de créditos de Felipe:");
        System.out.println("  Total:   " + stats.getSum());
        System.out.println("  Máximo:  " + stats.getMax());
        System.out.println("  Mínimo:  " + stats.getMin());
        System.out.println("  Promedio:" + stats.getAverage());
        System.out.println("  Cantidad:" + stats.getCount());

        System.out.println("---");

        // IntStream.range → genera secuencias de enteros (útil para indexar)
        System.out.println("Semanas del semestre (1-16):");
        IntStream.rangeClosed(1, 16)
                .filter(s -> s % 4 == 0)   // cada 4 semanas
                .peek(s -> System.out.print("  Semana " + s))
                .map(s -> s * 2)            // horas acumuladas (2h/semana)
                .forEach(h -> System.out.println(" → " + h + " horas"));

        System.out.println("---");

        // Operaciones directas en IntStream: sum, min, max, average
        int totalHoras = IntStream.of(4, 3, 4, 3, 4, 2).sum();
        System.out.println("Total horas semanales: " + totalHoras + "h");
    }
}
