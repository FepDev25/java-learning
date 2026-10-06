package com.cultodeportivo.archivos;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Formas de leer archivos:
 *  1. BufferedReader.readLine()       — línea a línea (java.io)
 *  2. Scanner                         — tokenización, útil para parsear
 *  3. Files.readAllLines()            — todas en List<String> (NIO.2)
 *  4. Files.readString()              — todo el archivo en un String (Java 11+)
 *  5. Files.lines() (Stream<String>)  — lazy, ideal para archivos grandes
 */
public class EjemploLectura {

    static final String TMP = System.getProperty("java.io.tmpdir");

    public static void main(String[] args) throws IOException {

        // Preparar archivo de prueba
        Path ruta = Path.of(TMP + "/notas_felipe.txt");
        Files.write(ruta, List.of(
                "Felipe,21,Ecuador,Algoritmos,9.5",
                "Felipe,21,Ecuador,Redes,8.8",
                "Felipe,21,Ecuador,Bases de Datos,9.1",
                "Felipe,21,Ecuador,SO,8.5",
                "Felipe,21,Ecuador,IA,9.7"
        ), StandardCharsets.UTF_8);

        // ── 1. BufferedReader.readLine() ───────────────────────────────────
        System.out.println("== 1. BufferedReader ==");
        try (BufferedReader br = new BufferedReader(
                new FileReader(ruta.toFile(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null)
                System.out.println("  " + linea);
        }

        // ── 2. Scanner ─────────────────────────────────────────────────────
        // Permite tokenizar por delimitador
        System.out.println("\n== 2. Scanner (parseo CSV) ==");
        try (Scanner sc = new Scanner(ruta.toFile(), StandardCharsets.UTF_8)) {
            sc.useDelimiter("[,\n\r]+");
            while (sc.hasNext()) {
                String nombre  = sc.next();
                String edad    = sc.next();
                String pais    = sc.next();
                String materia = sc.next();
                String nota    = sc.next();
                System.out.printf("  %-8s (%s) %-20s → %s%n", nombre, edad, materia, nota);
            }
        }

        // ── 3. Files.readAllLines() ────────────────────────────────────────
        // Carga todo en memoria: OK para archivos pequeños
        System.out.println("\n== 3. Files.readAllLines() ==");
        List<String> todas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
        todas.forEach(l -> System.out.println("  " + l));
        System.out.println("  Total líneas: " + todas.size());

        // ── 4. Files.readString() ──────────────────────────────────────────
        System.out.println("\n== 4. Files.readString() ==");
        String todo = Files.readString(ruta, StandardCharsets.UTF_8);
        System.out.println("  Caracteres totales: " + todo.length());
        System.out.println("  Primeros 40: " + todo.substring(0, 40) + "...");

        // ── 5. Files.lines() — Stream lazy ────────────────────────────────
        // Ideal para archivos grandes: no carga todo en memoria
        System.out.println("\n== 5. Files.lines() + Stream (notas >= 9.0) ==");
        try (Stream<String> stream = Files.lines(ruta, StandardCharsets.UTF_8)) {
            stream
                .map(l -> l.split(","))
                .filter(p -> Double.parseDouble(p[4]) >= 9.0)
                .forEach(p -> System.out.printf("  %-20s → %s%n", p[3], p[4]));
        }

        // ── Limpiar ───────────────────────────────────────────────────────
        Files.deleteIfExists(ruta);
        System.out.println("\nArchivo temporal eliminado.");
    }
}
