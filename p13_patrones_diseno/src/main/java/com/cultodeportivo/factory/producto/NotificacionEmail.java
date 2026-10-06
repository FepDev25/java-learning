package com.cultodeportivo.factory.producto;

import com.cultodeportivo.factory.Notificacion;

import java.util.Arrays;

public class NotificacionEmail extends Notificacion {

    public NotificacionEmail(String destinatario, String asunto) {
        this.destinatario = destinatario;
        this.asunto       = asunto;
        this.extras       = Arrays.asList("Adjunto: cronograma.pdf", "Responder antes de 48h");
    }

    @Override
    public void enviar() {
        System.out.println("[EMAIL] Enviando a: " + destinatario + " | smtp.uce.edu.ec");
    }

    @Override
    public String getTipo() { return "EMAIL"; }
}
