package com.cultodeportivo.factory.producto;

import com.cultodeportivo.factory.Notificacion;

public class NotificacionPush extends Notificacion {

    public NotificacionPush(String destinatario, String asunto) {
        this.destinatario = destinatario;
        this.asunto       = asunto;
    }

    @Override
    public void enviar() {
        System.out.println("[PUSH] Notificación push a dispositivo de: " + destinatario);
    }

    @Override
    public String getTipo() { return "PUSH"; }
}
