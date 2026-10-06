package com.cultodeportivo.operadores;

public class OperadoresIncrementales {
    public static void main(String[] args) {
        // Pre incremento
        System.out.println("Pre incremento");
        int i = 1;
        int j = ++i; // i = i + 1

        System.out.println("i = " + i);
        System.out.println("j = " + j);
        
        // Post incremento
        System.out.println("Post incremento");
        i = 2;
        System.out.println("inicial de i = " + i);
        j = i++;
        System.out.println("i = " + i);
        System.out.println("j = " + j);

        // Pre decremento
        System.out.println("Pre decremento");
        i = 3;
        j = --i; // i = i - 1 = 2
        System.out.println("i = " + i);
        System.out.println("j = " + j);

        // Post decremento
        System.out.println("Post decremento");
        i = 4;
        j = i--;
        System.out.println("i = " + i);
        System.out.println("j = " + j);

    }
}
