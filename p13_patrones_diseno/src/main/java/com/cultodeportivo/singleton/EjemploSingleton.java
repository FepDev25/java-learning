package com.cultodeportivo.singleton;

public class EjemploSingleton {
    public static void main(String[] args) {

        // Todas las referencias apuntan a la misma instancia
        ConfiguracionApp cfg1 = ConfiguracionApp.getInstancia();
        ConfiguracionApp cfg2 = ConfiguracionApp.getInstancia();
        ConfiguracionApp cfg3 = ConfiguracionApp.getInstancia();

        System.out.println("cfg1: " + cfg1);
        System.out.println("cfg2: " + cfg2);

        // Demostración: modificar cfg1 afecta a cfg2 (son el mismo objeto)
        cfg1.setSemestre(6);
        System.out.println("\nDespués de setSemestre(6) en cfg1:");
        System.out.println("cfg2.getSemestre() = " + cfg2.getSemestre());  // también 6

        // Verificación de identidad de referencia
        System.out.println("\n¿cfg1 == cfg2? " + (cfg1 == cfg2));  // true
        System.out.println("¿cfg2 == cfg3? " + (cfg2 == cfg3));  // true
        System.out.println("Todas son la misma instancia: " + (cfg1 == cfg2 && cfg2 == cfg3));
    }
}
