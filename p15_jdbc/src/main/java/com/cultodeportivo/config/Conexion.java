package com.cultodeportivo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión JDBC básica con DriverManager.
 *
 * URL JDBC PostgreSQL:
 *   jdbc:postgresql://<host>:<port>/<database>
 *
 * DriverManager — fábrica de conexiones directas, sin pool.
 * Cada llamada a getConnection() abre una nueva conexión TCP al servidor.
 * Usar en ejemplos simples o CLI; para producción usar HikariCP (ver ConexionPool).
 */
public class Conexion {

    // Puerto 5435 → mapeado al 5432 del contenedor Docker
    static final String URL      = "jdbc:postgresql://localhost:5435/notas_db";
    static final String USUARIO  = "felipe";
    static final String PASSWORD = "ecuador2024";

    /**
     * Devuelve una conexión nueva.
     * El llamador debe cerrarla (try-with-resources recomendado).
     */
    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    /** Verifica que la conexión sea posible e imprime metadatos del servidor. */
    public static void verificar() {
        System.out.println("Conectando a: " + URL);
        try (Connection conn = obtener()) {
            var meta = conn.getMetaData();
            System.out.println("  Servidor   : " + meta.getDatabaseProductName()
                    + " " + meta.getDatabaseProductVersion());
            System.out.println("  Driver     : " + meta.getDriverName()
                    + " " + meta.getDriverVersion());
            System.out.println("  URL real   : " + meta.getURL());
            System.out.println("  Auto-commit: " + conn.getAutoCommit());
            System.out.println("  Conectado  ✓");
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Verifica que el contenedor esté corriendo:");
            System.err.println("  docker compose up -d");
        }
    }
}
