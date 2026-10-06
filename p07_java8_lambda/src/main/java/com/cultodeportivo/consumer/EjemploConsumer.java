package com.cultodeportivo.consumer;

import com.cultodeportivo.modelo.Estudiante;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

// Consumer<T>    → recibe T, no devuelve nada          → accept(T t)
// BiConsumer<T,U>→ recibe T y U, no devuelve nada      → accept(T t, U u)
// Supplier<T>    → no recibe nada, devuelve T           → get()
public class EjemploConsumer {
    public static void main(String[] args) {

        // Consumer simple: imprime un estudiante
        Consumer<Estudiante> mostrar = e ->
                System.out.println("Estudiante: " + e.getNombre() + " | promedio: " + e.getPromedio());

        Estudiante felipe = new Estudiante("Felipe", 21, "Ciencias de la Computación", 8.9);
        mostrar.accept(felipe);

        // BiConsumer: asigna carrera y la muestra
        BiConsumer<Estudiante, String> asignarCarrera = (e, carrera) -> {
            e.setCarrera(carrera);
            System.out.println(e.getNombre() + " ahora estudia: " + carrera);
        };
        asignarCarrera.accept(felipe, "Ingeniería de Software");

        // Consumer con referencia de método de instancia
        Consumer<String> imprimir = System.out::println;
        imprimir.accept("Hola desde Ecuador!");

        // forEach con Consumer
        List<String> materias = Arrays.asList("Algoritmos", "Redes", "BD", "SO", "IA");
        materias.forEach(imprimir);

        // BiConsumer con referencia de método de instancia: setter
        BiConsumer<Estudiante, Double> actualizarPromedio = Estudiante::setPromedio;
        actualizarPromedio.accept(felipe, 9.2);
        System.out.println("Nuevo promedio de Felipe: " + felipe.getPromedio());

        // Supplier: fábrica de objetos sin argumentos
        Supplier<Estudiante> nuevoEstudiante = Estudiante::new;
        Estudiante otro = nuevoEstudiante.get();
        otro.setNombre("Ana");
        otro.setCarrera("Matemáticas");
        System.out.println("Nuevo estudiante creado: " + otro.getNombre() + " | " + otro.getCarrera());

        // Supplier con valor constante
        Supplier<String> universidad = () -> "Universidad Central del Ecuador";
        System.out.println("Universidad de Felipe: " + universidad.get());
    }
}
