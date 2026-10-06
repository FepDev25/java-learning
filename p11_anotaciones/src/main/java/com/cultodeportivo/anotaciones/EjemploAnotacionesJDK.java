package com.cultodeportivo.anotaciones;

import java.util.ArrayList;
import java.util.List;

// Anotaciones integradas del JDK: no requieren definición propia.
// Son metadatos: instrucciones para el compilador, la JVM o herramientas externas.
public class EjemploAnotacionesJDK {

    // @Override → el compilador verifica que REALMENTE se sobreescribe un método del padre.
    // Sin ella, un error de typo crearía un método nuevo silenciosamente.
    @Override
    public String toString() {
        return "EjemploAnotacionesJDK{}";
    }

    // @Deprecated → marca como obsoleto; el compilador genera advertencia al usarlo.
    // Desde Java 9: @Deprecated(since="2.0", forRemoval=true)
    @Deprecated(since = "1.0", forRemoval = true)
    public static String calcularPromedio(double[] notas) {
        double suma = 0;
        for (double n : notas) suma += n;
        return "Promedio: " + (suma / notas.length);
    }

    // @SuppressWarnings → silencia advertencias específicas del compilador.
    // "unchecked" → cast no verificado, "deprecation" → uso de métodos deprecated.
    @SuppressWarnings("deprecation")
    public static void usarMetodoDeprecado() {
        double[] notas = {9.1, 8.5, 7.8};
        System.out.println(calcularPromedio(notas));  // sin @SuppressWarnings daría warning
    }

    // @FunctionalInterface → verifica que la interfaz tiene exactamente 1 método abstracto.
    @FunctionalInterface
    interface Calificador {
        String calificar(double nota);
    }

    public static void main(String[] args) {
        // @Override
        System.out.println(new EjemploAnotacionesJDK());

        // @Deprecated (se puede usar con @SuppressWarnings)
        usarMetodoDeprecado();

        // @FunctionalInterface: se usa como lambda
        Calificador c = nota -> nota >= 7.0 ? "Aprobado" : "Reprobado";
        System.out.println("Felipe con 8.9: " + c.calificar(8.9));
        System.out.println("Felipe con 5.5: " + c.calificar(5.5));

        // @SuppressWarnings("unchecked") para raw types
        @SuppressWarnings("unchecked")
        List lista = new ArrayList();   // raw type sin genérico → warning suprimido
        lista.add("Ecuador");
        System.out.println("Lista sin genérico: " + lista);
    }
}
