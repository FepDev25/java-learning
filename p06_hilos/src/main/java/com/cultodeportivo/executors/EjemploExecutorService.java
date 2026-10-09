package com.cultodeportivo.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// ExecutorService gestiona un pool de hilos, evitando crear/destruir hilos
// manualmente. newSingleThreadExecutor() → cola FIFO con 1 hilo.
// shutdown() → acepta tareas enviadas pero no nuevas. awaitTermination() → bloquea.
public class EjemploExecutorService {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Runnable descargarApuntes = () -> {
            System.out.println("Descargando apuntes de Algoritmos...");
            System.out.println("  Hilo: " + Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Apuntes de Felipe listos.");
        };

        executor.submit(descargarApuntes);
        executor.shutdown();

        System.out.println("Main continúa mientras se descargan apuntes...");
        boolean termino = executor.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("¿Terminó dentro de 3s? " + termino);
        System.out.println("Main finaliza.");
    }
}
