package com.cultodeportivo.datetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

// LocalDate: solo fecha (año-mes-día), sin hora ni zona horaria.
// INMUTABLE: todos los métodos plus/minus/with devuelven un NUEVO objeto.
// API del JDK 8 — reemplaza a java.util.Date y java.util.Calendar (ambos obsoletos).
public class EjemploLocalDate {
    public static void main(String[] args) {

        // --- Creación ---
        LocalDate hoy = LocalDate.now();
        System.out.println("Hoy:              " + hoy);

        // Felipe nació
        LocalDate nacimientoFelipe = LocalDate.of(2005, Month.JANUARY, 25);
        System.out.println("Nacimiento:       " + nacimientoFelipe);

        // Inicio de universidad: 2022-03-01
        LocalDate inicioUniversidad = LocalDate.parse("2022-08-01");
        System.out.println("Inicio univ.:     " + inicioUniversidad);

        // --- Extraer campos ---
        System.out.println("\n--- Campos de hoy ---");
        System.out.println("Día del mes:  " + hoy.getDayOfMonth());
        System.out.println("Mes (enum):   " + hoy.getMonth());
        System.out.println("Nº de mes:    " + hoy.getMonth().getValue());
        System.out.println("Mes en es:    " + hoy.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
        System.out.println("Año:          " + hoy.getYear());
        System.out.println("Día del año:  " + hoy.getDayOfYear());

        DayOfWeek diaSemana = hoy.getDayOfWeek();
        System.out.println("Día semana:   " + diaSemana.getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
        System.out.println("Nº día sem.:  " + diaSemana.getValue());  // 1=Lunes … 7=Domingo

        // --- Aritmética (inmutable: se asigna el resultado) ---
        System.out.println("\n--- Aritmética ---");
        LocalDate proximoExamen   = hoy.plusDays(7);
        LocalDate finSemestre     = hoy.plusMonths(4);
        LocalDate semestreAnterior = hoy.minus(6, ChronoUnit.MONTHS);

        System.out.println("Próximo examen (+ 7 días):   " + proximoExamen);
        System.out.println("Fin de semestre (+ 4 meses): " + finSemestre);
        System.out.println("Hace 6 meses:                " + semestreAnterior);

        // with: reemplaza un campo manteniendo los demás
        LocalDate primeroDiciembre = hoy.withMonth(12).withDayOfMonth(1);
        System.out.println("1° diciembre mismo año:      " + primeroDiciembre);

        // --- Comparar ---
        System.out.println("\n--- Comparar ---");
        System.out.println("isBefore (nacimiento < hoy)?  " + nacimientoFelipe.isBefore(hoy));
        System.out.println("isAfter  (hoy > inicio univ)? " + hoy.isAfter(inicioUniversidad));
        System.out.println("¿Año bisiesto?                " + hoy.isLeapYear());

        // --- Día de la semana de una fecha específica ---
        System.out.println("\nEl 15/07/2003 fue: " +
                nacimientoFelipe.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
    }
}
