package com.cultodeportivo.datetime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

// Duration: diferencia entre dos instantes expresada en segundos y nanosegundos.
// Se usa con LocalDateTime o LocalTime (no con LocalDate → usa Period).
// Diferencia con Period:
//   Period   → años/meses/días  → fechas calendario (LocalDate)
//   Duration → horas/minutos/segundos → instantes precisos (LocalDateTime, Instant)
public class EjemploDuration {
    public static void main(String[] args) {

        // --- Duration entre dos LocalDateTime ---
        LocalDateTime ahora  = LocalDateTime.now();
        LocalDateTime entrega = LocalDateTime.of(2027, Month.JUNE, 30, 23, 59, 0);

        Duration tiempoParaEntrega = Duration.between(ahora, entrega);

        System.out.println("Duración ISO:          " + tiempoParaEntrega);       // PTxxxH
        System.out.println("En días:               " + tiempoParaEntrega.toDays());
        System.out.println("En horas:              " + tiempoParaEntrega.toHours());
        System.out.println("En minutos:            " + tiempoParaEntrega.toMinutes());
        System.out.println("En segundos:           " + tiempoParaEntrega.toSeconds());

        // --- Duration entre dos LocalTime ---
        LocalTime inicioEstudio = LocalTime.of(9, 0);
        LocalTime finEstudio    = LocalTime.of(13, 30);
        Duration sesion = Duration.between(inicioEstudio, finEstudio);

        System.out.printf("%nSesión de estudio de Felipe: %d horas y %d minutos%n",
                sesion.toHoursPart(),    // Java 9+: solo las horas de la parte
                sesion.toMinutesPart()); // Java 9+: solo los minutos restantes

        // --- Aritmética sobre Duration (inmutable) ---
        Duration sesionConDescanso = sesion.plusMinutes(15);  // + descanso
        Duration mitad             = sesion.dividedBy(2);
        System.out.println("Con descanso: " + sesionConDescanso.toMinutes() + " min");
        System.out.println("Mitad sesión: " + mitad.toMinutes() + " min");

        // --- Crear Duration directamente ---
        Duration dosHoras     = Duration.ofHours(2);
        Duration treintaMin   = Duration.ofMinutes(30);
        Duration unaHoraMedia = dosHoras.plus(treintaMin);
        System.out.println("\n2h + 30min = " + unaHoraMedia.toMinutes() + " min");

        // --- Negativo: si end < start ---
        Duration negativo = Duration.between(finEstudio, inicioEstudio);
        System.out.println("Duration negativa: " + negativo.toMinutes() + " min");
        System.out.println("¿Es negativa?      " + negativo.isNegative());
        System.out.println("Absoluta (abs):    " + negativo.abs().toMinutes() + " min");
    }
}
