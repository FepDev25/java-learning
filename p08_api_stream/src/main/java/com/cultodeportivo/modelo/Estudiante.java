package com.cultodeportivo.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Estudiante {

    private String nombre;
    private String apellido;
    private int    edad;
    private String pais;
    private List<Curso> cursos;

    public Estudiante(String nombre, String apellido, int edad, String pais) {
        this.nombre   = nombre;
        this.apellido = apellido;
        this.edad     = edad;
        this.pais     = pais;
        this.cursos   = new ArrayList<>();
    }

    public void addCurso(Curso c) { this.cursos.add(c); }

    public String getNombre()    { return nombre; }
    public String getApellido()  { return apellido; }
    public int    getEdad()      { return edad; }
    public String getPais()      { return pais; }
    public List<Curso> getCursos() { return cursos; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPromedio() {
        return cursos.stream()
                .mapToDouble(Curso::getNota)
                .average()
                .orElse(0.0);
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " [" + pais + ", " + edad + " años]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante e = (Estudiante) o;
        return Objects.equals(nombre, e.nombre) && Objects.equals(apellido, e.apellido);
    }

    @Override
    public int hashCode() { return Objects.hash(nombre, apellido); }
}
