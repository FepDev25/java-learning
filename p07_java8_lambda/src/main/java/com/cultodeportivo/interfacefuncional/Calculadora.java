package com.cultodeportivo.interfacefuncional;

import java.util.function.BiFunction;

public class Calculadora {

    // Recibe la lambda como parámetro → inyección de comportamiento
    public double computar(double a, double b, Operacion op) {
        return op.calcular(a, b);
    }

    // Equivalente usando BiFunction del JDK (no necesita interfaz propia)
    public double computarConBiFunction(double a, double b, BiFunction<Double, Double, Double> fn) {
        return fn.apply(a, b);
    }
}
