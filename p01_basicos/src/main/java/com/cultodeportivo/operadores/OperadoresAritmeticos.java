package com.cultodeportivo.operadores;

public class OperadoresAritmeticos {
    public static void main(String[] args) {

        int i = 20, j = 4;
        System.out.println("i: " + i);
        System.out.println("j: " + j);

        int suma = i + j;
        int resta = i - j;
        int multiplicacion = i*j;
        int divisionInt = i/j;
        float divisionDouble = (float) i / (float) j;
        int resto = i % j;

        System.out.println("suma: " + suma);
        System.out.println("resta: " + resta);
        System.out.println("multiplicacion: " + multiplicacion);
        System.out.println("divisionInt: " + divisionInt);
        System.out.println("divisionDouble: " + divisionDouble);
        System.out.println("resto: " + resto);

    }
}
