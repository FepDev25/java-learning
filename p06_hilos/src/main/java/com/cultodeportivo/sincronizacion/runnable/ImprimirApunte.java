package com.cultodeportivo.sincronizacion.runnable;

import static com.cultodeportivo.sincronizacion.EjemploSincronizacion.imprimirApunte;

// Runnable que llama al método sincronizado.
// Varios hilos usan esta clase, sin synchronized se intercalarían las líneas.
public class ImprimirApunte implements Runnable {

    private final String titulo;
    private final String contenido;

    public ImprimirApunte(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    @Override
    public void run() {
        imprimirApunte(titulo, contenido);
    }
}
