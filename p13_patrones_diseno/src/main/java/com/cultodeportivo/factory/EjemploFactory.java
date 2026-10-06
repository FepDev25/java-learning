package com.cultodeportivo.factory;

// El cliente trabaja con NotificacionFactory (abstracción).
// Si mañana se agrega NotificacionWhatsapp, SOLO se crea WhatsappFactory —
// el cliente no cambia → Principio Abierto/Cerrado (OCP).
public class EjemploFactory {
    public static void main(String[] args) {

        // El cliente elige la factory según configuración/canal
        NotificacionFactory emailFactory = new EmailFactory();
        NotificacionFactory smsFactory   = new SmsFactory();
        NotificacionFactory pushFactory  = new PushFactory();

        // Email: resultado del examen de Algoritmos
        emailFactory.enviarNotificacion(
                "felipe.perez@uce.edu.ec",
                "Resultado examen Algoritmos: 9.1/10");

        System.out.println();

        // SMS: recordatorio de entrega
        smsFactory.enviarNotificacion(
                "0987654321",
                "Entrega tesis mañana 23:59 — UCE Ecuador");

        System.out.println();

        // Push: anuncio de beca
        pushFactory.enviarNotificacion(
                "Felipe Pérez",
                "¡Felicitaciones! Beca mérito semestre 6");

        System.out.println();

        // La factory también puede seleccionarse dinámicamente
        String canal = "email";
        NotificacionFactory factory = switch (canal) {
            case "sms"  -> new SmsFactory();
            case "push" -> new PushFactory();
            default     -> new EmailFactory();
        };
        factory.enviarNotificacion("felipe@uce.edu.ec", "Calificaciones publicadas");
    }
}
