package com.cultodeportivo.jdbc;

import com.cultodeportivo.config.Conexion;

import java.sql.*;

/**
 * Transacciones JDBC — garantizan ACID.
 *
 * ACID:
 *  A — Atomicidad: todo o nada
 *  C — Consistencia: los datos cumplen las constraints siempre
 *  I — Aislamiento: transacciones concurrentes no se interfieren
 *  D — Durabilidad: commit persiste aunque caiga el servidor
 *
 * Por defecto JDBC tiene auto-commit = true (cada Statement es su propia transacción).
 * Para transacciones manuales: conn.setAutoCommit(false) → commit() o rollback().
 *
 * Savepoints — puntos intermedios para rollback parcial.
 */
public class EjemploTransacciones {

    public static void main(String[] args) {

        System.out.println("═══════════════════════════════════════");
        System.out.println(" JDBC — Transacciones");
        System.out.println("═══════════════════════════════════════");

        // ── 1. Transacción exitosa ────────────────────────────────────────
        System.out.println("\n── 1. Transacción exitosa (commit) ───");
        try (Connection conn = Conexion.obtener()) {
            conn.setAutoCommit(false);   // inicio de la transacción

            try (PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) VALUES (?,?,?,?,?)")) {

                ins.setString(1, "Transact_A");
                ins.setInt   (2, 20);
                ins.setString(3, "Ecuador");
                ins.setString(4, "CS");
                ins.setDouble(5, 8.5);
                ins.executeUpdate();

                ins.setString(1, "Transact_B");
                ins.setInt   (2, 21);
                ins.setDouble(5, 9.0);
                ins.executeUpdate();

                conn.commit();
                System.out.println("  commit() — ambas filas persistidas ✓");
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("  rollback() por error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error conexión: " + e.getMessage());
        }

        // ── 2. Transacción con error → rollback ───────────────────────────
        System.out.println("\n── 2. Transacción fallida (rollback) ─");
        try (Connection conn = Conexion.obtener()) {
            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement()) {
                // Primera operación OK
                stmt.executeUpdate(
                    "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) " +
                    "VALUES ('Rollback_A', 22, 'Ecuador', 'CS', 8.0)");
                System.out.println("  INSERT 1: OK");

                // Segunda operación FALLA (edad fuera del CHECK 15-99)
                stmt.executeUpdate(
                    "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) " +
                    "VALUES ('Rollback_B', 200, 'Ecuador', 'CS', 8.0)");
                System.out.println("  INSERT 2: OK");  // No llega aquí

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();  // deshace también el INSERT 1
                System.out.println("  rollback() → INSERT 1 también deshecho");
                System.out.println("  Causa: " + e.getMessage().split("\n")[0]);
            }

            // Verificar que Rollback_A no existe
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery(
                         "SELECT COUNT(*) FROM estudiantes WHERE nombre LIKE 'Rollback%'")) {
                rs.next();
                System.out.println("  Filas 'Rollback%' en BD: " + rs.getInt(1) + " (esperado 0)");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // ── 3. Savepoints ─────────────────────────────────────────────────
        System.out.println("\n── 3. Savepoints ─────────────────────");
        try (Connection conn = Conexion.obtener()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) VALUES (?,?,?,?,?)")) {

                // Op 1 — commit parcial
                ps.setString(1, "Save_A"); ps.setInt(2,20); ps.setString(3,"Ecuador");
                ps.setString(4,"CS"); ps.setDouble(5, 9.0); ps.executeUpdate();
                Savepoint sp1 = conn.setSavepoint("sp_after_A");
                System.out.println("  Savepoint sp_after_A creado");

                // Op 2 — se va a deshacer
                ps.setString(1, "Save_B"); ps.setInt(2,21); ps.setDouble(5, 7.0);
                ps.executeUpdate();
                System.out.println("  Save_B insertado (se deshará)");

                // Rollback solo hasta el savepoint (Save_A persiste)
                conn.rollback(sp1);
                System.out.println("  rollback(sp_after_A) → Save_B deshecho, Save_A sigue");

                conn.commit();

                // Verificar
                try (Statement s = conn.createStatement();
                     ResultSet rs = s.executeQuery(
                             "SELECT nombre FROM estudiantes WHERE nombre LIKE 'Save%' ORDER BY nombre")) {
                    System.out.print("  En BD: ");
                    while (rs.next()) System.out.print(rs.getString(1) + " ");
                    System.out.println();
                }

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error conexión: " + e.getMessage());
        }

        // ── Limpieza ──────────────────────────────────────────────────────
        try (Connection conn = Conexion.obtener();
             Statement  stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM estudiantes WHERE nombre LIKE 'Transact_%' OR nombre LIKE 'Save_%'");
            System.out.println("\nLimpieza de registros de prueba ✓");
        } catch (SQLException e) {
            System.err.println("Error limpieza: " + e.getMessage());
        }
    }
}
