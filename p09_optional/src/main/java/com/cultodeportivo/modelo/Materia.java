package com.cultodeportivo.modelo;

import java.util.Optional;

public class Materia {

    private String nombre;
    private double nota;
    private String descripcion;   // puede ser null
    private Docente docente;      // puede ser null

    public Materia(String nombre, double nota) {
        this.nombre = nombre;
        this.nota   = nota;
    }

    public String getNombre()   { return nombre; }
    public double getNota()     { return nota; }

    // Retornar Optional en el getter es la forma idiomática cuando el valor puede faltar
    public Optional<String>  getDescripcion() { return Optional.ofNullable(descripcion); }
    public Optional<Docente> getDocente()     { return Optional.ofNullable(docente); }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setDocente(Docente docente)         { this.docente = docente; }

    @Override
    public String toString() { return nombre + " (nota: " + nota + ")"; }
}
