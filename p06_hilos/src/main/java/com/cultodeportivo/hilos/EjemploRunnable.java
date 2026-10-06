package com.cultodeportivo.hilos;

import com.cultodeportivo.hilos.runnable.TareaEstudio;

// Opción 2: implementar Runnable y pasarlo a un Thread.
// Más flexible: la clase puede extender otra y aun así ser un Runnable.

public class EjemploRunnable {
    public static void main(String[] args) throws InterruptedException {

        // constructor de Thread: Thread(Runnable target, String name)
        Thread t1 = new Thread(new TareaEstudio("Cálculo I"),        "Hilo-Calculo");
        Thread t2 = new Thread(new TareaEstudio("Programación OOP"), "Hilo-OOP");
        Thread t3 = new Thread(new TareaEstudio("Inglés"),   "Hilo-Ingles");
        Thread t4 = new Thread(new TareaEstudio("Redes"),            "Hilo-Redes");

        t1.start(); // inicia el hilo
        t2.start();
        t3.start();
        t4.start();

        t1.join(); // join() bloquea el main hasta que t1 termine
        t2.join();
        t3.join();
        t4.join();

        System.out.println("Felipe terminó todas las materias del semestre.");
    }
}
