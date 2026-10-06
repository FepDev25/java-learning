package com.cultodeportivo.productorconsumidor.runnable;

import com.cultodeportivo.productorconsumidor.ColaApuntes;

import java.util.concurrent.ThreadLocalRandom;

public class Escritor implements Runnable {

    private final ColaApuntes cola;
    private final String[] temas = {
        "Complejidad algorítmica",
        "Estructuras de datos",
        "Programación funcional",
        "Concurrencia en Java",
        "Patrones de diseño",
        "SQL y bases de datos",
        "Redes de computadoras",
        "Sistemas operativos",
        "Inteligencia Artificial",
        "Arquitectura de software"
    };

    public Escritor(ColaApuntes cola) {
        this.cola = cola;
    }

    @Override
    public void run() {
        for (String tema : temas) {
            cola.escribir(tema);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(300, 1200));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
