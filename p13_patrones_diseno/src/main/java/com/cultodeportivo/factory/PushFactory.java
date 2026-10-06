package com.cultodeportivo.factory;

import com.cultodeportivo.factory.producto.NotificacionPush;

public class PushFactory extends NotificacionFactory {
    @Override
    protected Notificacion crearNotificacion(String destinatario, String asunto) {
        return new NotificacionPush(destinatario, asunto);
    }
}
