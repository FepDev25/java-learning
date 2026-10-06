package com.cultodeportivo.predicate;

import com.cultodeportivo.modelo.Estudiante;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

// Predicate<T>    → recibe T, devuelve boolean            → test(T t)
// BiPredicate<T,U>→ recibe T y U, devuelve boolean        → test(T t, U u)
// Composición: and(), or(), negate()
public class EjemploPredicado {
    public static void main(String[] args) {

        // Predicate simple
        Predicate<Double> aprobado = promedio -> promedio >= 7.0;
        System.out.println("¿Felipe aprobó con 8.9? " + aprobado.test(8.9));
        System.out.println("¿Aprobó con 5.5?        " + aprobado.test(5.5));

        // Predicate<String>
        Predicate<String> esAdmin = rol -> rol.equals("ADMIN");
        System.out.println("¿Felipe es admin? " + esAdmin.test("ESTUDIANTE"));

        // Composición con and / or / negate
        Predicate<Double> conHonores = promedio -> promedio >= 9.0;
        Predicate<Double> aprobadoConHonores = aprobado.and(conHonores);
        Predicate<Double> aprobadoOHonores   = aprobado.or(conHonores);
        Predicate<Double> reprobado           = aprobado.negate();

        System.out.println("¿9.1 aprueba con honores? " + aprobadoConHonores.test(9.1));
        System.out.println("¿6.5 aprueba o tiene honores? " + aprobadoOHonores.test(6.5));
        System.out.println("¿8.0 reprobó? " + reprobado.test(8.0));

        // BiPredicate: referencia de método
        BiPredicate<String, String> iguales = String::equals;
        System.out.println("¿'Felipe' == 'Felipe'? " + iguales.test("Felipe", "Felipe"));

        // BiPredicate con objetos
        Estudiante a = new Estudiante("Felipe", 21, "CS",  9.1);
        Estudiante b = new Estudiante("Ana",    20, "Mat", 8.5);

        BiPredicate<Estudiante, Estudiante> mismaCarrera =
                (e1, e2) -> e1.getCarrera().equals(e2.getCarrera());
        System.out.println("¿Misma carrera? " + mismaCarrera.test(a, b));

        BiPredicate<Estudiante, Double> superaPromedio =
                (e, umbral) -> e.getPromedio() > umbral;
        System.out.println("¿Felipe supera 9.0? " + superaPromedio.test(a, 9.0));
    }
}
