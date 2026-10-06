package com.cultodeportivo.stream;

import com.cultodeportivo.modelo.Estudiante;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// filter()    → operación intermedia. Mantiene solo los elementos que pasan el Predicate.
// count()     → terminal. Cuenta elementos resultantes.
// findFirst() → terminal. Devuelve Optional<T> con el primer elemento.
// findAny()   → terminal. Devuelve Optional<T> con cualquiera (útil en parallel).
// anyMatch()  → terminal. true si algún elemento cumple el Predicate.
// allMatch()  → terminal. true si todos cumplen.
// noneMatch() → terminal. true si ninguno cumple.
public class EjemploStreamFilter {
    public static void main(String[] args) {

        List<Estudiante> lista = Stream
                .of("Felipe Perez", "Ana Torres", "Felipe Mora",
                    "Sara Vega",    "Felipe Ruiz", "Luis Torres")
                .map(n -> new Estudiante(n.split(" ")[0], n.split(" ")[1], 21, "Ecuador"))
                .collect(Collectors.toList());

        // filter + collect
        List<Estudiante> felipes = lista.stream()
                .filter(e -> e.getNombre().equals("Felipe"))
                .peek(System.out::println)
                .collect(Collectors.toList());

        System.out.println("Total Felipes: " + felipes.size());

        System.out.println("---");

        // filter + count
        long countFelipes = lista.stream()
                .filter(e -> e.getNombre().equals("Felipe"))
                .count();
        System.out.println("count: " + countFelipes);

        // findFirst → Optional
        Optional<Estudiante> primero = lista.stream()
                .filter(e -> e.getApellido().equals("Torres"))
                .findFirst();
        primero.ifPresent(e -> System.out.println("Primer Torres: " + e));

        // anyMatch / allMatch / noneMatch
        boolean hayEcuatorianos = lista.stream().anyMatch(e -> e.getPais().equals("Ecuador"));
        boolean todosEcuatorianos = lista.stream().allMatch(e -> e.getPais().equals("Ecuador"));
        System.out.println("¿Hay ecuatorianos?    " + hayEcuatorianos);
        System.out.println("¿Todos ecuatorianos?  " + todosEcuatorianos);
    }
}
