package com.cultodeportivo.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

// Un Stream es una secuencia de elementos que soporta operaciones encadenadas.
// NO almacena datos (no es una colección), solo los procesa.
// Es lazy: las operaciones intermedias no se ejecutan hasta que hay una terminal.
// Una vez consumido, NO puede reutilizarse.
public class EjemploCrearStream {
    public static void main(String[] args) {

        // Forma 1: Stream.of(...)
        Stream<String> materias = Stream.of("Algoritmos", "Redes", "BD", "SO", "IA");
        materias.forEach(System.out::println);

        System.out.println("---");

        // Forma 2: Arrays.stream(array)
        String[] arr = {"Quito", "Guayaquil", "Cuenca"};
        Arrays.stream(arr).forEach(System.out::println);

        System.out.println("---");

        // Forma 3: Stream.builder()
        Stream<String> construido = Stream.<String>builder()
                .add("Felipe")
                .add("21 años")
                .add("Ecuador")
                .add("CS Student")
                .build();
        construido.forEach(System.out::println);

        System.out.println("---");

        // Forma 4 (más común): collection.stream()
        List<String> lista = List.of("Cálculo", "Programación", "Inglés", "Física");
        lista.stream().forEach(System.out::println);
    }
}
