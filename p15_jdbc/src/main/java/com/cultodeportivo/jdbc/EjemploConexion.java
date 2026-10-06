package com.cultodeportivo.jdbc;

import com.cultodeportivo.config.Conexion;
import com.cultodeportivo.config.ConexionPool;

import java.sql.*;

/**
 * Punto de entrada: verificar conexión con DriverManager y con HikariCP.
 *
 * ANTES DE EJECUTAR:
 *   cd p15_jdbc
 *   docker compose up -d
 *   (esperar a que el healthcheck pase: docker compose ps)
 *
 * Luego ejecutar este main.
 */
public class EjemploConexion {

    public static void main(String[] args) {

        System.out.println("═══════════════════════════════════════");
        System.out.println(" JDBC — Verificación de conexiones");
        System.out.println("═══════════════════════════════════════");

        // ── 1. DriverManager (conexión directa) ───────────────────────────
        System.out.println("\n── 1. DriverManager ──────────────────");
        Conexion.verificar();

        // ── 2. Ejecutar una consulta simple ───────────────────────────────
        System.out.println("\n── 2. Primera consulta SQL ───────────");
        try (Connection conn = Conexion.obtener();
             Statement  stmt = conn.createStatement();
             ResultSet  rs   = stmt.executeQuery("SELECT version()")) {

            if (rs.next())
                System.out.println("  PostgreSQL version: " + rs.getString(1));

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // ── 3. HikariCP Pool ──────────────────────────────────────────────
        System.out.println("\n── 3. HikariCP Connection Pool ───────");
        try (Connection conn = ConexionPool.obtener()) {
            System.out.println("  Conexión del pool obtenida ✓");
            System.out.println("  Auto-commit: " + conn.getAutoCommit());
            System.out.println("  Válida: " + conn.isValid(2));
            ConexionPool.estadisticas();
        } catch (SQLException e) {
            System.err.println("Error pool: " + e.getMessage());
        } finally {
            ConexionPool.cerrar();
        }

        // ── 4. Contar registros de la tabla ───────────────────────────────
        System.out.println("\n── 4. Tablas y conteo ────────────────");
        String[] tablas = {"estudiantes", "materias", "inscripciones"};
        try (Connection conn = Conexion.obtener()) {
            for (String tabla : tablas) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs   = stmt.executeQuery("SELECT COUNT(*) FROM " + tabla)) {
                    if (rs.next())
                        System.out.printf("  %-20s → %d filas%n", tabla, rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("\nEjecuta los demás ejemplos en orden del orden.txt");
    }
}
