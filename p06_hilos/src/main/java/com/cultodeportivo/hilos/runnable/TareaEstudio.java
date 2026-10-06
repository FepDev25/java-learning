package com.cultodeportivo.hilos.runnable;

// hilo implementando runnable → se puede usar con cualquier clase que ya extienda otra
public class TareaEstudio implements Runnable {

    private final String materia;

    public TareaEstudio(String materia) {
        this.materia = materia;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        System.out.println("INICIO Felipe estudia: " + materia + " | hilo: " + Thread.currentThread().getName());

        for (int i = 1; i <= 4; i++) {
            System.out.println("  " + materia + " → ejercicio " + i);
            try {
                // (long) (Math.random() * 700) → entre 0 y 700 ms
                Thread.sleep((long) (Math.random() * 700));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("FIN Felipe terminó de estudiar: " + materia + " | hilo: " + Thread.currentThread().getName());
    }
}
