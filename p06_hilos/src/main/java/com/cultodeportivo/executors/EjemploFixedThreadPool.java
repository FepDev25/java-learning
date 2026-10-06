package com.cultodeportivo.executors;

import java.util.concurrent.*;

// newFixedThreadPool(n) → exactamente n hilos reutilizables.
// Callable<T> es como Runnable pero retorna un valor y puede lanzar excepciones.
// Future<T> representa el resultado de un cómputo asíncrono.
public class EjemploFixedThreadPool {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

        System.out.println("Pool size inicial: " + executor.getPoolSize());

        Callable<String> compilarProyecto = () -> {
            System.out.println("Compilando proyecto de Felipe... | " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(2);
            return "Proyecto compilado exitosamente";
        };

        Callable<String> ejecutarTests = () -> {
            System.out.println("Ejecutando tests unitarios... | " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(3);
            return "Tests: 42 passed, 0 failed";
        };

        Callable<Integer> contarLineas = () -> {
            System.out.println("Contando líneas de código... | " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(1);
            return 1_340;
        };

        Future<String>  f1 = executor.submit(compilarProyecto);
        Future<String>  f2 = executor.submit(ejecutarTests);
        Future<Integer> f3 = executor.submit(contarLineas);

        System.out.println("Pool size activo: " + executor.getPoolSize());
        System.out.println("Tareas en cola: " + executor.getQueue().size());

        executor.shutdown();
        System.out.println("Main esperando resultados...");

        while (!(f1.isDone() && f2.isDone() && f3.isDone())) {
            System.out.printf("  compilar: %-10s | tests: %-10s | líneas: %-10s%n",
                    f1.isDone() ? "listo" : "en curso",
                    f2.isDone() ? "listo" : "en curso",
                    f3.isDone() ? "listo" : "en curso");
            TimeUnit.MILLISECONDS.sleep(800);
        }

        System.out.println("\nResultados de Felipe:");
        System.out.println("  " + f1.get());
        System.out.println("  " + f2.get());
        System.out.println("  Líneas de código: " + f3.get());
    }
}
