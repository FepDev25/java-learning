package com.cultodeportivo.productorconsumidor;

import com.cultodeportivo.productorconsumidor.runnable.Escritor;
import com.cultodeportivo.productorconsumidor.runnable.Lector;

// Patrón Productor-Consumidor con wait/notify.
// Escritor produce un apunte → ColaApuntes.escribir() → notifica al Lector.
// Lector consume el apunte → ColaApuntes.leer()     → notifica al Escritor.
// Sin sincronización: race condition, datos corruptos o deadlock.
public class EjemploProductorConsumidor {
    public static void main(String[] args) {
        ColaApuntes cola = new ColaApuntes();

        Thread escritor = new Thread(new Escritor(cola), "Hilo-Escritor");
        Thread lector  = new Thread(new Lector(cola),   "Hilo-Lector");

        // El orden de start no importa gracias al wait/notify
        lector.start();
        escritor.start();
    }
}
