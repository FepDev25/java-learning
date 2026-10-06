package com.cultodeportivo.optional;

import java.util.Optional;

// Optional<T>: contenedor que puede tener un valor o estar vacío.
// Objetivo: eliminar NullPointerExceptions y hacer explícita la ausencia de valor.
// NUNCA llamar get() sin verificar isPresent() → equivale al viejo null-check.
// Preferir los métodos funcionales: ifPresent, ifPresentOrElse, map, orElse, etc.
public class EjemploOptional {
    public static void main(String[] args) {

        // --- Creación ---
        // of(valor)        → lanza NullPointerException si el valor es null
        Optional<String> opt1 = Optional.of("Felipe");

        // ofNullable(valor)→ acepta null, devuelve Optional.empty() si es null
        String beca = null;
        Optional<String> opt2 = Optional.ofNullable(beca);

        // empty()          → Optional vacío explícito
        Optional<String> opt3 = Optional.empty();

        System.out.println("opt1: " + opt1);   // Optional[Felipe]
        System.out.println("opt2: " + opt2);   // Optional.empty
        System.out.println("opt3: " + opt3);   // Optional.empty

        // --- Consultar ---
        System.out.println("isPresent: " + opt1.isPresent());  // true
        System.out.println("isEmpty:   " + opt2.isEmpty());    // true (Java 11+)

        // get() solo cuando se está seguro
        if (opt1.isPresent()) {
            System.out.println("Hola, " + opt1.get());
        }

        // --- Consumir de forma funcional (preferido) ---
        // ifPresent: ejecuta Consumer si hay valor
        opt1.ifPresent(nombre -> System.out.println("Estudiante: " + nombre + ", 21 años, Ecuador"));

        // ifPresentOrElse: Consumer si hay valor, Runnable si está vacío (Java 9+)
        opt2.ifPresentOrElse(
                b  -> System.out.println("Beca: " + b),
                () -> System.out.println("Felipe no tiene beca asignada aún")
        );

        opt3.ifPresentOrElse(
                v  -> System.out.println("valor: " + v),
                () -> System.out.println("Optional vacío confirmado")
        );
    }
}
