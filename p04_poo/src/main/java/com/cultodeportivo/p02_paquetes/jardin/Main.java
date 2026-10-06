package com.cultodeportivo.p02_paquetes.jardin;

import com.cultodeportivo.p02_paquetes.hogar.Persona;

public class Main {
    public static void main(String[] args) {
        Perro perro = new Perro();
        perro.nombre = "Firulais";
        perro.raza = "Labrador";

        Persona persona = new Persona();
        persona.setNombre("Juan");

        System.out.println(perro.jugar(persona));

        // No se puede acceder a la clase Gato desde otro paquete
        // Gato gato = new Gato();
    }   
}
