package com.cultodeportivo.entradasalida;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SistemasNumericosEntradaScanner {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese un número entero:");
            int numeroDecimal;
            try {
                numeroDecimal = scanner.nextInt();// Integer.parseInt(numeroStr);
            } catch (InputMismatchException e) {
                System.out.println("Error debe ingresar un número entero!");
                numeroDecimal = 0;
            }

            System.out.println("numeroDecimal = " + numeroDecimal);

            String resultadoBinario = "numero binario de " + numeroDecimal + " = " + Integer.toBinaryString(numeroDecimal);
            String resultadoOctal = "numero octal de " + numeroDecimal + " = " + Integer.toOctalString(numeroDecimal);
            String resultadoHex = "numero hexadecimal de " + numeroDecimal + " = " + Integer.toHexString(numeroDecimal);

            String mensaje = resultadoBinario;
            mensaje += "\n" + resultadoOctal;
            mensaje += "\n" + resultadoHex;

            System.out.println(mensaje);
        }
    }
}
