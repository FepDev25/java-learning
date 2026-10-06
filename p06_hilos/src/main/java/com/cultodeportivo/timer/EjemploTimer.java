package com.cultodeportivo.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

// Timer ejecuta una tarea una sola vez después de un delay.
// Alternativa moderna: ScheduledExecutorService (ver paquete executors/).
public class EjemploTimer {
    public static void main(String[] args) {
        Timer timer = new Timer();

        System.out.println("Felipe programa recordatorio para dentro de 3 segundos...");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("⏰ Recordatorio: entregar tarea de CS en Quito");
                System.out.println("   Ejecutado en: " + new Date());
                System.out.println("   Hilo: " + Thread.currentThread().getName());
                timer.cancel(); // libera el hilo del Timer
            }
        }, 3000);

        System.out.println("Continuando con otras tareas mientras espera el timer...");
    }
}
