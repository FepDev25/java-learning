package com.cultodeportivo.datetime;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// LocalTime: solo hora (hora:minuto:segundo:nanosegundo), sin fecha ni zona.
// Inmutable igual que LocalDate.
// Útil para horarios de clases, rangos de tiempo dentro de un día.
public class EjemploLocalTime {
    public static void main(String[] args) {

        // --- Creación ---
        LocalTime ahora = LocalTime.now();
        System.out.println("Ahora:            " + ahora);
        System.out.println("  Hora:    " + ahora.getHour());
        System.out.println("  Minuto:  " + ahora.getMinute());
        System.out.println("  Segundo: " + ahora.getSecond());

        // Horario de clase de Felipe: 08:00 y 14:30
        LocalTime claseManiana = LocalTime.of(8, 0);
        LocalTime claseTarde   = LocalTime.of(14, 30, 0);
        System.out.println("\nClase mañana: " + claseManiana);
        System.out.println("Clase tarde:  " + claseTarde);

        // parse desde String
        LocalTime entradaBiblioteca = LocalTime.parse("16:00:00");
        System.out.println("Entrada biblioteca: " + entradaBiblioteca);

        // --- Aritmética ---
        System.out.println("\n--- Aritmética ---");
        LocalTime finClaseManiana = claseManiana.plus(1, ChronoUnit.HOURS).plusMinutes(40); // 1h40m
        System.out.println("Fin clase mañana:    " + finClaseManiana);

        LocalTime descanso = claseTarde.minusMinutes(15);
        System.out.println("Descanso antes tarde:" + descanso);

        // --- Comparar ---
        System.out.println("\n--- Comparar ---");
        System.out.println("¿Clase mañana antes de tarde? " + claseManiana.isBefore(claseTarde));
        System.out.println("¿Ahora después de 06:00?      " + ahora.isAfter(LocalTime.of(6, 0)));

        // --- Formatear ---
        System.out.println("\n--- Formato ---");
        DateTimeFormatter fmt12h = DateTimeFormatter.ofPattern("hh:mm:ss a");
        DateTimeFormatter fmt24h = DateTimeFormatter.ofPattern("HH:mm");

        System.out.println("Clase mañana (12h): " + claseManiana.format(fmt12h));
        System.out.println("Clase tarde  (24h): " + claseTarde.format(fmt24h));
        System.out.println("Ahora        (12h): " + ahora.format(fmt12h));

        // --- Constantes ---
        System.out.println("\nLocalTime.MAX: " + LocalTime.MAX);   // 23:59:59.999999999
        System.out.println("LocalTime.MIN: " + LocalTime.MIN);   // 00:00
        System.out.println("LocalTime.NOON:" + LocalTime.NOON);  // 12:00
    }
}
