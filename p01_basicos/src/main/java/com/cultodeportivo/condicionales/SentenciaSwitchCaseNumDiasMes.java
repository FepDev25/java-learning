package com.cultodeportivo.condicionales;

import java.util.Scanner;

public class SentenciaSwitchCaseNumDiasMes {

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {

            System.out.println("Ingrese el número del mes de 1 - 12");
            int mes = s.nextInt();
            
            int numeroDias;
            System.out.println("Ingrese el año (YYYY)");
            int anio = s.nextInt();
            
            switch (mes) {
                case 1, 3, 5, 7, 8, 10, 12 -> numeroDias = 31;
                case 4, 6, 9, 11 -> numeroDias = 30;
                case 2 -> {
                    if (anio % 400 == 0 || ((anio % 4 == 0) && !(anio % 100 == 0))) {
                        numeroDias = 29;
                    } else {
                        numeroDias = 28;
                    }
                }
                default -> numeroDias = 0;
            }

            System.out.println("numeroDias = " + numeroDias);
        }
    }
}
