package com.cultodeportivo.hilos;

// Forma 3 (Java 8+): Runnable como expresión lambda.
// Runnable es una interfaz funcional, se puede expresar como lambda.
public class EjemploRunnableLambda {
    
    @SuppressWarnings("BusyWait")
    public static void main(String[] args) throws InterruptedException {

        // referencia al hilo principal
        Thread mainThread = Thread.currentThread();

        String[] destinos = {"Quito", "Guayaquil", "Cuenca", "Loja"};

        // Runnable como expresión lambda
        Runnable viaje = () -> {
            // referencia al hilo actual dentro del lambda
            String ciudad = Thread.currentThread().getName();

            System.out.println("Felipe sale hacia: " + ciudad);
            // Simula el viaje con pasos y sleeps
            for (int i = 1; i <= 3; i++) {
                System.out.println("  " + ciudad + " → km " + (i * 100));
                try {
                    Thread.sleep((long) (Math.random() * 600));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Llegó a " + ciudad + " | main state: " + mainThread.getState());
        };

        // Creamos un hilo por cada destino usando el mismo Runnable
        Thread[] hilos = new Thread[destinos.length];
        // constructor de Thread: Thread(Runnable target, String name)
        for (int i = 0; i < destinos.length; i++) {
            hilos[i] = new Thread(viaje, destinos[i]);
            hilos[i].start();
        }

        // Esperamos a que todos los hilos terminen
        for (Thread h : hilos) h.join();

        System.out.println("Todos los viajes terminaron. Continuando en: " + mainThread.getName());
    }
}
