package com.cultodeportivo.productorconsumidor;

// Monitor compartido: el Escritor produce apuntes, el Lector los consume.
// wait() libera el lock y suspende el hilo. notify() despierta al hilo en espera.
public class ColaApuntes {

    private String apunte;
    private boolean disponible = false;

    public synchronized void escribir(String tema) {
        // Si ya hay un apunte sin leer, esperar
        while (disponible) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.apunte = tema;
        System.out.println("E -> Felipe escribió apunte: " + apunte);
        this.disponible = true;
        notify(); // avisa al Lector
    }

    public synchronized String leer() {
        // Si no hay apunte aún, esperar
        while (!disponible) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("L -> Felipe lee apunte:    " + apunte);
        this.disponible = false;
        notify(); // avisa al Escritor
        return apunte;
    }
}
