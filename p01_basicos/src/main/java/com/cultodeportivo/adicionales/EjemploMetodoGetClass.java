package com.cultodeportivo.adicionales;

import java.lang.reflect.Method;

public class EjemploMetodoGetClass {

    public static void main(String[] args) {
        String texto = "Hola que tal!";

        @SuppressWarnings("rawtypes")
        Class strClass = texto.getClass();

        System.out.println("strClass.getName() = " + strClass.getName());
        System.out.println("strClass.getName() = " + strClass.getSimpleName());
        System.out.println("strClass.getName() = " + strClass.getPackageName());
        System.out.println("strClass.getName() = " + strClass);

        for (Method metodo : strClass.getMethods()) {
            System.out.println("metodo.getName() = " + metodo.getName());
        }

        Integer num = 34;

        @SuppressWarnings("rawtypes")
        Class intClass = num.getClass();
        
        @SuppressWarnings("rawtypes")
        Class objClass = intClass.getSuperclass().getSuperclass();

        System.out.println("intClass.getSuperclass().getSuperclass() = " + intClass.getSuperclass());
        System.out.println("intClass.getSuperclass().getSuperclass() = " + objClass);

        for (Method metodo : objClass.getMethods()) {
            System.out.println("metodo.getName() = " + metodo.getName());
        }
    }
}
