package com.cultodeportivo.p07_interfaces.interfaces;

// Clase abstracta Hoja
abstract public class Hoja {

    protected String contenido;

    public Hoja(String contenido) {
        this.contenido = contenido;
    }

    abstract public String imprimir();
}
