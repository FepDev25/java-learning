package com.cultodeportivo.observer;

// Observador es @FunctionalInterface → se puede usar directamente como lambda.
// Cada lambda = un observador distinto (correo, SMS, log, padre).
public class EjemploObserver {
    public static void main(String[] args) {

        SistemaNotas algoritmos = new SistemaNotas("Algoritmos y Estructuras de Datos");

        // Observador 1: el propio Felipe recibe notificación en su app
        algoritmos.agregarObservador((obs, dato) -> {
            SistemaNotas.EventoNota e = (SistemaNotas.EventoNota) dato;
            if (e.estudiante().equals("Felipe Pérez")) {
                System.out.println("  [App] Hola Felipe, tu nota de "
                        + e.materia() + " es " + e.nota()
                        + " — " + (e.aprobado() ? "¡Aprobado!" : "Reprobado"));
            }
        });

        // Observador 2: correo automático a todos
        algoritmos.agregarObservador((obs, dato) -> {
            SistemaNotas.EventoNota e = (SistemaNotas.EventoNota) dato;
            System.out.println("  [Email] Notificación enviada a "
                    + e.estudiante() + "@uce.edu.ec → " + e.nota());
        });

        // Observador 3: log del sistema
        algoritmos.agregarObservador((obs, dato) -> {
            SistemaNotas.EventoNota e = (SistemaNotas.EventoNota) dato;
            System.out.println("  [LOG] " + e.materia() + " | "
                    + e.estudiante() + " | " + e.nota()
                    + " | aprobado=" + e.aprobado());
        });

        // Observador 4: alerta de riesgo académico para el coordinador
        algoritmos.agregarObservador((obs, dato) -> {
            SistemaNotas.EventoNota e = (SistemaNotas.EventoNota) dato;
            if (!e.aprobado()) {
                System.out.println("  [ALERTA] " + e.estudiante()
                        + " en riesgo académico — nota: " + e.nota());
            }
        });

        // El profesor publica notas → todos los observadores reaccionan
        algoritmos.publicarNota("Felipe Pérez", 9.1);
        algoritmos.publicarNota("Ana Torres",   8.5);
        algoritmos.publicarNota("Luis Mora",    5.4);   // activa alerta

        System.out.println("\n--- Felipe se desuscribe ---");
        // Remover observador no es posible con lambda anónima, necesitaría referencia.
        // Por eso en código real se guardan los observadores en variables.

        SistemaNotas redes = new SistemaNotas("Redes de Computadoras");
        // Redes no tiene observadores → publicar no notifica a nadie
        redes.publicarNota("Felipe Pérez", 8.5);
        System.out.println("  (sin observadores en Redes)");
    }
}
