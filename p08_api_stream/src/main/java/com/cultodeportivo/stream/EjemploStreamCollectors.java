package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Curso;
import com.cultodeportivo.modelo.Estudiante;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Collectors → operaciones terminales que acumulan en colecciones u otros contenedores.
// toList()       → List<T>
// toSet()        → Set<T>
// toMap()        → Map<K,V>
// groupingBy()   → Map<K, List<T>>  (agrupar por criterio)
// joining()      → String concatenado
// counting()     → Long (como downstream de groupingBy)
public class EjemploStreamCollectors {
    public static void main(String[] args) {

        List<Estudiante> estudiantes = buildEstudiantes();

        // toList
        List<String> nombres = estudiantes.stream()
                .map(Estudiante::getNombre)
                .collect(Collectors.toList());
        System.out.println("Nombres: " + nombres);

        // joining: unir strings con delimitador, prefijo y sufijo
        String listado = estudiantes.stream()
                .map(Estudiante::toString)
                .collect(Collectors.joining(" | ", "[", "]"));
        System.out.println("Listado: " + listado);

        System.out.println("---");

        // toMap: nombre → promedio
        Map<String, Double> promedios = estudiantes.stream()
                .collect(Collectors.toMap(
                        e -> e.getNombre() + " " + e.getApellido(),
                        Estudiante::getPromedio)
                );
        promedios.forEach((nombre, prom) ->
                System.out.printf("  %-20s → %.2f%n", nombre, prom));

        System.out.println("---");

        // groupingBy: agrupar por país
        Map<String, List<Estudiante>> porPais = estudiantes.stream()
                .collect(Collectors.groupingBy(Estudiante::getPais));
        porPais.forEach((pais, lista) -> {
            System.out.println("País: " + pais);
            lista.forEach(e -> System.out.println("  " + e));
        });

        System.out.println("---");

        // groupingBy + counting: cuántos estudiantes por país
        Map<String, Long> cantPorPais = estudiantes.stream()
                .collect(
                        Collectors.groupingBy(
                                Estudiante::getPais,
                                Collectors.counting()
                        )
                );
        cantPorPais.forEach((pais, cant) ->
                System.out.println(pais + ": " + cant + " estudiante(s)"));

        System.out.println("---");

        // Collectors con cursos: todos los cursos agrupados por nombre de materia
        Map<String, Long> cursosFrecuencia = estudiantes.stream()
                .flatMap(e -> e.getCursos().stream())
                .collect(Collectors.groupingBy(Curso::getNombre, Collectors.counting()));
        System.out.println("Materias compartidas:");
        cursosFrecuencia.forEach((mat, c) -> System.out.println("  " + mat + ": " + c + " vez/veces"));
    }

    private static List<Estudiante> buildEstudiantes() {
        Estudiante felipe = new Estudiante("Felipe", "Perez",  21, "Ecuador");
        felipe.addCurso(new Curso("Algoritmos", 4, 9.1));
        felipe.addCurso(new Curso("Redes",      3, 8.5));

        Estudiante ana = new Estudiante("Ana", "Torres", 20, "Ecuador");
        ana.addCurso(new Curso("Algoritmos", 4, 7.5));
        ana.addCurso(new Curso("Física",     3, 8.0));

        Estudiante luis = new Estudiante("Luis", "Mora", 22, "Colombia");
        luis.addCurso(new Curso("BD",         4, 9.5));
        luis.addCurso(new Curso("Algoritmos", 4, 9.2));

        Estudiante sara = new Estudiante("Sara", "Vega", 19, "Peru");
        sara.addCurso(new Curso("Redes", 3, 8.8));
        sara.addCurso(new Curso("SO",    3, 7.9));

        return Arrays.asList(felipe, ana, luis, sara);
    }
}
