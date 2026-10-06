package com.cultodeportivo.hilos.threads;

// hilo extendiendo Thread → no se puede extender otra clase
public class TareaFelipe extends Thread {

    public TareaFelipe(String nombre) {
        super(nombre);
    }

    // El método run() es el punto de entrada del hilo
    @SuppressWarnings("BusyWait") // para warning por el sleep
    @Override
    public void run() {
        System.out.println("INICIO Hilo: " + getName());

        for (int i = 1; i <= 5; i++) {
            System.out.println("  " + getName() + " → paso " + i);
            try {
                Thread.sleep((long) (Math.random() * 800));
            } catch (InterruptedException e) {
                System.out.println("Hilo interrumpido: " + e.getMessage());
            }
        }

        System.out.println("FIN Hilo: " + getName() + " terminó | estado final: " + getState());
    }
}
