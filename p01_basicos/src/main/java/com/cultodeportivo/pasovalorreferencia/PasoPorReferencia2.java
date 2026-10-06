package com.cultodeportivo.pasovalorreferencia;

public class PasoPorReferencia2 {

    public static class Persona {

        private String nombre;

        public void modificarNombre(String nuevoNombre) {
            nombre = nuevoNombre;
        }

        public String leerNombre() {
            return this.nombre;
        }
    }

    public static void main(String[] args) {

        Persona persona = new Persona();
        persona.modificarNombre("Felipe");

        System.out.println("iniciamos el método main");

        System.out.println("persona.nombre = " + persona.leerNombre());
        System.out.println("Antes de llamar al método test");
        metodoTest(persona);
        System.out.println("Después de llamar al método test");

        System.out.println("persona.nombre = " + persona.leerNombre());

        System.out.println("Finaliza el método main con los datos de la persona modificados!");
    }

    public static void metodoTest(Persona persona) {
        System.out.println("Iniciamos el método test");
        persona.modificarNombre("Pedrito");
        System.out.println("Finaliza el método test");
    }
}
