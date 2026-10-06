package com.cultodeportivo.observer;

// Interfaz del "Suscriptor".
// Recibe el sujeto que cambió y el dato específico del evento.
// Al ser @FunctionalInterface se puede usar como lambda.
@FunctionalInterface
public interface Observador {
    void actualizar(Observable observable, Object dato);
}
