package com.cultodeportivo.entradasalida;

public class ArgumentosLineaComando {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Debe ingresar argumentos o parámetros!");
            System.exit(-1);
        }
        for (int i = 0; i < args.length; i++) {
            System.out.println("Argumentos nº " + i + ": " + args[i]);
        }

        /*
            ~/Learning/Udemy_Master_Java/p01_basicos/src/main/java main* ❯ javac com/cultodeportivo/entradasalida/ArgumentosLineaComando.java
            ~/Learning/Udemy_Master_Java/p01_basicos/src/main/java main* ❯ java com.cultodeportivo.entradasalida.ArgumentosLineaComando Felipe Juan Emilia
            Argumentos nº 0: Felipe
            Argumentos nº 1: Juan
            Argumentos nº 2: Emilia
         */
    }
}
