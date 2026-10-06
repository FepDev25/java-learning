package com.cultodeportivo.composite;

import java.util.ArrayList;
import java.util.List;

// Rama (Composite): puede contener Archivos u otros Directorios.
// mostrar() y buscar() delegan recursivamente a sus hijos → Composite Pattern.
public class Directorio extends Componente {

    private final List<Componente> hijos = new ArrayList<>();

    public Directorio(String nombre) { super(nombre); }

    public Directorio add(Componente c) { hijos.add(c); return this; }  // fluent
    public void remove(Componente c)   { hijos.remove(c); }
    public List<Componente> getHijos() { return hijos; }

    @Override
    public String mostrar(int nivel) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ".repeat(nivel)).append(nombre).append("/\n");
        for (Componente hijo : hijos) {
            sb.append(hijo.mostrar(nivel + 1));
            if (hijo instanceof Archivo) sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean buscar(String nombre) {
        if (this.nombre.equalsIgnoreCase(nombre)) return true;
        // Delega a cada hijo recursivamente (anyMatch hace cortocircuito)
        return hijos.stream().anyMatch(h -> h.buscar(nombre));
    }
}
