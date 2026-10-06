package com.cultodeportivo.composite;

import java.util.Objects;

// Componente abstracto: interfaz común para hojas y ramas.
// El cliente siempre trabaja con Componente — no sabe si es Archivo o Directorio.
public abstract class Componente {

    protected String nombre;

    public Componente(String nombre) { this.nombre = nombre; }

    public String getNombre() { return nombre; }

    // mostrar: visualiza el árbol con indentación según el nivel
    public abstract String mostrar(int nivel);

    // buscar: recursión hacia abajo hasta encontrar el nombre
    public abstract boolean buscar(String nombre);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(nombre, ((Componente) o).nombre);
    }

    @Override
    public int hashCode() { return Objects.hash(nombre); }
}
