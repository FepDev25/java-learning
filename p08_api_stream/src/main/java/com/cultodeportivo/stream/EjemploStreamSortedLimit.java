package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Curso;
import com.cultodeportivo.modelo.Estudiante;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// sorted()      → ordena con Comparator (o natural order para Comparable).
// limit(n)      → toma los primeros n elementos.
// skip(n)       → salta los primeros n (paginación con limit).
// min() / max() → terminales que devuelven Optional<T> con Comparator.
public class EjemploStreamSortedLimit {
    public static void main(String[] args) {

        // sorted por nota descendente → top 3
        List<Curso> cursos = Arrays.asList(
                new Curso("Algoritmos",     4, 9.1),
                new Curso("Redes",          3, 8.5),
                new Curso("BD",             4, 8.8),
                new Curso("SO",             3, 7.9),
                new Curso("IA",             4, 9.3),
                new Curso("Inglés Técnico", 2, 8.0)
        );

        System.out.println("Top 3 materias de Felipe por nota:");
        cursos.stream()
                .sorted(Comparator.comparingDouble(Curso::getNota).reversed())
                .limit(3)
                .forEach(c -> System.out.println("  " + c));

        System.out.println("---");

        // sorted + skip: "página 2" de materias ordenadas por créditos
        System.out.println("Materias por créditos (pág 2, desde la 3ra):");
        cursos.stream()
                .sorted(Comparator.comparingInt(Curso::getCreditos).reversed())
                .skip(2)
                .forEach(c -> System.out.println("  " + c));

        System.out.println("---");

        // min / max sobre Stream<Estudiante>
        List<Estudiante> estudiantes = buildEstudiantes();
        estudiantes.stream()
                .min(Comparator.comparingDouble(Estudiante::getPromedio))
                .ifPresent(e -> System.out.println("Menor promedio: " + e
                        + " → " + e.getPromedio()));

        estudiantes.stream()
                .max(Comparator.comparingDouble(Estudiante::getPromedio))
                .ifPresent(e -> System.out.println("Mayor promedio: " + e
                        + " → " + e.getPromedio()));
    }

    private static List<Estudiante> buildEstudiantes() {
        Estudiante felipe = new Estudiante("Felipe", "Perez", 21, "Ecuador");
        felipe.addCurso(new Curso("Algoritmos", 4, 9.1));
        felipe.addCurso(new Curso("Redes",      3, 8.5));

        Estudiante ana = new Estudiante("Ana", "Torres", 20, "Ecuador");
        ana.addCurso(new Curso("Cálculo", 4, 7.5));
        ana.addCurso(new Curso("Física",  3, 7.0));

        Estudiante luis = new Estudiante("Luis", "Mora", 22, "Colombia");
        luis.addCurso(new Curso("BD",  4, 9.5));
        luis.addCurso(new Curso("IA",  4, 9.2));

        return Arrays.asList(felipe, ana, luis);
    }
}
