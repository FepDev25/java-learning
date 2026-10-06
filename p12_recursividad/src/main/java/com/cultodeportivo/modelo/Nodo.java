package com.cultodeportivo.modelo;

import java.util.ArrayList;
import java.util.List;

// Nodo de árbol genérico (patrón Composite).
// Cada nodo puede ser hoja (sin hijos) o rama (con hijos).
// addHijo retorna this → permite encadenar: nodo.addHijo(a).addHijo(b)
public class Nodo {

    private String nombre;
    private List<Nodo> hijos;
    private int nivel;              // calculado por la recursión, no en el constructor

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.hijos  = new ArrayList<>();
    }

    public Nodo addHijo(Nodo hijo) {
        this.hijos.add(hijo);
        return this;
    }

    public boolean tieneHijos()    { return !hijos.isEmpty(); }
    public String  getNombre()     { return nombre; }
    public List<Nodo> getHijos()   { return hijos; }
    public int     getNivel()      { return nivel; }
    public void    setNivel(int n) { this.nivel = n; }
}
