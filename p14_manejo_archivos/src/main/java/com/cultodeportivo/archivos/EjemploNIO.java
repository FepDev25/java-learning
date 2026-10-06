package com.cultodeportivo.archivos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * NIO.2 (java.nio.file) — API moderna desde Java 7.
 *
 * Ventajas sobre java.io.File:
 *  - Manejo de errores con excepciones detalladas (no boolean)
 *  - Operaciones atómicas y opciones de copia/mover
 *  - Soporte nativo de rutas multiplataforma con Path
 *  - Files.walk / Files.find para recorrer directorios
 *
 * Clases clave: Path, Paths, Files, StandardOpenOption, StandardCopyOption
 */
public class EjemploNIO {

    static final String TMP = System.getProperty("java.io.tmpdir");

    public static void main(String[] args) throws IOException {

        // ── 1. Path ────────────────────────────────────────────────────────
        Path p = Path.of(TMP, "cs", "apunte.txt");  // multiplataforma
        System.out.println("== 1. Path ==");
        System.out.println("  toString   : " + p);
        System.out.println("  fileName   : " + p.getFileName());
        System.out.println("  parent     : " + p.getParent());
        System.out.println("  root       : " + p.getRoot());
        System.out.println("  nameCount  : " + p.getNameCount());
        System.out.println("  subpath    : " + p.subpath(1, p.getNameCount()));
        System.out.println("  absolute?  : " + p.isAbsolute());
        System.out.println("  toAbsolute : " + p.toAbsolutePath());

        // ── 2. Crear directorios y archivo ────────────────────────────────
        System.out.println("\n== 2. Crear estructura ==");
        Files.createDirectories(p.getParent());                        // mkdirs equivalente
        Files.writeString(p, "Notas de Felipe - Quito\n", StandardCharsets.UTF_8);
        System.out.println("  Creado: " + p.toAbsolutePath());

        // Append con OpenOption
        Files.writeString(p, "Materia: Algoritmos — Nota: 9.5\n",
                StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        Files.writeString(p, "Materia: IA          — Nota: 9.7\n",
                StandardCharsets.UTF_8, StandardOpenOption.APPEND);

        // ── 3. Leer ────────────────────────────────────────────────────────
        System.out.println("\n== 3. Leer ==");
        String contenido = Files.readString(p, StandardCharsets.UTF_8);
        System.out.print("  " + contenido.replace("\n", "\n  "));

        List<String> lineas = Files.readAllLines(p, StandardCharsets.UTF_8);
        System.out.println("  Líneas: " + lineas.size());

        // ── 4. Metadatos (BasicFileAttributes) ────────────────────────────
        System.out.println("\n== 4. Atributos ==");
        BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
        System.out.println("  Tamaño       : " + attr.size() + " bytes");
        System.out.println("  Creación     : " + attr.creationTime());
        System.out.println("  Últ. acceso  : " + attr.lastAccessTime());
        System.out.println("  Últ. modif.  : " + attr.lastModifiedTime());
        System.out.println("  Es regular?  : " + attr.isRegularFile());

        // ── 5. Copiar y mover ──────────────────────────────────────────────
        System.out.println("\n== 5. Copiar / Mover ==");
        Path copia  = Path.of(TMP, "cs", "apunte_copia.txt");
        Path movido = Path.of(TMP, "cs", "apunte_v2.txt");

        Files.copy(p, copia, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("  Copiado a: " + copia.getFileName());

        Files.move(copia, movido, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("  Movido a : " + movido.getFileName());

        // ── 6. Comprobar y eliminar ───────────────────────────────────────
        System.out.println("\n== 6. Eliminar ==");
        System.out.println("  Existe apunte.txt : " + Files.exists(p));
        Files.deleteIfExists(p);
        Files.deleteIfExists(movido);
        System.out.println("  Existe después    : " + Files.exists(p));

        // Borrar directorio (solo si está vacío)
        Files.deleteIfExists(Path.of(TMP, "cs"));
        System.out.println("  Directorio 'cs' eliminado.");
    }
}
