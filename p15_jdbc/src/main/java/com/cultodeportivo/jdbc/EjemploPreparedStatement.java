package com.cultodeportivo.jdbc;

import com.cultodeportivo.config.Conexion;

import java.sql.*;
import java.util.List;

/**
 * PreparedStatement — la forma CORRECTA de ejecutar SQL con parámetros.
 *
 * Ventajas sobre Statement:
 *  1. SEGURIDAD: previene SQL Injection (los parámetros nunca se interpretan como SQL)
 *  2. RENDIMIENTO: la consulta se compila UNA vez en el servidor → reutilizable
 *  3. LEGIBILIDAD: parámetros bien definidos con ?
 *
 * SQL Injection (ejemplo de ataque con Statement):
 *   WHERE nombre = 'Felipe' OR '1'='1'   ← devuelve TODOS los registros
 *   PreparedStatement escapa esto automáticamente.
 *
 * Marcadores: ? (posicionales, 1-based)
 *   setString(1, valor)  / setInt(2, valor) / setDouble(3, valor) ...
 */
public class EjemploPreparedStatement {

    public static void main(String[] args) throws SQLException {

        System.out.println("═══════════════════════════════════════");
        System.out.println(" JDBC — PreparedStatement");
        System.out.println("═══════════════════════════════════════");

        try (Connection conn = Conexion.obtener()) {

            // ── 1. INSERT con PreparedStatement ───────────────────────────
            System.out.println("\n── 1. INSERT parametrizado ───────────");
            String sql = """
                INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio)
                VALUES (?, ?, ?, ?, ?)
                """;

            // Lista de datos a insertar
            List<Object[]> nuevos = List.of(
                new Object[]{"Ana",    20, "Ecuador", "Ciencias de la Computación", 9.3},
                new Object[]{"Pablo",  22, "Ecuador", "Ingeniería Civil",            7.5},
                new Object[]{"Camila", 21, "Ecuador", "Matemáticas",                 9.8}
            );

            try (PreparedStatement ps = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {

                for (Object[] datos : nuevos) {
                    ps.setString(1, (String) datos[0]);
                    ps.setInt   (2, (int)    datos[1]);
                    ps.setString(3, (String) datos[2]);
                    ps.setString(4, (String) datos[3]);
                    ps.setDouble(5, (double) datos[4]);
                    ps.executeUpdate();

                    // Obtener ID generado
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next())
                            System.out.printf("  Insertado: %-8s → id=%d%n",
                                    datos[0], keys.getInt(1));
                    }
                }
            }

            // ── 2. SELECT con parámetro ───────────────────────────────────
            System.out.println("\n── 2. SELECT con parámetro ───────────");
            String selectSql = "SELECT id, nombre, promedio FROM estudiantes WHERE carrera = ? ORDER BY promedio DESC";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, "Ciencias de la Computación");
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("  Estudiantes de Ciencias de la Computación:");
                    while (rs.next())
                        System.out.printf("    [%d] %-10s → %.2f%n",
                                rs.getInt("id"), rs.getString("nombre"), rs.getDouble("promedio"));
                }
            }

            // ── 3. Reutilización del PreparedStatement ─────────────────────
            System.out.println("\n── 3. Reutilización (mismo PS, distintos params) ──");
            String buscarSql = "SELECT nombre, promedio FROM estudiantes WHERE promedio BETWEEN ? AND ?";
            try (PreparedStatement ps = conn.prepareStatement(buscarSql)) {

                double[][] rangos = {{7.0, 8.0}, {8.0, 9.0}, {9.0, 10.0}};
                for (double[] rango : rangos) {
                    ps.setDouble(1, rango[0]);
                    ps.setDouble(2, rango[1]);
                    try (ResultSet rs = ps.executeQuery()) {
                        System.out.printf("  Rango [%.1f - %.1f]: ", rango[0], rango[1]);
                        StringBuilder sb = new StringBuilder();
                        while (rs.next())
                            sb.append(rs.getString("nombre")).append("(").append(
                                    String.format("%.1f", rs.getDouble("promedio"))).append(") ");
                        System.out.println(sb.isEmpty() ? "ninguno" : sb.toString().trim());
                    }
                }
            }

            // ── 4. UPDATE con PreparedStatement ───────────────────────────
            System.out.println("\n── 4. UPDATE parametrizado ───────────");
            String updateSql = "UPDATE estudiantes SET promedio = ? WHERE nombre = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setDouble(1, 9.9);
                ps.setString(2, "Camila");
                int rows = ps.executeUpdate();
                System.out.println("  Filas actualizadas: " + rows);
            }

            // ── 5. DELETE y limpieza ──────────────────────────────────────
            System.out.println("\n── 5. DELETE y limpieza ──────────────");
            String delSql = "DELETE FROM estudiantes WHERE nombre = ANY(?)";
            try (PreparedStatement ps = conn.prepareStatement(delSql)) {
                // PostgreSQL: pasar array con setArray
                Array arr = conn.createArrayOf("varchar",
                        new String[]{"Ana", "Pablo", "Camila"});
                ps.setArray(1, arr);
                int rows = ps.executeUpdate();
                System.out.println("  Filas eliminadas: " + rows);
                arr.free();
            }

            System.out.println("\nPreparedStatement: siempre preferirlo sobre Statement.");
        }
    }
}
