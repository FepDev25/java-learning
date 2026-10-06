package com.cultodeportivo.factory;

// Abstract Factory / Factory Method.
// El método enviarNotificacion() orquesta el flujo (Template Method interno).
// crearNotificacion() es el "factory method" que las subclases implementan.
// El cliente solo conoce NotificacionFactory — no sabe qué tipo concreto se crea.
public abstract class NotificacionFactory {

    // Template method: preparar → enviar (orden fijo, implementación variable)
    public final Notificacion enviarNotificacion(String destinatario, String asunto) {
        Notificacion n = crearNotificacion(destinatario, asunto);
        System.out.println("--- Procesando " + n.getTipo() + " ---");
        n.preparar();
        n.enviar();
        return n;
    }

    // Factory method: cada subclase decide qué producto concreto instanciar
    protected abstract Notificacion crearNotificacion(String destinatario, String asunto);
}
