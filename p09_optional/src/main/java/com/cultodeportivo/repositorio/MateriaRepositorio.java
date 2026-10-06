package com.cultodeportivo.repositorio;

import com.cultodeportivo.modelo.Docente;
import com.cultodeportivo.modelo.Materia;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MateriaRepositorio implements Repositorio<Materia> {

    private final List<Materia> datos = new ArrayList<>();

    public MateriaRepositorio() {
        // Algoritmos: tiene docente con departamento
        Docente docAlgo = new Docente("Dr. Rivera");
        docAlgo.setDepartamento("Ciencias de la Computación");
        Materia algoritmos = new Materia("Algoritmos", 9.1);
        algoritmos.setDescripcion("Análisis y diseño de algoritmos eficientes");
        algoritmos.setDocente(docAlgo);
        datos.add(algoritmos);

        // Redes: tiene docente pero sin departamento asignado
        Materia redes = new Materia("Redes", 8.5);
        redes.setDocente(new Docente("Ing. Paredes"));
        datos.add(redes);

        // Inglés Técnico: sin docente ni descripción
        datos.add(new Materia("Inglés Técnico", 8.0));

        // Cálculo: solo descripción, sin docente
        Materia calculo = new Materia("Cálculo I", 7.8);
        calculo.setDescripcion("Límites, derivadas e integrales");
        datos.add(calculo);
    }

    @Override
    public Optional<Materia> buscarPorNombre(String nombre) {
        // Retorna Optional: el llamador decide qué hacer si no existe
        return datos.stream()
                .filter(m -> m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .findFirst();
    }
}
