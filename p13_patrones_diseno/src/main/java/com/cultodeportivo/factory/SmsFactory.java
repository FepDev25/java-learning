package com.cultodeportivo.factory;

import com.cultodeportivo.factory.producto.NotificacionSMS;

public class SmsFactory extends NotificacionFactory {
    @Override
    protected Notificacion crearNotificacion(String destinatario, String asunto) {
        return new NotificacionSMS(destinatario, asunto);
    }
}
