package com.cultodeportivo.interfacefuncional;

// @FunctionalInterface: exactamente 1 método abstracto.
// Puede tener métodos default y static sin romper la regla.
// Garantiza que el compilador rechace una segunda firma abstracta.
@FunctionalInterface
public interface Operacion {
    double calcular(double a, double b);
}
