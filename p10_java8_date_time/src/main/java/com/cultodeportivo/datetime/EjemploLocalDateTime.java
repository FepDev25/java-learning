package com.cultodeportivo.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

// LocalDateTime = LocalDate + LocalTime, sin zona horaria.
// Útil para fechas/horas locales donde la zona no importa (ej.: eventos de una sola ciudad).
// Inmutable, thread-safe.
public class EjemploLocalDateTime {
    public static void main(String[] args) {

        // --- Creación ---
        LocalDateTime ahora = LocalDateTime.now();
        System.out.println("Ahora:       " + ahora);

        // Felipe entrega su proyecto el 2025-06-30 a las 23:59
        LocalDateTime entrega = LocalDateTime.of(2025, Month.JUNE, 30, 23, 59, 0);
        System.out.println("Entrega:     " + entrega);

        // Combinando LocalDate + LocalTime
        LocalDateTime inicioClase = LocalDateTime.of(
                LocalDate.of(2025, 3, 13),
                LocalTime.of(8, 0)
        );
        System.out.println("Inicio clase:" + inicioClase);

        // parse: el separador T es el estándar ISO-8601
        LocalDateTime examen = LocalDateTime.parse("2025-07-15T09:00:00");
        System.out.println("Examen final:" + examen);

        // --- Extraer campos ---
        System.out.println("\n--- Campos de la entrega ---");
        System.out.println("Mes:  " + entrega.getMonth());
        System.out.println("Día:  " + entrega.getDayOfMonth());
        System.out.println("Año:  " + entrega.getYear());
        System.out.println("Hora: " + entrega.getHour() + ":" + entrega.getMinute());

        // --- Aritmética (inmutable) ---
        System.out.println("\n--- Aritmética ---");
        LocalDateTime recordatorio = entrega.minusHours(2);   // 2h antes de la entrega
        LocalDateTime defensa      = entrega.plusDays(3).withHour(10).withMinute(0);

        System.out.println("Recordatorio: " + recordatorio);
        System.out.println("Defensa:      " + defensa);
        System.out.println("Original sin cambios: " + entrega);   // inmutabilidad

        // --- Formatear / parsear con patrón custom ---
        System.out.println("\n--- Formato ---");
        DateTimeFormatter fmtCustom  = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter fmtLegible = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'de' yyyy, HH:mm",
                new java.util.Locale("es", "EC"));

        System.out.println("ISO:      " + entrega.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("Custom:   " + entrega.format(fmtCustom));
        System.out.println("Legible:  " + entrega.format(fmtLegible));

        // parse con patrón custom
        LocalDateTime desde = LocalDateTime.parse("15/07/2025 10:30:00", fmtCustom);
        System.out.println("Parseado: " + desde);
    }
}
