package com.cultodeportivo.p02_paquetes.hogar;

public class Main {
    public static void main(String[] args) {
        Persona persona = new Persona();
        persona.setNombre("Ana");
        persona.setColorPelo(ColorPelo.CAFE);

        System.out.println("Nombre: " + persona.getNombre());
        System.out.println("Color de pelo: " + persona.getColorPelo());

        Gato gato = new Gato(); // Clase Gato accesible dentro del mismo paquete
        System.out.println(gato);
    }
}
