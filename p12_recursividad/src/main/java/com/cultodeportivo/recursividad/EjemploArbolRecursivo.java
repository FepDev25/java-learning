package com.cultodeportivo.recursividad;

import com.cultodeportivo.modelo.Nodo;

import java.util.stream.Stream;

// Árbol de carpetas del repositorio de Felipe (CS Student, Ecuador).
// Demuestra las DOS formas de recorrer árboles recursivamente:
//   1. Recursión clásica: llamada directa + loop de hijos
//   2. Recursión Java 8:  Stream.concat + flatMap → produce Stream plano de nodos
public class EjemploArbolRecursivo {

    public static void main(String[] args) {

        // Árbol: proyectos universitarios de Felipe
        Nodo repo = new Nodo("Udemy_Master_Java/");

        Nodo basicos   = new Nodo("p01_basicos/");
        Nodo poo       = new Nodo("p04_poo/");
        Nodo hilos     = new Nodo("p06_hilos/");
        Nodo lambda    = new Nodo("p07_java8_lambda/");
        Nodo proyectos = new Nodo("proyects/");

        basicos.addHijo(new Nodo("fundamentos/"))
               .addHijo(new Nodo("operadores/"))
               .addHijo(new Nodo("strings/"));

        poo.addHijo(new Nodo("herencia/"))
           .addHijo(new Nodo("polimorfismo/"))
           .addHijo(new Nodo("interfaces/"));

        Nodo executors = new Nodo("executors/");
        executors.addHijo(new Nodo("EjemploExecutorService.java"))
                 .addHijo(new Nodo("EjemploFixedThreadPool.java"));

        hilos.addHijo(new Nodo("hilos/"))
             .addHijo(new Nodo("sincronizacion/"))
             .addHijo(executors);

        lambda.addHijo(new Nodo("consumer/"))
              .addHijo(new Nodo("predicate/"))
              .addHijo(new Nodo("function/"));

        proyectos.addHijo(new Nodo("tdd-reverse/"))
                 .addHijo(new Nodo("calculadora/"));

        repo.addHijo(basicos)
            .addHijo(poo)
            .addHijo(hilos)
            .addHijo(lambda)
            .addHijo(proyectos);

        // ── Forma 1: recursión clásica ──────────────────────────────
        System.out.println("=== ÁRBOL (recursión clásica) ===");
        imprimirArbol(repo, 0);

        System.out.println();

        // ── Forma 2: recursión Java 8 con Stream ────────────────────
        System.out.println("=== ÁRBOL (recursión Java 8) ===");
        nodoStream(repo, 0)
                .forEach(n -> System.out.println("  ".repeat(n.getNivel()) + n.getNombre()));
    }

    // ── Recursión CLÁSICA ───────────────────────────────────────────
    // Caso base implícito: si no tiene hijos, el for no itera → se detiene.
    public static void imprimirArbol(Nodo nodo, int nivel) {
        System.out.println("  ".repeat(nivel) + nodo.getNombre());
        if (nodo.tieneHijos()) {
            for (Nodo hijo : nodo.getHijos()) {
                imprimirArbol(hijo, nivel + 1);   // llamada recursiva con nivel + 1
            }
        }
    }

    // ── Recursión JAVA 8 ────────────────────────────────────────────
    // Devuelve Stream<Nodo> plano (nodo actual + todos sus descendientes).
    // Stream.concat(a, b): une dos streams sin materializar.
    // flatMap: aplana Stream<Stream<Nodo>> → Stream<Nodo> por cada hijo.
    public static Stream<Nodo> nodoStream(Nodo nodo, int nivel) {
        nodo.setNivel(nivel);
        return Stream.concat(
                Stream.of(nodo),
                nodo.getHijos().stream()
                    .flatMap(hijo -> nodoStream(hijo, nivel + 1))
        );
    }
}
