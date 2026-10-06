package com.cultodeportivo.condicionales;

import java.util.Scanner;

public class OperadorTernario {

    public static void main(String[] args) {
        
        double matematicas;
        double ciencias;
        double historia;
        double promedio;
        String estado;

        try (Scanner s = new Scanner(System.in)) {
            System.out.println("Ingrese la nota de matemáticas entre 2.0 - 7.0:");
            matematicas = s.nextDouble();

            System.out.println("Ingrese la nota de ciencias entre 2.0 - 7.0:");
            ciencias = s.nextDouble();

            System.out.println("Ingrese la nota de historia entre 2.0 - 7.0:");
            historia = s.nextDouble();

            promedio = (matematicas + ciencias + historia) / 3;
            System.out.println("promedio = " + promedio);

            estado = promedio >= 5.49 ? "Aprobado" : "Rechazado";
            System.out.println("estado = " + estado);

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
