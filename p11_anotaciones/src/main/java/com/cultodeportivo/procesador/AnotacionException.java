package com.cultodeportivo.procesador;

// RuntimeException: no obliga al llamador a hacer try/catch,
// pero sí interrumpe el flujo si algo falla en la reflection.
public class AnotacionException extends RuntimeException {
    public AnotacionException(String mensaje) {
        super(mensaje);
    }
}
