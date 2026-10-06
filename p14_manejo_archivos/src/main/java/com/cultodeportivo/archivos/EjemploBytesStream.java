package com.cultodeportivo.archivos;

import java.io.*;
import java.nio.file.*;

/**
 * Streams de bytes (java.io):
 *  FileOutputStream / FileInputStream — para cualquier tipo de archivo (imágenes, PDFs, binarios).
 *  Para texto es mejor usar streams de caracteres (FileWriter/Reader) o NIO.2.
 *
 *  Aquí simulamos la copia de un archivo binario byte a byte y con buffer.
 */
public class EjemploBytesStream {

    static final String TMP = System.getProperty("java.io.tmpdir");

    public static void main(String[] args) throws IOException {

        Path origen  = Path.of(TMP + "/imagen_cs.bin");
        Path destino = Path.of(TMP + "/imagen_cs_copia.bin");

        // ── Crear archivo de prueba con bytes simulados ────────────────────
        try (FileOutputStream fos = new FileOutputStream(origen.toFile())) {
            byte[] datos = new byte[1024];
            for (int i = 0; i < datos.length; i++) datos[i] = (byte)(i % 256);
            fos.write(datos);
        }
        System.out.println("Archivo origen creado: " + origen.toFile().length() + " bytes");

        // ── 1. Copia byte a byte (lento, solo didáctico) ───────────────────
        long inicio = System.currentTimeMillis();
        try (FileInputStream  fis = new FileInputStream(origen.toFile());
             FileOutputStream fos = new FileOutputStream(destino.toFile())) {
            int b;
            while ((b = fis.read()) != -1)
                fos.write(b);
        }
        System.out.printf("1. Copia byte a byte   → %d bytes en %d ms%n",
                destino.toFile().length(), System.currentTimeMillis() - inicio);

        Files.deleteIfExists(destino);

        // ── 2. Copia con buffer (eficiente) ───────────────────────────────
        inicio = System.currentTimeMillis();
        byte[] buffer = new byte[8192];
        try (FileInputStream  fis = new FileInputStream(origen.toFile());
             FileOutputStream fos = new FileOutputStream(destino.toFile())) {
            int leidos;
            while ((leidos = fis.read(buffer)) != -1)
                fos.write(buffer, 0, leidos);
        }
        System.out.printf("2. Copia con buffer    → %d bytes en %d ms%n",
                destino.toFile().length(), System.currentTimeMillis() - inicio);

        Files.deleteIfExists(destino);

        // ── 3. BufferedInputStream / BufferedOutputStream ─────────────────
        // Encapsula el buffer automáticamente
        inicio = System.currentTimeMillis();
        try (BufferedInputStream  bis = new BufferedInputStream(new FileInputStream(origen.toFile()));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destino.toFile()))) {
            int b;
            while ((b = bis.read()) != -1)
                bos.write(b);
        }
        System.out.printf("3. Buffered streams    → %d bytes en %d ms%n",
                destino.toFile().length(), System.currentTimeMillis() - inicio);

        // ── 4. Files.copy() — la forma NIO.2 recomendada ──────────────────
        Files.deleteIfExists(destino);
        inicio = System.currentTimeMillis();
        Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        System.out.printf("4. Files.copy (NIO.2)  → %d bytes en %d ms%n",
                destino.toFile().length(), System.currentTimeMillis() - inicio);

        // ── Limpiar ───────────────────────────────────────────────────────
        Files.deleteIfExists(origen);
        Files.deleteIfExists(destino);
        System.out.println("\nArchivos temporales eliminados.");
    }
}
