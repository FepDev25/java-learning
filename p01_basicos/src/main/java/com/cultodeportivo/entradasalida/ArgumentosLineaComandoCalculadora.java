package com.cultodeportivo.entradasalida;

public class ArgumentosLineaComandoCalculadora {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Por favor ingresar una operación "
                    + "(suma, resta, div o multi) y dos enteros");
            System.exit(-1);
        }

        String operacion = args[0];
        int a;
        int b;
        double resultado;

        try {
            a = Integer.parseInt(args[1]);
            b = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Cuidado a y b deben ser enteros, vuelva a intentar!");
            a=-1;
            b=-1;
        }

        switch (operacion) {
            case "suma" -> resultado = a + b;
            case "resta" -> resultado = a - b;
            case "multi" -> resultado = a * b;
            case "div" -> {
                if (b == 0) {
                    System.err.println("No se puede dividir por zero!");
                    System.exit(-1);
                }
                resultado = (double) a / b;
            }
            default -> resultado = a + b;
        }

        System.out.println("Resultado de la operación '" + operacion + "' es: " + resultado);

        /*
            ~/Learning/Udemy_Master_Java/p01_basicos/src/main/java main* ❯ javac com/cultodeportivo/entradasalida/ArgumentosLineaComandoCalculadora.java
            ~/Learning/Udemy_Master_Java/p01_basicos/src/main/java main* ❯ java com.cultodeportivo.entradasalida.ArgumentosLineaComandoCalculadora resta 67 12
            Resultado de la operación 'resta' es: 55.0
         */
    }
}
