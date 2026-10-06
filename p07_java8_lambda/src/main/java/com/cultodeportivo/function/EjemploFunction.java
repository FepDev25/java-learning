package com.cultodeportivo.function;

import com.cultodeportivo.modelo.Estudiante;

import java.util.function.BiFunction;
import java.util.function.Function;

// Function<T,R>    → recibe T, devuelve R                 → apply(T t)
// BiFunction<T,U,R>→ recibe T y U, devuelve R             → apply(T t, U u)
// andThen()        → composición: f1.andThen(f2) = f2(f1(x))
// compose()        → composición inversa: f1.compose(f2) = f1(f2(x))
public class EjemploFunction {
    public static void main(String[] args) {

        // Function simple: nombre → mayúsculas
        Function<String, String> aMayusculas = String::toUpperCase;
        System.out.println(aMayusculas.apply("felipe"));

        // Function: String → Estudiante
        Function<String, Estudiante> crearEstudiante =
                nombre -> new Estudiante(nombre, 21, "CS", 8.5);
        Estudiante e = crearEstudiante.apply("Felipe");
        System.out.println("Creado: " + e);

        // Composición con andThen: primero crea el estudiante, luego extrae el nombre en mayúsculas
        Function<String, String> nombreMayus = crearEstudiante.andThen(est -> est.getNombre().toUpperCase());
        System.out.println("andThen: " + nombreMayus.apply("felipe"));

        // BiFunction: (nombre, promedio) → descripción
        BiFunction<String, Double, String> resumen =
                (nombre, prom) -> nombre + " de Ecuador tiene promedio " + prom;
        System.out.println(resumen.apply("Felipe", 9.1));

        // BiFunction con referencia de método estático
        BiFunction<String, String, Integer> comparar = String::compareTo;
        System.out.println("compareTo: " + comparar.apply("Felipe", "Felipe"));  // 0 → iguales

        // BiFunction con andThen: (a,b) → Long luego Long→String
        BiFunction<Integer, Integer, Long> sumar = (a, b) -> (long) (a + b);
        Function<Long, String> mostrarCreditos = total -> "Total créditos: " + total;
        String resultado = sumar.andThen(mostrarCreditos).apply(4, 3);
        System.out.println(resultado);
    }
}
