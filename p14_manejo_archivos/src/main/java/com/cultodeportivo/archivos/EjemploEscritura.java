package com.cultodeportivo.archivos;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

/**
 * Formas de escribir en un archivo:
 *  1. FileWriter / BufferedWriter  — streams de caracteres (java.io)
 *  2. PrintWriter                  — métodos println/printf cómodos
 *  3. Files.writeString / write    — NIO.2 (Java 11+), más simple
 *
 * Clave: try-with-resources cierra el stream automáticamente (AutoCloseable).
 * Sin cerrar → datos pueden quedar en buffer y no llegar al disco.
 */
public class EjemploEscritura {

    static final String TMP = System.getProperty("java.io.tmpdir");

    public static void main(String[] args) throws IOException {

        // ── 1. FileWriter + BufferedWriter ────────────────────────────────
        // BufferedWriter acumula en buffer → menos I/O al disco
        String ruta1 = TMP + "/apunte1.txt";
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(ruta1, StandardCharsets.UTF_8))) {

            bw.write("Estudiante: Felipe");
            bw.newLine();
            bw.write("Carrera   : Ciencias de la Computación");
            bw.newLine();
            bw.write("Ciudad    : Quito, Ecuador");
            bw.newLine();
            bw.write("Semestre  : 5");
        }
        System.out.println("1. Escrito con BufferedWriter → " + ruta1);

        // Modo APPEND (segundo argumento true en FileWriter)
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(ruta1, StandardCharsets.UTF_8, true))) {
            bw.write("-- anexo --");
            bw.newLine();
            bw.write("Promedio: 9.2");
        }
        System.out.println("   Appended.");

        // ── 2. PrintWriter ─────────────────────────────────────────────────
        // println / printf igual que System.out
        String ruta2 = TMP + "/apunte2.txt";
        try (PrintWriter pw = new PrintWriter(
                new BufferedWriter(new FileWriter(ruta2, StandardCharsets.UTF_8)))) {

            pw.println("=== Notas CS ===");
            String[][] materias = {
                {"Algoritmos",     "9.5"},
                {"Redes",          "8.8"},
                {"Bases de Datos", "9.1"},
                {"SO",             "8.5"},
                {"IA",             "9.7"},
            };
            for (String[] m : materias)
                pw.printf("  %-20s %s%n", m[0], m[1]);
        }
        System.out.println("2. Escrito con PrintWriter  → " + ruta2);

        // ── 3. Files.writeString (NIO.2 — Java 11+) ───────────────────────
        // Más directo para strings o listas de líneas
        String ruta3 = TMP + "/apunte3.txt";
        String contenido = """
                Nombre : Felipe
                País   : Ecuador
                Nota   : esto se escribe de una sola vez
                """;
        Files.writeString(Path.of(ruta3), contenido, StandardCharsets.UTF_8);
        System.out.println("3. Escrito con Files.writeString → " + ruta3);

        // Files.write acepta List<String> (una línea por elemento)
        String ruta4 = TMP + "/apunte4.txt";
        List<String> lineas = List.of(
                "Semestre 1: aprobado",
                "Semestre 2: aprobado",
                "Semestre 3: aprobado",
                "Semestre 4: aprobado",
                "Semestre 5: en curso");
        Files.write(Path.of(ruta4), lineas, StandardCharsets.UTF_8);
        System.out.println("4. Escrito con Files.write(List) → " + ruta4);

        // ── Limpiar ───────────────────────────────────────────────────────
        for (String r : new String[]{ruta1, ruta2, ruta3, ruta4})
            Files.deleteIfExists(Path.of(r));

        System.out.println("\nArchivos temporales eliminados.");
    }
}
