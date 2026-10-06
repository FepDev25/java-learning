package com.cultodeportivo.fundamentos;

public class PrimitivosCaracteres {
    public static void main(String[] args) {
        char caracter = '\u0040'; // @
        char decimal = 64; // @
        System.out.println("caracter = " + caracter);
        System.out.println("decimal = " + decimal);
        System.out.println("decimal = caracter: " + (decimal == caracter));

        char simbolo = '@';
        System.out.println("simbolo = " + simbolo);
        System.out.println("simbolo = caracter: " + (simbolo == caracter));

        char espacio = '\u0020';
        char retroceso = '\b';
        char tabulador = '\t';
        char nuevaLinea = '\n';
        char retornoCarro = '\r';

        System.out.println("Antes:" + espacio + "despues de espacio");
        System.out.println("Antes:" + retroceso + "despues de retroceso");
        System.out.println("Antes:" + tabulador + "despues de tabulador");
        System.out.println("Antes:" + nuevaLinea + "despues de nuevaLinea");
        System.out.println("Antes:" + retornoCarro + "despues de retornoCarro");

        System.out.println("char corresponde en byte:" + System.lineSeparator() + Character.BYTES);
        System.out.println("Char corresponde en bites = " + Character.SIZE);
        System.out.println("Character.MIN_VALUE = " + Character.MIN_VALUE);
        System.out.println("Character.MAX_VALUE = " + Character.MAX_VALUE);
    
    }
}
