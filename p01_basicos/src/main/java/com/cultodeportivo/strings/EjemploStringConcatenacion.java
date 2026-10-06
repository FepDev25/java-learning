package com.cultodeportivo.strings;

public class EjemploStringConcatenacion {
    public static void main(String[] args) {
        String nombre = "Felipe";
        String apellido = "Peralta";
        String nombreCompleto = nombre + " " + apellido;
        System.out.println("nombre completo: " + nombreCompleto);

        int pago1 = 156;
        int pago2 = 155;
        System.out.println("El pago total es de: " + (pago1 + pago2));

        String nombreCompleto2 = nombre.concat(" ").concat(apellido);
        System.out.println("nombre completo 2: " + nombreCompleto2);
    }
}
