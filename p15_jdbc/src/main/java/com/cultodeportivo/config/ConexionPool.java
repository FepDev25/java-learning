package com.cultodeportivo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection pool con HikariCP — el pool de conexiones más usado en Java.
 *
 * ¿Por qué un pool?
 *  - Abrir/cerrar conexiones TCP es costoso (latencia, recursos OS)
 *  - El pool mantiene N conexiones abiertas y las reutiliza
 *  - Bajo carga: peticiones esperan en cola en vez de abrir conexiones nuevas
 *
 * HikariCP: ultra-rápido, configuración simple, se integra con Spring Boot.
 *
 * Uso como Singleton: una sola instancia de HikariDataSource por aplicación.
 */
public class ConexionPool {

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl ("jdbc:postgresql://localhost:5435/notas_db");
        config.setUsername("felipe");
        config.setPassword("ecuador2024");

        // Tamaño del pool
        config.setMaximumPoolSize   (10);   // máximo de conexiones activas
        config.setMinimumIdle       (2);    // conexiones en espera mínimas
        config.setConnectionTimeout (30_000); // ms esperando una conexión libre
        config.setIdleTimeout       (600_000);// ms antes de cerrar una idle
        config.setMaxLifetime       (1_800_000);// ms máximo de vida de una conexión

        // Nombre del pool (aparece en logs y JMX)
        config.setPoolName("HikariPool-JDBC");

        // Validar conexión al tomarla del pool
        config.setConnectionTestQuery("SELECT 1");

        // PostgreSQL: configuraciones de sesión
        config.addDataSourceProperty("ApplicationName", "p15_jdbc_felipe");

        dataSource = new HikariDataSource(config);
    }

    /** Obtiene una conexión del pool. Devolver con conn.close() o try-with-resources. */
    public static Connection obtener() throws SQLException {
        return dataSource.getConnection();
    }

    /** Cierra el pool completo (llamar al apagar la app). */
    public static void cerrar() {
        if (dataSource != null && !dataSource.isClosed())
            dataSource.close();
    }

    /** Muestra estadísticas del pool. */
    public static void estadisticas() {
        var pool = dataSource.getHikariPoolMXBean();
        System.out.println("== HikariCP Pool Stats ==");
        System.out.println("  Activas  : " + pool.getActiveConnections());
        System.out.println("  Idle     : " + pool.getIdleConnections());
        System.out.println("  En cola  : " + pool.getThreadsAwaitingConnection());
        System.out.println("  Total    : " + pool.getTotalConnections());
    }
}
