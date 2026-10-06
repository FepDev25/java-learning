package com.cultodeportivo.sincronizacion;

import com.cultodeportivo.sincronizacion.runnable.ImprimirApunte;

// synchronized garantiza que solo un hilo a la vez ejecuta imprimirApunte().
// Sin synchronized las líneas "título" y "contenido" se mezclarían entre hilos.
public class EjemploSincronizacion {
    public static void main(String[] args) throws InterruptedException {

        new Thread(new ImprimirApunte("Hilo 1", "Algoritmos de búsqueda")).start();
        new Thread(new ImprimirApunte("Hilo 2", "Complejidad O(n log n)")).start();

        Thread.sleep(100);

        Thread h3 = new Thread(new ImprimirApunte("Hilo 3", "Grafos y BFS/DFS"));
        h3.start();
        Thread.sleep(100);

        System.out.println("Estado de Hilo-3 mientras escribe: " + h3.getState());
    }
    
    // synchronized: el monitor del objeto clase (static) evita condiciones de carrera
    public synchronized static void imprimirApunte(String titulo, String contenido) {
        System.out.print("Felipe anota: " + titulo);
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(contenido);
    }
}
