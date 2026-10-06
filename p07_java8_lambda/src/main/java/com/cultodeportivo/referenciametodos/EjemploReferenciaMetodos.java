package com.cultodeportivo.referenciametodos;

import com.cultodeportivo.modelo.Estudiante;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// Referencia de método: azúcar sintáctico para lambdas que solo delegan a un método.
// 4 formas:
//   1. ClaseEstatica::metodoEstatico   →  (a,b) -> Clase.metodo(a,b)
//   2. instancia::metodoInstancia      →  (a)   -> obj.metodo(a)
//   3. Clase::metodoInstancia          →  (obj,a)-> obj.metodo(a)
//   4. Clase::new  (constructor ref)   →  (a)   -> new Clase(a)
public class EjemploReferenciaMetodos {
    public static void main(String[] args) {

        // 1. Referencia de método ESTÁTICO
        //    lambda equivalente: (a, b) -> Math.max(a, b)
        BiFunction<Double, Double, Double> max = Math::max;
        System.out.println("Máx créditos (4.0 vs 3.0): " + max.apply(4.0, 3.0));

        // 2. Referencia de método de INSTANCIA sobre una instancia concreta
        //    lambda equivalente: s -> System.out.println(s)
        Consumer<String> imprimir = System.out::println;
        imprimir.accept("Hola Felipe desde Ecuador!");

        // 3. Referencia de método de INSTANCIA sobre la clase (instancia arbitraria)
        //    lambda equivalente: s -> s.toUpperCase()
        Function<String, String> mayusculas = String::toUpperCase;
        System.out.println(mayusculas.apply("ciencias de la computacion"));

        // Útil con Comparator
        List<String> materias = Arrays.asList("Redes", "Algoritmos", "BD", "SO", "IA");
        materias.sort(String::compareToIgnoreCase);   // Comparator como referencia
        materias.forEach(System.out::println);

        // 4. Referencia de CONSTRUCTOR
        //    lambda equivalente: () -> new Estudiante()
        Supplier<Estudiante> nuevoEstudiante = Estudiante::new;
        Estudiante e = nuevoEstudiante.get();
        e.setNombre("Felipe");
        e.setEdad(21);
        System.out.println("Estudiante creado: " + e.getNombre() + ", " + e.getEdad() + " años");

        // Comparator con referencia de método: ordenar por promedio
        List<Estudiante> lista = Arrays.asList(
                new Estudiante("Felipe", 21, "CS",  9.1),
                new Estudiante("Ana",    20, "Mat", 8.5),
                new Estudiante("Luis",   22, "CS",  7.8),
                new Estudiante("Sara",   19, "Fis", 9.4)
        );
        lista.sort(Comparator.comparingDouble(Estudiante::getPromedio).reversed());
        System.out.println("\nRanking por promedio:");
        lista.forEach(est -> System.out.println("  " + est));
    }
}
