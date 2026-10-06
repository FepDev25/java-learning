package com.cultodeportivo.dao;

import com.cultodeportivo.config.ConexionPool;
import com.cultodeportivo.modelo.Estudiante;

import java.util.List;
import java.util.Optional;

/**
 * Demostración del patrón DAO completo.
 *
 * El cliente (este main) solo conoce EstudianteDao (la interfaz).
 * EstudianteDaoJdbc es la implementación concreta → sustituible por
 * una implementación JPA, en memoria (tests), o cualquier otra.
 */
public class EjemploDAO {

    public static void main(String[] args) {

        System.out.println("═══════════════════════════════════════");
        System.out.println(" JDBC — Patrón DAO con HikariCP");
        System.out.println("═══════════════════════════════════════");

        // Depende de la interfaz, no de la implementación (DIP)
        EstudianteDao dao = new EstudianteDaoJdbc();

        try {
            // ── guardar ───────────────────────────────────────────────────
            System.out.println("\n── guardar() ─────────────────────────");
            Estudiante felipe = new Estudiante("Felipe", 21, "Ecuador",
                    "Ciencias de la Computación", 9.20);
            dao.guardar(felipe);
            System.out.println("  Guardado con id=" + felipe.getId() + ": " + felipe);

            Estudiante sofia = dao.guardar(
                    new Estudiante("Sofía_DAO", 22, "Ecuador", "Ingeniería de Software", 8.75));
            System.out.println("  Guardado con id=" + sofia.getId() + ": " + sofia);

            // ── buscarPorId ───────────────────────────────────────────────
            System.out.println("\n── buscarPorId() ─────────────────────");
            Optional<Estudiante> encontrado = dao.buscarPorId(felipe.getId());
            encontrado.ifPresentOrElse(
                e  -> System.out.println("  Encontrado: " + e),
                () -> System.out.println("  No encontrado")
            );

            // buscar id inexistente
            dao.buscarPorId(9999).ifPresentOrElse(
                e  -> System.out.println("  Encontrado: " + e),
                () -> System.out.println("  id 9999 → Optional vacío ✓")
            );

            // ── buscarPorNombre ───────────────────────────────────────────
            System.out.println("\n── buscarPorNombre() ─────────────────");
            dao.buscarPorNombre("FELIPE")  // insensible a mayúsculas
               .ifPresent(e -> System.out.println("  Felipe (UPPERCASE) → " + e.getNombre()));

            // ── listarTodos ───────────────────────────────────────────────
            System.out.println("\n── listarTodos() ─────────────────────");
            List<Estudiante> todos = dao.listarTodos();
            todos.forEach(e -> System.out.printf(
                "  [%d] %-15s | %-35s | %.2f%n",
                e.getId(), e.getNombre(), e.getCarrera(), e.getPromedio()));
            System.out.println("  Total: " + todos.size());

            // ── listarPorPromedioMinimo ────────────────────────────────────
            System.out.println("\n── listarPorPromedioMinimo(9.0) ──────");
            dao.listarPorPromedioMinimo(9.0)
               .forEach(e -> System.out.printf("  %-15s → %.2f%n",
                       e.getNombre(), e.getPromedio()));

            // ── actualizar ────────────────────────────────────────────────
            System.out.println("\n── actualizar() ──────────────────────");
            felipe.setPromedio(9.9);
            felipe.setCarrera("Ingeniería en Computación");
            boolean actualizado = dao.actualizar(felipe);
            System.out.println("  Actualizado: " + actualizado);
            dao.buscarPorId(felipe.getId())
               .ifPresent(e -> System.out.println("  Ahora: " + e));

            // ── contarActivos ─────────────────────────────────────────────
            System.out.println("\n── contarActivos() ───────────────────");
            System.out.println("  Estudiantes activos: " + dao.contarActivos());

            // ── eliminar ──────────────────────────────────────────────────
            System.out.println("\n── eliminar() ────────────────────────");
            boolean eliminado = dao.eliminar(sofia.getId());
            System.out.println("  Eliminado Sofía_DAO: " + eliminado);
            System.out.println("  Existe aún: " + dao.buscarPorId(sofia.getId()).isPresent());

            // ── Limpiar ───────────────────────────────────────────────────
            dao.eliminar(felipe.getId());
            System.out.println("\nLimpieza de registros de prueba ✓");

        } finally {
            ConexionPool.cerrar();
        }
    }
}
