package com.cultodeportivo.strings;

public class EjemploStringTestRendimientoConcat {

    public static void main(String[] args) {
        String a = "a";
        String b = "b";
        String c = a;

        long inicio = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            c = c.concat(a).concat(b).concat("\n"); // 500 => 2ms, 1000 => 4ms, 10000 => 105ms, 100000 => 5071ms
        }
        System.gc();
        long fin = System.currentTimeMillis();
        System.out.println("Concat: " + (fin - inicio));

        inicio = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            c += a + b + "\n"; // 500 => 19, 1000 => 21ms, 10000 => 68, 100000 => 1976ms
        }
        System.gc();
        fin = System.currentTimeMillis();
        System.out.println("Suma: " + (fin - inicio));

        StringBuilder sb = new StringBuilder(a);
        inicio = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            sb.append(a).append(b).append("\n"); // 500 => 0ms, 1000 => 0ms, 10000 => 2ms, 100000 => 8ms
        }
        System.gc();
        fin = System.currentTimeMillis();
        System.out.println("String builder: " + (fin - inicio));

        // Output:
        // Concat: 4472
        // Suma: 4829
        // String builder: 9

        System.exit(0);
    }
}
