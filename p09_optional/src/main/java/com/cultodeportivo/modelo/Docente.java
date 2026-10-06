package com.cultodeportivo.modelo;

import java.util.Optional;

public class Docente {

    private String nombre;
    private String departamento;  // puede ser null

    public Docente(String nombre) { this.nombre = nombre; }

    public String getNombre() { return nombre; }

    public Optional<String> getDepartamento() { return Optional.ofNullable(departamento); }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    @Override
    public String toString() { return nombre; }
}
