package com.cultodeportivo.interfacefuncional;

// Interfaz funcional propia + lambda + referencia de método.
// Una lambda ES una implementación anónima de la interfaz funcional.
public class EjemploInterfaceFuncional {
    public static void main(String[] args) {

        // Lambda asignada a variable (como función de primera clase)
        Operacion suma  = (a, b) -> a + b;
        Operacion resta = (a, b) -> a - b;

        // Referencia de método estático como lambda
        Operacion max = Math::max;

        Calculadora calc = new Calculadora();

        double creditosAlgoritmos = 4.0;
        double creditosRedes      = 3.0;

        System.out.println("Créditos totales (suma):  " + calc.computar(creditosAlgoritmos, creditosRedes, suma));
        System.out.println("Diferencia (resta):        " + calc.computar(creditosAlgoritmos, creditosRedes, resta));
        System.out.println("Materia con más créditos:  " + calc.computar(creditosAlgoritmos, creditosRedes, max));

        // Lambda inline al momento de llamar
        System.out.println("Producto créditos:         " + calc.computar(creditosAlgoritmos, creditosRedes, (a, b) -> a * b));

        // BiFunction del JDK: misma potencia, sin definir interfaz
        System.out.println("BiFunction potencia:       " + calc.computarConBiFunction(2, 10, Math::pow));
    }
}
