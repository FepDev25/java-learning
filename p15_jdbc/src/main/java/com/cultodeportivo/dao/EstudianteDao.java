package com.cultodeportivo.dao;

import com.cultodeportivo.modelo.Estudiante;

import java.util.List;
import java.util.Optional;

/**
 * DAO (Data Access Object) — interfaz que define el contrato de acceso a datos.
 *
 * Propósito: aislar la lógica de negocio del acceso a la base de datos.
 *   - La capa de negocio habla con EstudianteDao (abstracción)
 *   - La implementación concreta puede cambiar (JDBC, JPA, archivo, memoria) sin tocar el negocio
 *
 * Esto aplica DIP (Dependency Inversion Principle) del SOLID.
 */
public interface EstudianteDao {

    /** Persiste un estudiante nuevo. Devuelve el estudiante con id generado. */
    Estudiante guardar(Estudiante e);

    /** Busca por id. Devuelve Optional vacío si no existe. */
    Optional<Estudiante> buscarPorId(int id);

    /** Busca por nombre exacto (insensible a mayúsculas). */
    Optional<Estudiante> buscarPorNombre(String nombre);

    /** Lista todos los estudiantes. */
    List<Estudiante> listarTodos();

    /** Lista estudiantes con promedio >= minPromedio. */
    List<Estudiante> listarPorPromedioMinimo(double minPromedio);

    /** Actualiza nombre, carrera y promedio. Devuelve true si actualizó. */
    boolean actualizar(Estudiante e);

    /** Elimina por id. Devuelve true si eliminó. */
    boolean eliminar(int id);

    /** Cuenta el total de estudiantes activos. */
    int contarActivos();
}
