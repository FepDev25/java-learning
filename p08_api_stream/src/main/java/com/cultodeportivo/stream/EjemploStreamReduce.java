package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Curso;
import com.cultodeportivo.modelo.Estudiante;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

// reduce() → operación terminal. Combina todos los elementos en un único resultado.
// Variantes:
//   reduce(identity, BinaryOperator<T>)       → devuelve T
//   reduce(BinaryOperator<T>)                 → devuelve Optional<T> (stream puede estar vacío)
//   reduce(identity, BiFunction, BinaryOperator)→ para combinar tipos distintos
public class EjemploStreamReduce {
    public static void main(String[] args) {

        // Reduce con Integer: sumar créditos
        int totalCreditos = Stream.of(4, 3, 4, 2, 3)
                .reduce(0, Integer::sum);
        System.out.println("Créditos totales de Felipe: " + totalCreditos);

        // Reduce con Integer::sum equivale a:
        int igual = Stream.of(4, 3, 4, 2, 3)
                .reduce(0, (acum, elemento) -> acum + elemento);
        System.out.println("Igual resultado: " + igual);

        System.out.println("---");

        // Reduce con String: concatenar materias aprobadas
        String aprobadas = Stream.of("Algoritmos", "Redes", "BD", "SO", "IA")
                .distinct()
                .reduce("Materias aprobadas: ", (acum, materia) -> acum + " | " + materia);
        System.out.println(aprobadas);

        System.out.println("---");

        // Reduce sin identity → Optional (puede estar vacío)
        Optional<Integer> maxCreditos = Stream.of(4, 3, 4, 2, 3)
                .reduce(Integer::max);
        maxCreditos.ifPresent(m -> System.out.println("Máx créditos: " + m));

        System.out.println("---");

        // Reduce para calcular promedio ponderado de cursos de Felipe
        Estudiante felipe = new Estudiante("Felipe", "Perez", 21, "Ecuador");
        List<Curso> cursos = Arrays.asList(
                new Curso("Algoritmos",  4, 9.1),
                new Curso("Redes",       3, 8.5),
                new Curso("BD",          4, 8.8),
                new Curso("SO",          3, 7.9)
        );
        cursos.forEach(felipe::addCurso);

        int totalCred = cursos.stream().mapToInt(Curso::getCreditos).sum();
        double promPonderado = cursos.stream()
                .reduce(0.0,
                        (acum, c) -> acum + c.getNota() * c.getCreditos(),
                        Double::sum)
                / totalCred;

        System.out.printf("Promedio ponderado de Felipe: %.2f%n", promPonderado);
    }
}
