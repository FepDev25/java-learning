package com.cultodeportivo.factory;

import java.util.List;

// Producto abstracto del patrón Factory.
// Define el contrato que todos los tipos de notificación deben cumplir.
public abstract class Notificacion {

    protected String destinatario;
    protected String asunto;
    protected List<String> extras;

    public Notificacion() {}

    // Template Method: preparar() orquesta los pasos; enviar() es específico de cada tipo
    public void preparar() {
        System.out.println("[" + getTipo() + "] Preparando notificación para: " + destinatario);
        System.out.println("  Asunto: " + asunto);
        if (extras != null) extras.forEach(e -> System.out.println("  + " + e));
    }

    public abstract void enviar();
    public abstract String getTipo();

    public String getDestinatario() { return destinatario; }
    public String getAsunto()       { return asunto; }

    @Override
    public String toString() {
        return getTipo() + " → " + destinatario + " | " + asunto;
    }
}
