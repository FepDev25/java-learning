package com.cultodeportivo.executors;

import java.time.LocalTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

// ScheduledExecutorService: versión moderna de Timer.
// schedule()            → una sola vez con delay.
// scheduleAtFixedRate() → repetir cada N unidades de tiempo.
// Preferir sobre Timer: maneja excepciones mejor y usa pool de hilos.
public class EjemploScheduledExecutor {
    public static void main(String[] args) throws InterruptedException {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        AtomicInteger contador = new AtomicInteger(0);

        // Tarea única: notificación en 2 segundos
        scheduler.schedule(() -> {
            System.out.println("[" + LocalTime.now() + "] ⏰ Felipe: empieza la clase de Redes!");
        }, 2, TimeUnit.SECONDS);

        // Tarea periódica: recordatorio cada 1.5 segundos, máximo 4 veces
        ScheduledFuture<?> futuro = scheduler.scheduleAtFixedRate(() -> {
            int vez = contador.incrementAndGet();
            System.out.println("[" + LocalTime.now() + "] 💡 Repasa tema " + vez + " de Algoritmos");
        }, 500, 1500, TimeUnit.MILLISECONDS);

        System.out.println("Scheduler activo, main continúa...");

        // Cancelar la tarea periódica después de 7 segundos
        TimeUnit.SECONDS.sleep(7);
        futuro.cancel(false);
        scheduler.shutdown();
        System.out.println("Scheduler detenido. Felipe terminó de repasar.");
    }
}
