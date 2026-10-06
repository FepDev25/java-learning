package com.cultodeportivo.factory.producto;

import com.cultodeportivo.factory.Notificacion;

public class NotificacionSMS extends Notificacion {

    public NotificacionSMS(String destinatario, String asunto) {
        this.destinatario = destinatario;
        this.asunto       = asunto;
    }

    @Override
    public void enviar() {
        System.out.println("[SMS] Enviando a: +593 " + destinatario + " | máx 160 chars");
    }

    @Override
    public String getTipo() { return "SMS"; }
}
