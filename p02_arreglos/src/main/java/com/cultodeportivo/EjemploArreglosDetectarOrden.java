package com.cultodeportivo;

import java.util.Scanner;

public class EjemploArreglosDetectarOrden {
    public static void main(String[] args) {

        int[] a = new int[7];

        try (Scanner s = new Scanner(System.in)) {
            System.out.println("Ingrese 7 números:");
            for(int i = 0; i<a.length; i++){
                a[i] = s.nextInt();
            }
        }
        boolean ascendente = false;
        boolean descendente = false;
        for(int i = 0; i < a.length - 1; i++){

            if(a[i] > a[i+1]){
                descendente = true;
            }

            if(a[i] < a[i+1]){
                ascendente = true;
            }
        }

        if(ascendente == true && descendente == true){
            System.out.println("Arreglo = desordenado");
        }

        if(ascendente == false && descendente == false){
            System.out.println("Arreglo = todos son iguales");
        }

        if(ascendente == true && descendente == false){
            System.out.println("Arreglo = ordenado de forma ascendente");
        }

        if(ascendente == false && descendente == true){
            System.out.println("Arreglo = ordenado de forma descendente");
        }
    }
}
