package com.cultodeportivo.optional;

import com.cultodeportivo.modelo.Materia;
import com.cultodeportivo.repositorio.MateriaRepositorio;
import com.cultodeportivo.repositorio.Repositorio;

// orElse(T)            → devuelve T si vacío; el argumento SIEMPRE se evalúa.
// orElseGet(Supplier)  → devuelve T si vacío; el Supplier se evalúa SOLO si vacío (lazy).
// orElseThrow()        → lanza NoSuchElementException si vacío (Java 10+).
// orElseThrow(Supplier)→ lanza la excepción provista si vacío.
//
// Diferencia clave orElse vs orElseGet:
//   Si crear el valor por defecto es costoso (consulta DB, objeto pesado),
//   siempre usar orElseGet para evitar ejecutarlo cuando no hace falta.
public class EjemploOrElse {
    public static void main(String[] args) {

        Repositorio<Materia> repo = new MateriaRepositorio();

        // --- orElse: valor por defecto literal ---
        Materia m1 = repo.buscarPorNombre("algoritmos").orElse(new Materia("Materia no encontrada", 0));
        System.out.println("orElse (encontrada):     " + m1);

        Materia m2 = repo.buscarPorNombre("fisica").orElse(new Materia("Materia no encontrada", 0));
        System.out.println("orElse (no encontrada):  " + m2);

        System.out.println("---");

        // --- orElseGet: Supplier evaluado solo si vacío ---
        // Aquí valorDefecto() solo se llama cuando no se encuentra
        Materia m3 = repo.buscarPorNombre("redes").orElseGet(EjemploOrElse::materiaDefecto);
        System.out.println("orElseGet (encontrada):  " + m3);

        Materia m4 = repo.buscarPorNombre("quimica").orElseGet(EjemploOrElse::materiaDefecto);
        System.out.println("orElseGet (no encontrada): " + m4);

        System.out.println("---");

        // --- orElseThrow: tirar excepción si no existe ---
        try {
            Materia m5 = repo.buscarPorNombre("ingles").orElseThrow();
            System.out.println("orElseThrow (encontrada): " + m5);

            // Esta debe lanzar excepción
            Materia m6 = repo.buscarPorNombre("filosofia")
                    .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada en el pensum de Felipe"));
            System.out.println(m6); // no llega aquí
        } catch (IllegalArgumentException e) {
            System.out.println("Excepción capturada: " + e.getMessage());
        }
    }

    public static Materia materiaDefecto() {
        System.out.println("  [creando materia por defecto...]");
        return new Materia("Sin asignar", 0.0);
    }
}
