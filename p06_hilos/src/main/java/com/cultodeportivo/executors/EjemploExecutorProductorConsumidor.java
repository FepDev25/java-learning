package com.cultodeportivo.executors;

import com.cultodeportivo.productorconsumidor.ColaApuntes;
import com.cultodeportivo.productorconsumidor.runnable.Escritor;
import com.cultodeportivo.productorconsumidor.runnable.Lector;

import java.util.concurrent.*;

// Patrón Productor-Consumidor usando ExecutorService en lugar de new Thread().
// Future<?> sobre Runnable devuelve null al terminar, útil para saber si acabó.
public class EjemploExecutorProductorConsumidor {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        System.out.println("Pool size: " + executor.getPoolSize());

        ColaApuntes cola = new ColaApuntes();

        Future<?> fEscritor = executor.submit(new Escritor(cola));
        Future<?> fLector   = executor.submit(new Lector(cola));

        System.out.println("Pool size activo: " + executor.getPoolSize());

        executor.shutdown();

        // Bloquea main hasta que ambos terminen
        fEscritor.get();
        fLector.get();

        System.out.println("Escritor terminó: " + fEscritor.isDone());
        System.out.println("Lector  terminó: " + fLector.isDone());
        System.out.println("Felipe tiene todos sus apuntes listos para el parcial.");
    }
}
