package com.cultodeportivo.archivos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Files.walk() y Files.find() — recorrer árbol de directorios.
 *
 *  Files.walk(path)            → Stream<Path> en preorden, profundidad ilimitada
 *  Files.walk(path, maxDepth)  → limita la profundidad
 *  Files.find(path, depth, BiPredicate<Path, BasicFileAttributes>)
 *
 * Caso de uso real: indexar archivos, buscar por extensión, calcular tamaño de carpeta.
 */
public class EjemploNIOWalk {

    static final String TMP = System.getProperty("java.io.tmpdir");
    static final Path BASE  = Path.of(TMP, "repo_felipe");

    public static void main(String[] args) throws IOException {

        // ── Setup: crear árbol de directorios simulando un repo de CS ─────
        crearRepoSimulado();

        // ── 1. walk() — listar todo ────────────────────────────────────────
        System.out.println("== 1. Files.walk() — árbol completo ==");
        try (Stream<Path> stream = Files.walk(BASE)) {
            stream
                .map(p -> {
                    int nivel = BASE.relativize(p).getNameCount() - 1;
                    String indent = "  ".repeat(Math.max(0, nivel));
                    String nombre = p.getFileName() != null ? p.getFileName().toString() : p.toString();
                    String tipo   = Files.isDirectory(p) ? "[DIR] " : "[FILE] ";
                    return indent + tipo + nombre;
                })
                .forEach(System.out::println);
        }

        // ── 2. walk() + filter — solo archivos .java ──────────────────────
        System.out.println("\n== 2. Solo archivos .java ==");
        try (Stream<Path> stream = Files.walk(BASE)) {
            stream
                .filter(p -> p.toString().endsWith(".java"))
                .map(p -> "  " + BASE.relativize(p))
                .forEach(System.out::println);
        }

        // ── 3. Calcular tamaño total de una carpeta ───────────────────────
        System.out.println("\n== 3. Tamaño total de la carpeta ==");
        try (Stream<Path> stream = Files.walk(BASE)) {
            long total = stream
                .filter(Files::isRegularFile)
                .mapToLong(p -> {
                    try { return Files.size(p); }
                    catch (IOException e) { return 0L; }
                })
                .sum();
            System.out.println("  Tamaño: " + total + " bytes");
        }

        // ── 4. find() — con atributos en el predicado ─────────────────────
        System.out.println("\n== 4. Files.find() — archivos > 10 bytes ==");
        try (Stream<Path> stream = Files.find(BASE, 10,
                (p, attr) -> attr.isRegularFile() && attr.size() > 10)) {
            stream
                .map(p -> "  " + BASE.relativize(p) + " (" + p.toFile().length() + " bytes)")
                .forEach(System.out::println);
        }

        // ── 5. Contar archivos por extensión ─────────────────────────────
        System.out.println("\n== 5. Contar por extensión ==");
        try (Stream<Path> stream = Files.walk(BASE)) {
            stream
                .filter(Files::isRegularFile)
                .collect(java.util.stream.Collectors.groupingBy(
                    p -> {
                        String n = p.getFileName().toString();
                        int dot = n.lastIndexOf('.');
                        return dot == -1 ? "sin extensión" : n.substring(dot);
                    },
                    java.util.stream.Collectors.counting()
                ))
                .forEach((ext, count) -> System.out.println("  " + ext + " → " + count));
        }

        // ── Limpiar ───────────────────────────────────────────────────────
        eliminarArbol(BASE);
        System.out.println("\nRepo simulado eliminado.");
    }

    /** Crea un árbol de archivos simulando el repo de Felipe. */
    static void crearRepoSimulado() throws IOException {
        List<Path> archivos = List.of(
            BASE.resolve("README.md"),
            BASE.resolve("p01_intro/Main.java"),
            BASE.resolve("p01_intro/orden.txt"),
            BASE.resolve("p08_api_stream/src/EjemploStream.java"),
            BASE.resolve("p08_api_stream/src/modelo/Estudiante.java"),
            BASE.resolve("p13_patrones_diseno/src/singleton/Singleton.java"),
            BASE.resolve("p13_patrones_diseno/src/factory/Factory.java"),
            BASE.resolve("resources/config.properties")
        );
        for (Path a : archivos) {
            Files.createDirectories(a.getParent());
            Files.writeString(a, "// " + a.getFileName() + " — Felipe, Ecuador\n",
                    StandardCharsets.UTF_8);
        }
    }

    /** Elimina un árbol de directorios recursivamente (NIO.2). */
    static void eliminarArbol(Path raiz) throws IOException {
        try (Stream<Path> stream = Files.walk(raiz)) {
            stream
                .sorted(Comparator.reverseOrder())  // hijos antes que padres
                .forEach(p -> { try { Files.delete(p); } catch (IOException e) { /* ignorar */ } });
        }
    }
}
