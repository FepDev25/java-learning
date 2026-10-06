package com.cultodeportivo.hilos;

import com.cultodeportivo.hilos.threads.TareaFelipe;

// Opción 1: extender Thread directamente.
// Limitación: Java no permite herencia múltiple, así que si TareaFelipe ya extiende otra clase, esto no sirve
// Recomendacion: usar Runnable.

public class EjemploExtenderThread {
    public static void main(String[] args) throws InterruptedException {

        // constructor de Thread: Thread(String name)
        Thread hilo1 = new TareaFelipe("Algoritmos"); // Thread clase, y TareaFelipe es la clase que extiende Thread
        Thread hilo2 = new TareaFelipe("Estructuras de Datos");
        Thread hilo3 = new TareaFelipe("Base de Datos");

        hilo1.start();
        hilo2.start();

        System.out.println("Estado de hilo1 justo después de start(): " + hilo1.getState());

        hilo3.start();

        // join() bloquea el main hasta que los tres hilos terminen
        hilo1.join();
        hilo2.join();
        hilo3.join();

        System.out.println("Todos los hilos finalizaron.");
    }
}
