package com.cultodeportivo.jdbc;

import com.cultodeportivo.config.Conexion;

import java.sql.*;
import java.util.Random;

/**
 * Batch operations — enviar múltiples sentencias SQL en un solo viaje de red.
 *
 * Sin batch: N inserts = N round-trips al servidor (lento).
 * Con batch:  N inserts = 1 round-trip → mucho más eficiente.
 *
 * PreparedStatement.addBatch()    → acumula el set de parámetros actual
 * PreparedStatement.executeBatch()→ envía todo junto al servidor
 *                                   devuelve int[] con filas afectadas por cada sentencia
 *
 * Buena práctica: hacer commit y limpiar el batch cada N filas
 * para no acumular demasiada memoria.
 */
public class EjemploBatch {

    static final int TOTAL   = 100;   // registros a insertar
    static final int LOTE    = 20;    // commit cada 20

    public static void main(String[] args) throws SQLException {

        System.out.println("═══════════════════════════════════════");
        System.out.println(" JDBC — Batch Operations");
        System.out.println("═══════════════════════════════════════");

        try (Connection conn = Conexion.obtener()) {
            conn.setAutoCommit(false);

            // ── 1. Benchmark: sin batch ───────────────────────────────────
            System.out.println("\n── 1. Sin batch (" + TOTAL + " inserts) ──────");
            long t0 = System.currentTimeMillis();
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) VALUES (?,?,?,?,?)")) {

                for (int i = 1; i <= TOTAL; i++) {
                    ps.setString(1, "NoBatch_" + i);
                    ps.setInt   (2, 18 + (i % 10));
                    ps.setString(3, "Ecuador");
                    ps.setString(4, "CS");
                    ps.setDouble(5, 7.0 + (i % 30) * 0.1);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            long sinBatch = System.currentTimeMillis() - t0;
            System.out.printf("  Tiempo: %d ms%n", sinBatch);

            // Limpiar
            try (Statement s = conn.createStatement()) {
                s.executeUpdate("DELETE FROM estudiantes WHERE nombre LIKE 'NoBatch_%'");
                conn.commit();
            }

            // ── 2. Con batch (sin cortes) ─────────────────────────────────
            System.out.println("\n── 2. Con batch completo ─────────────");
            t0 = System.currentTimeMillis();
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) VALUES (?,?,?,?,?)")) {

                for (int i = 1; i <= TOTAL; i++) {
                    ps.setString(1, "Batch_" + i);
                    ps.setInt   (2, 18 + (i % 10));
                    ps.setString(3, "Ecuador");
                    ps.setString(4, "CS");
                    ps.setDouble(5, 7.0 + (i % 30) * 0.1);
                    ps.addBatch();   // acumular, no ejecutar aún
                }

                int[] resultados = ps.executeBatch();   // un solo viaje al servidor
                conn.commit();
                System.out.printf("  Tiempo: %d ms — %d filas insertadas%n",
                        System.currentTimeMillis() - t0, resultados.length);

                // Verificar resultados del batch
                int totalAfectadas = 0;
                for (int r : resultados) totalAfectadas += r;
                System.out.println("  Total filas afectadas: " + totalAfectadas);
            } catch (BatchUpdateException e) {
                conn.rollback();
                System.err.println("BatchUpdateException: " + e.getMessage());
                int[] counts = e.getUpdateCounts();
                System.err.println("Falló en índice: " + counts.length);
            }

            // ── 3. Batch con commit por lotes (recomendado en producción) ─
            System.out.println("\n── 3. Batch con commit cada " + LOTE + " ────");
            t0 = System.currentTimeMillis();
            int totalInsertados = 0;
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) VALUES (?,?,?,?,?)")) {

                for (int i = 1; i <= TOTAL; i++) {
                    ps.setString(1, "BatchLote_" + i);
                    ps.setInt   (2, 18 + (i % 10));
                    ps.setString(3, "Ecuador");
                    ps.setString(4, "CS");
                    ps.setDouble(5, 7.0 + (i % 30) * 0.1);
                    ps.addBatch();

                    if (i % LOTE == 0) {
                        int[] r = ps.executeBatch();
                        conn.commit();
                        totalInsertados += r.length;
                        System.out.printf("  Lote %d/%d committed (%d filas)%n",
                                i / LOTE, TOTAL / LOTE, r.length);
                        ps.clearBatch();
                    }
                }
                // Residuo (si TOTAL no es divisible por LOTE)
                int[] r = ps.executeBatch();
                if (r.length > 0) { conn.commit(); totalInsertados += r.length; }
            }
            System.out.printf("  Total: %d en %d ms%n",
                    totalInsertados, System.currentTimeMillis() - t0);

            // ── 4. Speedup summary ────────────────────────────────────────
            System.out.printf("%n  Speedup batch vs sin-batch: %.1fx%n",
                    (double) sinBatch / (System.currentTimeMillis() - t0 + 1));

            // ── Limpieza ──────────────────────────────────────────────────
            try (Statement s = conn.createStatement()) {
                s.executeUpdate(
                    "DELETE FROM estudiantes WHERE nombre LIKE 'Batch_%' OR nombre LIKE 'BatchLote_%'");
                conn.commit();
                System.out.println("\nLimpieza de registros de prueba ✓");
            }
        }
    }
}
