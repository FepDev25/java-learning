package com.cultodeportivo.fundamentos;

public class ConversionDeTipos {
    public static void main(String[] args) {
        // Conversion implicita
        int numeroInt = 100;
        long numeroLong = numeroInt; // Conversion implicita de int a long
        System.out.println("Numero Long: " + numeroLong);

        float numeroFloat = 13.4f;
        double numeroDouble = numeroFloat;
        System.out.println("Numero double: " + numeroDouble);

        // Conversion explicita (casting)
        numeroDouble = 99.99;
        int numeroIntFromDouble = (int) numeroDouble; // Conversion explicita de double a int
        System.out.println("Numero Int from Double: " + numeroIntFromDouble);

        // Conversion entre tipos compatibles
        String numeroStr = "123";
        int numeroFromString = Integer.parseInt(numeroStr); // Convertir String a int
        System.out.println("Numero from String: " + numeroFromString);

        // Conversion de tipos no compatibles
        String booleanStr = "true";
        boolean booleanFromString = Boolean.parseBoolean(booleanStr); // Convertir String a boolean
        System.out.println("Boolean from String: " + booleanFromString);
    }
}
