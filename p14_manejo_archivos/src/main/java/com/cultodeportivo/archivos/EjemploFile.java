package com.cultodeportivo.archivos;

import java.io.File;
import java.io.IOException;

/**
 * java.io.File — representa una ruta (no abre el archivo).
 * Permite inspeccionar y manipular el sistema de archivos.
 */
public class EjemploFile {

    public static void main(String[] args) throws IOException {

        // ── 1. Crear referencia a un archivo (no lo crea aún) ──────────────
        String tmp = System.getProperty("java.io.tmpdir");
        File archivo = new File(tmp + "/apuntes_felipe.txt");

        System.out.println("== Info antes de crear ==");
        System.out.println("Ruta absoluta : " + archivo.getAbsolutePath());
        System.out.println("Existe        : " + archivo.exists());

        // ── 2. Crear el archivo físicamente ───────────────────────────────
        boolean creado = archivo.createNewFile();
        System.out.println("\n== Después de createNewFile() ==");
        System.out.println("Creado        : " + creado);        // false si ya existía
        System.out.println("Existe        : " + archivo.exists());
        System.out.println("Es archivo    : " + archivo.isFile());
        System.out.println("Es directorio : " + archivo.isDirectory());
        System.out.println("Tamaño bytes  : " + archivo.length());
        System.out.println("Puede leer    : " + archivo.canRead());
        System.out.println("Puede escribir: " + archivo.canWrite());

        // ── 3. Crear un directorio ─────────────────────────────────────────
        File carpeta = new File(tmp + "/notas_cs");
        boolean dirCreado = carpeta.mkdir();
        System.out.println("\n== Directorio ==");
        System.out.println("Directorio creado : " + dirCreado);
        System.out.println("Es directorio     : " + carpeta.isDirectory());

        // Crear estructura anidada con mkdirs()
        File anidado = new File(tmp + "/notas_cs/semestre_2/IA");
        anidado.mkdirs();
        System.out.println("Ruta anidada creada: " + anidado.exists());

        // ── 4. Listar contenido de un directorio ──────────────────────────
        File dirTmp = new File(tmp);
        String[] nombres = dirTmp.list();   // solo nombres
        File[]   hijos   = dirTmp.listFiles(f -> f.getName().startsWith("notas"));

        System.out.println("\n== Listar tmp (filtro 'notas*') ==");
        if (hijos != null) {
            for (File h : hijos)
                System.out.println("  " + (h.isDirectory() ? "[DIR] " : "[FILE] ") + h.getName());
        }

        // ── 5. Renombrar / mover ───────────────────────────────────────────
        File renombrado = new File(tmp + "/apuntes_felipe_v2.txt");
        boolean ok = archivo.renameTo(renombrado);
        System.out.println("\nRenombrado a _v2: " + ok);

        // ── 6. Eliminar ────────────────────────────────────────────────────
        renombrado.delete();
        System.out.println("Eliminado _v2: " + !renombrado.exists());

        // Eliminar árbol anidado (hay que borrar de adentro hacia afuera)
        eliminarRecursivo(carpeta);
        System.out.println("Carpeta notas_cs eliminada: " + !carpeta.exists());
    }

    /** Borra un directorio y todo su contenido recursivamente. */
    static void eliminarRecursivo(File dir) {
        File[] hijos = dir.listFiles();
        if (hijos != null)
            for (File h : hijos)
                eliminarRecursivo(h);
        dir.delete();
    }
}
