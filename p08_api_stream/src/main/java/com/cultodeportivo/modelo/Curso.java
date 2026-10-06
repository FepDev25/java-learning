package com.cultodeportivo.modelo;

public class Curso {

    private String nombre;
    private int    creditos;
    private double nota;

    public Curso(String nombre, int creditos, double nota) {
        this.nombre   = nombre;
        this.creditos = creditos;
        this.nota     = nota;
    }

    public String getNombre()   { return nombre; }
    public int    getCreditos() { return creditos; }
    public double getNota()     { return nota; }

    @Override
    public String toString() {
        return nombre + " (" + creditos + " cred, nota: " + nota + ")";
    }
}
