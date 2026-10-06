package com.cultodeportivo.datetime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// ZonedDateTime = LocalDateTime + ZoneId (zona horaria con reglas DST).
// Usar cuando se necesita saber la hora en distintas zonas del mundo.
// Ecuador: America/Guayaquil → UTC-5 (sin cambio de horario de verano/invierno).
// ZoneOffset: offset fijo sin reglas DST (ej: -05:00).
// ZoneId:     zona con nombre, respeta cambios de horario (ej: America/New_York → -4 verano, -5 invierno).
public class EjemploZonedDateTime {
    public static void main(String[] args) {

        // --- ZoneId de Ecuador y otras zonas ---
        ZoneId zonaEcuador  = ZoneId.of("America/Guayaquil");   // UTC-5
        ZoneId zonaMadrid   = ZoneId.of("Europe/Madrid");
        ZoneId zonaNewYork  = ZoneId.of("America/New_York");
        ZoneId zonaTokio    = ZoneId.of("Asia/Tokyo");

        // --- Crear ZonedDateTime desde LocalDateTime ---
        LocalDateTime fechaLocal = LocalDateTime.of(2025, Month.JUNE, 15, 8, 0);
        ZonedDateTime enEcuador  = fechaLocal.atZone(zonaEcuador);

        System.out.println("Salida de Felipe desde Quito: " + enEcuador);

        // --- Convertir a otras zonas (mismo instante, distinta hora local) ---
        ZonedDateTime enMadrid   = enEcuador.withZoneSameInstant(zonaMadrid);
        ZonedDateTime enNewYork  = enEcuador.withZoneSameInstant(zonaNewYork);
        ZonedDateTime enTokio    = enEcuador.withZoneSameInstant(zonaTokio);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm z", new Locale("es", "EC"));

        System.out.println("\nEl mismo instante en distintas zonas:");
        System.out.println("  Ecuador:  " + enEcuador.format(fmt));
        System.out.println("  Madrid:   " + enMadrid.format(fmt));
        System.out.println("  New York: " + enNewYork.format(fmt));
        System.out.println("  Tokio:    " + enTokio.format(fmt));

        // --- Vuelo: Quito → Madrid (duración ~12h) ---
        System.out.println("\n--- Vuelo Quito → Madrid ---");
        ZonedDateTime salidaQuito   = enEcuador;
        ZonedDateTime llegadaMadrid = salidaQuito
                .withZoneSameInstant(zonaMadrid)
                .plusHours(12);

        DateTimeFormatter fmtLegible = DateTimeFormatter.ofPattern("HH:mm, dd 'de' MMMM 'de' yyyy (z)", new Locale("es", "ES"));
        System.out.println("Salida  Quito:  " + salidaQuito.format(fmtLegible));
        System.out.println("Llegada Madrid: " + llegadaMadrid.format(fmtLegible));

        // --- ZoneOffset fijo (sin reglas DST) ---
        System.out.println("\n--- ZoneOffset fijo ---");
        ZonedDateTime conOffset = fechaLocal.atZone(ZoneOffset.of("-05:00"));
        System.out.println("Con offset -05:00: " + conOffset);

        // --- ZonedDateTime.now() en distintas zonas ---
        System.out.println("\n--- Hora actual por zona ---");
        System.out.println("Ecuador ahora:  " + ZonedDateTime.now(zonaEcuador).format(fmt));
        System.out.println("Madrid ahora:   " + ZonedDateTime.now(zonaMadrid).format(fmt));
        System.out.println("New York ahora: " + ZonedDateTime.now(zonaNewYork).format(fmt));

        // listar zonas disponibles (comentado para no saturar salida)
        // ZoneId.getAvailableZoneIds().stream().sorted().forEach(System.out::println);
    }
}
