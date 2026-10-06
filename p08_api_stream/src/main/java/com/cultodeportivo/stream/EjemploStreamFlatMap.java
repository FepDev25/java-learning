package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Curso;
import com.cultodeportivo.modelo.Estudiante;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// flatMap() → aplana Stream<Stream<T>> en Stream<T>.
// Útil cuando cada elemento contiene una colección anidada.
// Diferencia:
//   map()     → Stream<List<Curso>>   (un stream de listas)
//   flatMap() → Stream<Curso>         (un stream plano de cursos)
public class EjemploStreamFlatMap {
    public static void main(String[] args) {

        // flatMap sobre Strings: separar palabras
        List<String> frases = List.of("Felipe estudia CS", "en Ecuador");
        frases.stream()
                .flatMap(frase -> Arrays.stream(frase.split(" ")))
                .forEach(System.out::println);

        System.out.println("---");

        // flatMap sobre colecciones anidadas: cursos de cada estudiante
        Estudiante felipe = new Estudiante("Felipe", "Perez", 21, "Ecuador");
        felipe.addCurso(new Curso("Algoritmos",  4, 9.1));
        felipe.addCurso(new Curso("Redes",       3, 8.5));

        Estudiante ana = new Estudiante("Ana", "Torres", 20, "Ecuador");
        ana.addCurso(new Curso("Cálculo",  4, 7.8));
        ana.addCurso(new Curso("Física",   3, 8.0));
        ana.addCurso(new Curso("Inglés",   2, 9.4));

        List<Estudiante> lista = Arrays.asList(felipe, ana);

        // Con map → obtenemos Stream<List<Curso>>  (no es lo que queremos)
        System.out.println("Con map (Stream de listas):");
        lista.stream()
                .map(Estudiante::getCursos)
                .forEach(System.out::println);

        System.out.println("\nCon flatMap (Stream plano de cursos):");
        List<Curso> todosCursos = lista.stream()
                .flatMap(e -> e.getCursos().stream())
                .peek(c -> System.out.println("  " + c + " → alumno: "
                        + lista.stream()
                               .filter(e -> e.getCursos().contains(c))
                               .map(Estudiante::toString)
                               .findFirst().orElse("?")))
                .collect(Collectors.toList());

        System.out.println("Total cursos: " + todosCursos.size());

        // flatMap como alternativa a filter: devolver Stream.empty() para excluir
        System.out.println("\nCursos con nota >= 9.0:");
        lista.stream()
                .flatMap(e -> e.getCursos().stream())
                .flatMap(c -> c.getNota() >= 9.0 ? Stream.of(c) : Stream.empty())
                .forEach(System.out::println);
    }
}
