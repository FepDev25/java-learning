package com.cultodeportivo.jdbc;

import com.cultodeportivo.config.Conexion;
import com.cultodeportivo.modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD básico con Statement y ResultSet.
 *
 * Statement — ejecuta SQL directamente como String.
 * NUNCA concatenar input de usuario en Statement → SQL Injection.
 * Para datos variables, usar PreparedStatement (ver EjemploPreparedStatement).
 *
 * ResultSet — cursor sobre las filas del resultado.
 *   rs.next()        → avanza al siguiente; true si hay fila
 *   rs.getString(n)  → por índice (1-based) o rs.getString("columna")
 *   rs.getInt / getDouble / getBoolean / getTimestamp ...
 */
public class EjemploCRUD {

    public static void main(String[] args) throws SQLException {

        System.out.println("═══════════════════════════════════════");
        System.out.println(" JDBC — CRUD con Statement");
        System.out.println("═══════════════════════════════════════");

        try (Connection conn = Conexion.obtener()) {

            // ── CREATE ─────────────────────────────────────────────────────
            System.out.println("\n── INSERT ────────────────────────────");
            // NOTA: en producción NO concatenar strings — esto solo es para
            // demostrar Statement puro. Ver PreparedStatement para la forma correcta.
            String insert = """
                INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio)
                VALUES ('Carlos', 20, 'Ecuador', 'Ciencias de la Computación', 8.50)
                """;
            int filas = conn.createStatement().executeUpdate(insert);
            System.out.println("  Filas insertadas: " + filas);

            // INSERT con RETURNING para obtener el ID generado
            String insertRet = """
                INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio)
                VALUES ('Laura', 21, 'Ecuador', 'Ingeniería', 9.00)
                RETURNING id
                """;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs   = stmt.executeQuery(insertRet)) {
                if (rs.next())
                    System.out.println("  ID generado (RETURNING): " + rs.getInt("id"));
            }

            // ── READ — SELECT todos ────────────────────────────────────────
            System.out.println("\n── SELECT todos ──────────────────────");
            List<Estudiante> todos = selectTodos(conn);
            todos.forEach(e -> System.out.println("  " + e));

            // ── READ — SELECT con WHERE ────────────────────────────────────
            System.out.println("\n── SELECT con WHERE (promedio >= 9.0) ──");
            String query = "SELECT * FROM estudiantes WHERE promedio >= 9.0 ORDER BY promedio DESC";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs   = stmt.executeQuery(query)) {
                while (rs.next()) {
                    System.out.printf("  %-12s | promedio: %.2f%n",
                            rs.getString("nombre"), rs.getDouble("promedio"));
                }
            }

            // ── READ — Metadata del ResultSet ─────────────────────────────
            System.out.println("\n── ResultSetMetaData ─────────────────");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs   = stmt.executeQuery("SELECT * FROM estudiantes LIMIT 1")) {
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                System.out.println("  Columnas en 'estudiantes': " + cols);
                for (int i = 1; i <= cols; i++)
                    System.out.printf("    %d. %-20s (%s)%n",
                            i, meta.getColumnName(i), meta.getColumnTypeName(i));
            }

            // ── UPDATE ─────────────────────────────────────────────────────
            System.out.println("\n── UPDATE ────────────────────────────");
            String update = "UPDATE estudiantes SET promedio = 8.80 WHERE nombre = 'Carlos'";
            int actualizados = conn.createStatement().executeUpdate(update);
            System.out.println("  Filas actualizadas: " + actualizados);

            // ── DELETE ─────────────────────────────────────────────────────
            System.out.println("\n── DELETE ────────────────────────────");
            String delete = "DELETE FROM estudiantes WHERE nombre IN ('Carlos', 'Laura')";
            int eliminados = conn.createStatement().executeUpdate(delete);
            System.out.println("  Filas eliminadas: " + eliminados);

            // ── Verificar estado final ─────────────────────────────────────
            System.out.println("\n── Estado final ──────────────────────");
            selectTodos(conn).forEach(e ->
                System.out.printf("  %-12s | carrera: %-35s | promedio: %.2f%n",
                        e.getNombre(), e.getCarrera(), e.getPromedio()));
        }
    }

    static List<Estudiante> selectTodos(Connection conn) throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(
                     "SELECT * FROM estudiantes ORDER BY id")) {
            while (rs.next()) {
                Estudiante e = new Estudiante();
                e.setId      (rs.getInt      ("id"));
                e.setNombre  (rs.getString   ("nombre"));
                e.setEdad    (rs.getInt      ("edad"));
                e.setPais    (rs.getString   ("pais"));
                e.setCarrera (rs.getString   ("carrera"));
                e.setPromedio(rs.getDouble   ("promedio"));
                e.setActivo  (rs.getBoolean  ("activo"));
                lista.add(e);
            }
        }
        return lista;
    }
}
