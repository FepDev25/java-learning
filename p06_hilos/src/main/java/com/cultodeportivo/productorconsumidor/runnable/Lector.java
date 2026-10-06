package com.cultodeportivo.productorconsumidor.runnable;

import com.cultodeportivo.productorconsumidor.ColaApuntes;

public class Lector implements Runnable {

    private final ColaApuntes cola;

    public Lector(ColaApuntes cola) {
        this.cola = cola;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            cola.leer();
        }
    }
}
