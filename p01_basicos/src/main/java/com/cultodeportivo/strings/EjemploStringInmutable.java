package com.cultodeportivo.strings;

public class EjemploStringInmutable {
    public static void main(String[] args) {
        String curso = "Java Master";
        String profesor = "Felipe Peralta";
        String resultado = curso.concat(profesor);

        System.out.println("curso = " + curso);
        System.out.println("resultado = " + resultado);
        System.out.println(curso.equals(resultado));

        String resultado2 = curso.transform(c -> {
            return c + " con " + profesor;
        });
        System.out.println("resultado2 = " + resultado2);

        String resultado3 = resultado2.replace("a", "A");
        System.out.println("resultado3 = " + resultado3);
    }
}
