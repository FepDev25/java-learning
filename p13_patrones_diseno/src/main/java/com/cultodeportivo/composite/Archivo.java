package com.cultodeportivo.composite;

// Hoja (Leaf): no tiene hijos. mostrar() retorna solo su nombre indentado.
public class Archivo extends Componente {

    public Archivo(String nombre) { super(nombre); }

    @Override
    public String mostrar(int nivel) {
        return "  ".repeat(nivel) + nombre;
    }

    @Override
    public boolean buscar(String nombre) {
        return this.nombre.equalsIgnoreCase(nombre);
    }
}
