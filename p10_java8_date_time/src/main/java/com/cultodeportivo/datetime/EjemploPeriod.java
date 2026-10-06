package com.cultodeportivo.datetime;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

// Period: diferencia entre dos LocalDate expresada en años, meses y días.
// Útil para calcular edades, duraciones de semestres, tiempo transcurrido en días calendario.
// Period.between(start, end) → siempre de menor a mayor para resultado positivo.
public class EjemploPeriod {
    public static void main(String[] args) {

        // Nacimiento de Felipe: 15 jul 2003
        LocalDate nacimiento = LocalDate.of(2003, Month.JULY, 15);
        LocalDate hoy        = LocalDate.now();

        // --- Edad de Felipe ---
        Period edad = Period.between(nacimiento, hoy);
        System.out.printf("Felipe tiene %d años, %d meses y %d días%n",
                edad.getYears(), edad.getMonths(), edad.getDays());

        // --- Duración de un semestre universitario ---
        LocalDate inicioSemestre = LocalDate.of(2025, Month.MARCH, 1);
        LocalDate finSemestre    = LocalDate.of(2025, Month.JULY, 31);
        Period duracionSemestre  = Period.between(inicioSemestre, finSemestre);
        System.out.printf("Duración del semestre: %d meses y %d días%n",
                duracionSemestre.getMonths(), duracionSemestre.getDays());

        // --- Crear Period directamente ---
        Period cincoDias   = Period.ofDays(5);
        Period dosSemanas  = Period.ofWeeks(2);
        Period unAnio      = Period.ofYears(1);
        Period personalizado = Period.of(2, 3, 10);  // 2 años, 3 meses, 10 días

        System.out.println("\nPeriods fijos:");
        System.out.println("  5 días:           " + cincoDias);
        System.out.println("  2 semanas:        " + dosSemanas);
        System.out.println("  1 año:            " + unAnio);
        System.out.println("  2a 3m 10d:        " + personalizado);

        // Aplicar period a una fecha
        LocalDate gradFelipe = hoy.plus(Period.of(2, 6, 0));  // graduación en 2.5 años
        System.out.println("\nFecha estimada de graduación de Felipe: " + gradFelipe);

        // --- with: cambiar campos de una fecha ---
        LocalDate primerJulio = finSemestre.withDayOfMonth(1).withMonth(7);
        System.out.println("Primer día de julio: " + primerJulio);
        Period diff = Period.between(inicioSemestre, primerJulio);
        System.out.printf("Inicio → 1° julio: %d meses y %d días%n",
                diff.getMonths(), diff.getDays());
    }
}
