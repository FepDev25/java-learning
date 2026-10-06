package com.cultodeportivo.properties;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Properties;

/**
 * java.util.Properties — almacena pares clave=valor.
 *
 * Formato .properties:
 *   clave=valor
 *   # comentario
 *   clave.anidada=valor con espacios
 *
 * Usos reales: configuración de aplicaciones, internacionalización (i18n),
 * credenciales de entorno (con cuidado), Spring application.properties.
 */
public class EjemploProperties {

    static final String TMP = System.getProperty("java.io.tmpdir");

    public static void main(String[] args) throws IOException {

        // ── 1. Crear y guardar Properties ────────────────────────────────
        Properties config = new Properties();
        config.setProperty("app.nombre",     "GestorNotas");
        config.setProperty("app.version",    "1.0.0");
        config.setProperty("app.estudiante", "Felipe");
        config.setProperty("app.ciudad",     "Quito");
        config.setProperty("app.carrera",    "Ciencias de la Computacion");
        config.setProperty("db.host",        "localhost");
        config.setProperty("db.puerto",      "5432");
        config.setProperty("db.nombre",      "notas_db");

        Path ruta = Path.of(TMP + "/config.properties");

        // store() escribe con comentario de cabecera
        try (Writer w = new OutputStreamWriter(
                new FileOutputStream(ruta.toFile()), StandardCharsets.UTF_8)) {
            config.store(w, "Configuración de GestorNotas — Felipe, UCE Ecuador");
        }
        System.out.println("Properties guardado en: " + ruta);
        System.out.println("Contenido:\n" + Files.readString(ruta, StandardCharsets.UTF_8));

        // ── 2. Cargar Properties ──────────────────────────────────────────
        Properties cargado = new Properties();
        try (Reader r = new InputStreamReader(
                new FileInputStream(ruta.toFile()), StandardCharsets.UTF_8)) {
            cargado.load(r);
        }

        System.out.println("== Propiedades cargadas ==");
        System.out.println("  app.nombre     : " + cargado.getProperty("app.nombre"));
        System.out.println("  app.estudiante : " + cargado.getProperty("app.estudiante"));
        System.out.println("  db.host        : " + cargado.getProperty("db.host"));
        System.out.println("  no.existe      : " + cargado.getProperty("no.existe", "valor por defecto"));

        // ── 3. Iterar todas las propiedades ───────────────────────────────
        System.out.println("\n== Todas las propiedades (ordenadas) ==");
        cargado.stringPropertyNames().stream()
               .sorted()
               .forEach(k -> System.out.printf("  %-25s = %s%n", k, cargado.getProperty(k)));

        // ── 4. Propiedades del sistema (JVM) ─────────────────────────────
        System.out.println("\n== Propiedades del sistema (muestra) ==");
        Properties sys = System.getProperties();
        String[] clavesSys = {"java.version", "java.vendor", "os.name", "user.home"};
        for (String k : clavesSys)
            System.out.printf("  %-15s = %s%n", k, sys.getProperty(k));

        // ── 5. Cargar desde classpath (simulado) ──────────────────────────
        // En proyectos reales: getClass().getResourceAsStream("/config.properties")
        System.out.println("\n(En un proyecto Maven: src/main/resources/config.properties");
        System.out.println(" se carga con: getClass().getResourceAsStream(\"/config.properties\"))");

        // ── Limpiar ───────────────────────────────────────────────────────
        Files.deleteIfExists(ruta);
        System.out.println("\nArchivo eliminado.");
    }
}
