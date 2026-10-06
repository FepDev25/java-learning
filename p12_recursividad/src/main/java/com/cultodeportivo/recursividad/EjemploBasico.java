package com.cultodeportivo.recursividad;

// Recursividad: un método que se llama a sí mismo para resolver un problema
// dividiéndolo en subproblemas más pequeños del mismo tipo.
//
// ESTRUCTURA OBLIGATORIA:
//   1. Caso base    → condición de parada (sin él → StackOverflowError)
//   2. Caso recursivo → llamada a sí mismo con un problema MÁS PEQUEÑO
//
// Cada llamada ocupa un frame en el call stack → profundidad limitada (~10.000 por defecto).
public class EjemploBasico {

    // ──────────────────────────────────────────────
    // FACTORIAL: n! = n * (n-1)!       base: 0! = 1
    // ──────────────────────────────────────────────
    public static long factorial(int n) {
        if (n <= 1) return 1;           // caso base
        return n * factorial(n - 1);   // caso recursivo
    }

    // ──────────────────────────────────────────────
    // FIBONACCI: fib(n) = fib(n-1) + fib(n-2)  bases: fib(0)=0, fib(1)=1
    // Nota: exponencial O(2^n) — ineficiente para n grande, sirve para entender recursividad.
    // ──────────────────────────────────────────────
    public static long fibonacci(int n) {
        if (n <= 0) return 0;           // caso base 1
        if (n == 1) return 1;           // caso base 2
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    // ──────────────────────────────────────────────
    // SUMA DE ARRAY: suma los elementos de arr[i..fin]
    // ──────────────────────────────────────────────
    public static int sumaArray(int[] arr, int i) {
        if (i == arr.length) return 0;                  // caso base: fuera del array
        return arr[i] + sumaArray(arr, i + 1);          // caso recursivo
    }

    // ──────────────────────────────────────────────
    // POTENCIA: base^exp = base * base^(exp-1)   base: base^0 = 1
    // ──────────────────────────────────────────────
    public static double potencia(double base, int exp) {
        if (exp == 0) return 1;
        if (exp < 0)  return 1.0 / potencia(base, -exp);  // soporte para exponentes negativos
        return base * potencia(base, exp - 1);
    }

    // ──────────────────────────────────────────────
    // PALÍNDROMO: ¿"ABCBA" es palíndromo?
    // Compara primer y último; luego llama con el substring interior.
    // ──────────────────────────────────────────────
    public static boolean esPalindromo(String s) {
        if (s.length() <= 1) return true;                        // caso base: 0 o 1 char
        if (s.charAt(0) != s.charAt(s.length() - 1)) return false;
        return esPalindromo(s.substring(1, s.length() - 1));    // caso recursivo
    }

    // ──────────────────────────────────────────────
    // CONTAR DÍGITOS de un número
    // ──────────────────────────────────────────────
    public static int contarDigitos(int n) {
        if (n < 0)   n = -n;    // manejar negativos
        if (n < 10)  return 1;  // caso base: un solo dígito
        return 1 + contarDigitos(n / 10);
    }

    public static void main(String[] args) {

        // Factorial
        System.out.println("=== FACTORIAL ===");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("  %2d! = %d%n", i, factorial(i));
        }

        // Fibonacci: Felipe tiene 21 años → fib(21)
        System.out.println("\n=== FIBONACCI ===");
        System.out.println("  fib(21) = " + fibonacci(21) + "  ← edad de Felipe");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("  fib(%2d) = %d%n", i, fibonacci(i));
        }

        // Suma de notas de Felipe
        System.out.println("\n=== SUMA ARRAY (notas) ===");
        int[] notas = {9, 9, 8, 8, 9, 7};
        int total = sumaArray(notas, 0);
        System.out.println("  Suma: " + total + " | Promedio: " + (total / notas.length));

        // Potencia: interés compuesto aproximado
        System.out.println("\n=== POTENCIA ===");
        System.out.printf("  2^10 = %.0f%n",  potencia(2, 10));
        System.out.printf("  1.05^4 = %.4f  ← crecimiento 5%% anual 4 años%n", potencia(1.05, 4));
        System.out.printf("  2^-3  = %.4f%n", potencia(2, -3));

        // Palíndromo
        System.out.println("\n=== PALÍNDROMO ===");
        String[] palabras = {"radar", "Felipe", "reconocer", "Ecuador", "ana"};
        for (String p : palabras) {
            System.out.printf("  %-12s → %s%n", p, esPalindromo(p.toLowerCase()) ? "palíndromo" : "no es palíndromo");
        }

        // Contar dígitos
        System.out.println("\n=== CONTAR DÍGITOS ===");
        int[] nums = {5, 21, 2003, 593, -12345};
        for (int n : nums) {
            System.out.printf("  %6d → %d dígito(s)%n", n, contarDigitos(n));
        }
    }
}
