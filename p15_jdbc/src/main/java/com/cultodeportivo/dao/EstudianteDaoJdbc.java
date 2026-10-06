package com.cultodeportivo.dao;

import com.cultodeportivo.config.ConexionPool;
import com.cultodeportivo.modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de EstudianteDao.
 *
 * Patrones aplicados:
 *  - DAO: lógica SQL encapsulada aquí, invisible para el resto
 *  - mapearFila(): método privado que evita repetir el mapeo ResultSet → Estudiante
 *  - HikariCP: conexiones del pool (no DriverManager directo)
 *  - PreparedStatement: siempre, nunca Statement con concatenación
 *  - try-with-resources: cierra conexión/statement/resultset automáticamente
 */
public class EstudianteDaoJdbc implements EstudianteDao {

    @Override
    public Estudiante guardar(Estudiante e) {
        String sql = """
            INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (Connection conn = ConexionPool.obtener();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getNombre());
            ps.setInt   (2, e.getEdad());
            ps.setString(3, e.getPais());
            ps.setString(4, e.getCarrera());
            ps.setDouble(5, e.getPromedio());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getInt(1));
            }
            return e;

        } catch (SQLException ex) {
            throw new RuntimeException("Error al guardar estudiante: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Estudiante> buscarPorId(int id) {
        String sql = "SELECT * FROM estudiantes WHERE id = ?";
        try (Connection conn = ConexionPool.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearFila(rs)) : Optional.empty();
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar por id: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Estudiante> buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM estudiantes WHERE LOWER(nombre) = LOWER(?)";
        try (Connection conn = ConexionPool.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearFila(rs)) : Optional.empty();
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar por nombre: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Estudiante> listarTodos() {
        String sql = "SELECT * FROM estudiantes ORDER BY id";
        try (Connection conn = ConexionPool.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Estudiante> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapearFila(rs));
            return lista;

        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Estudiante> listarPorPromedioMinimo(double minPromedio) {
        String sql = "SELECT * FROM estudiantes WHERE promedio >= ? ORDER BY promedio DESC";
        try (Connection conn = ConexionPool.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, minPromedio);
            try (ResultSet rs = ps.executeQuery()) {
                List<Estudiante> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapearFila(rs));
                return lista;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar por promedio: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean actualizar(Estudiante e) {
        String sql = "UPDATE estudiantes SET nombre=?, carrera=?, promedio=? WHERE id=?";
        try (Connection conn = ConexionPool.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCarrera());
            ps.setDouble(3, e.getPromedio());
            ps.setInt   (4, e.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";
        try (Connection conn = ConexionPool.obtener();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar: " + ex.getMessage(), ex);
        }
    }

    @Override
    public int contarActivos() {
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE activo = TRUE";
        try (Connection conn = ConexionPool.obtener();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException ex) {
            throw new RuntimeException("Error al contar: " + ex.getMessage(), ex);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /** Mapea la fila actual del ResultSet a un Estudiante. */
    private Estudiante mapearFila(ResultSet rs) throws SQLException {
        Estudiante e = new Estudiante();
        e.setId      (rs.getInt      ("id"));
        e.setNombre  (rs.getString   ("nombre"));
        e.setEdad    (rs.getInt      ("edad"));
        e.setPais    (rs.getString   ("pais"));
        e.setCarrera (rs.getString   ("carrera"));
        e.setPromedio(rs.getDouble   ("promedio"));
        e.setActivo  (rs.getBoolean  ("activo"));
        Timestamp ts = rs.getTimestamp("creado_en");
        if (ts != null) e.setCreadoEn(ts.toLocalDateTime());
        return e;
    }
}
