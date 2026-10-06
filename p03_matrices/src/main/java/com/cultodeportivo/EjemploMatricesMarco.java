package com.cultodeportivo;

public class EjemploMatricesMarco {
    public static void main(String[] args) {

        int[][] matriz = new int[5][5];

        for(int i = 0; i < matriz.length;i++){
            for(int j = 0; j < matriz[i].length; j++){
                if(i == 0 || i == matriz.length - 1
                || j == 0 || j == matriz[i].length -1 || i == j){
                    matriz[i][j] = 1;
                }
            }
        }

        for (int[] matriz1 : matriz) {
            for (int j = 0; j < matriz1.length; j++) {
                System.out.print(matriz1[j] + "\t");
            }
            System.out.println();
        }
    }
}
