package com.cultodeportivo.optional;

import com.cultodeportivo.modelo.Materia;
import com.cultodeportivo.repositorio.MateriaRepositorio;
import com.cultodeportivo.repositorio.Repositorio;

// map(Function)    → transforma el valor si presente; devuelve Optional<R>.
// filter(Predicate)→ mantiene el valor solo si cumple la condición.
// flatMap(Function)→ como map pero la función ya devuelve Optional<R> (evita Optional<Optional<R>>).
//
// Caso de uso clásico: navegar cadenas de Optional anidados
// sin null-checks ni try/catch → "Optional chaining".
public class EjemploMapFilter {
    public static void main(String[] args) {

        Repositorio<Materia> repo = new MateriaRepositorio();

        // --- map: transformar el valor ---
        // String → Materia → nombre en mayúsculas
        String nombreMayus = repo.buscarPorNombre("redes")
                .map(m -> m.getNombre().toUpperCase())
                .orElse("no encontrada");
        System.out.println("map (nombre upper): " + nombreMayus);

        // --- filter: mantener solo si la nota es suficiente ---
        repo.buscarPorNombre("algoritmos")
                .filter(m -> m.getNota() >= 7.0)
                .ifPresentOrElse(
                        m  -> System.out.println("Materia aprobada: " + m),
                        () -> System.out.println("Materia reprobada o no encontrada")
                );

        // filter que descarta (nota < 7.0 hipotético)
        repo.buscarPorNombre("cálculo")
                .filter(m -> m.getNota() >= 10.0)   // nadie saca 10, filtro excluye
                .ifPresentOrElse(
                        m  -> System.out.println("Nota perfecta: " + m),
                        () -> System.out.println("No tiene nota perfecta (filter vacío)")
                );

        System.out.println("---");

        // --- flatMap: navegar Optional anidados sin Optional<Optional<X>> ---
        // Algoritmos → Optional<Docente> → Optional<Departamento>
        // Si usáramos map aquí obtendríamos Optional<Optional<String>>, que es inútil
        String departamento = repo.buscarPorNombre("algoritmos")
                .flatMap(Materia::getDocente)             // Optional<Materia> → Optional<Docente>
                .flatMap(com.cultodeportivo.modelo.Docente::getDepartamento)  // Optional<Docente> → Optional<String>
                .map(String::toUpperCase)
                .orElse("Departamento no asignado");
        System.out.println("Departamento (Algoritmos): " + departamento);

        // Redes → tiene docente pero sin departamento → orElse actúa
        String depRedes = repo.buscarPorNombre("redes")
                .flatMap(Materia::getDocente)
                .flatMap(com.cultodeportivo.modelo.Docente::getDepartamento)
                .orElse("Departamento no asignado");
        System.out.println("Departamento (Redes):      " + depRedes);

        // Inglés → sin docente → cadena cortada en el primer flatMap
        String depIngles = repo.buscarPorNombre("inglés")
                .flatMap(Materia::getDocente)
                .flatMap(com.cultodeportivo.modelo.Docente::getDepartamento)
                .orElse("Sin docente asignado");
        System.out.println("Departamento (Inglés):     " + depIngles);

        System.out.println("---");

        // --- Extraer descripción de un archivo (sin repositorio) ---
        String archivo = "tesis_felipe_ecuador.pdf";
        String extension = java.util.Optional.ofNullable(archivo)
                .filter(a -> a.contains("."))
                .map(a -> a.substring(a.lastIndexOf('.') + 1).toUpperCase())
                .orElse("SIN EXTENSIÓN");
        System.out.println("Extensión de archivo: " + extension);
    }
}
