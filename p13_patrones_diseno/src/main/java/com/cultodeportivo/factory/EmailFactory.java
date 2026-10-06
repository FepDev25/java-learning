package com.cultodeportivo.factory;

import com.cultodeportivo.factory.producto.NotificacionEmail;

public class EmailFactory extends NotificacionFactory {
    @Override
    protected Notificacion crearNotificacion(String destinatario, String asunto) {
        return new NotificacionEmail(destinatario, asunto);
    }
}
