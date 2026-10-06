package com.cultodeportivo.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

// Timer con periodo: se repite cada N ms hasta que se cancela.
// AtomicInteger es thread-safe para contar desde otro hilo.
public class EjemploTimerPeriodo {
    public static void main(String[] args) {
        Timer timer = new Timer();
        AtomicInteger contador = new AtomicInteger(0);

        System.out.println("Felipe activa recordatorio de hidratación cada 2 segundos...");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int vez = contador.incrementAndGet();
                System.out.println("💧 Recordatorio #" + vez + " → Toma agua, Felipe! | " + new Date());
                if (vez >= 4) {
                    System.out.println("Ya tomó suficiente agua. Cancelando timer.");
                    timer.cancel();
                }
            }
        }, 1000, 2000); // delay inicial 1s, luego cada 2s

        System.out.println("Timer en marcha. El main continúa...");
    }
}
