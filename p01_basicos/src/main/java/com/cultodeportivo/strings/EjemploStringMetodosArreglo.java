package com.cultodeportivo.strings;

import java.util.Arrays;

public class EjemploStringMetodosArreglo {
    public static void main(String[] args) {
         
        String trabalenguas = "trabalenguas";
        System.out.println("trabalenguas.length() = " + trabalenguas.length());

        char[] arreglo = trabalenguas.toCharArray();
        System.out.println("trabalenguas.toCharArray() = " + Arrays.toString(arreglo));

        int largo = arreglo.length;
        System.out.println("largo = " + largo);

        for(int i = 0; i < largo; i++){
            System.out.print(arreglo[i]);
        }

        String[] arreglo2 = trabalenguas.split("a");
        System.out.println("\nrabalenguas.split(\"a\") = " + Arrays.toString(arreglo2));

        int l = arreglo2.length;
        for(int j=0; j<l; j++){
            System.out.println(arreglo2[j]);
        }

    }
}
