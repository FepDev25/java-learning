package com.cultodeportivo.p07_interfaces.interfaces;

public class Informe extends Hoja implements Imprimible{

    private Persona autor;
    private Persona revisor;

    public Informe(Persona autor, Persona revisor, String contenido) {
        super(contenido);
        this.autor = autor;
        this.revisor = revisor;
    }

    // Al implementar la interfaz Imprimible, debemos definir el método imprimir()
    @Override
    public String imprimir() {
        return "Informe escrito por: " + autor
                + " Revisado por: " + revisor
                + "\n" + this.contenido;
    }

    public Persona getAutor() {
        return autor;
    }

    public void setAutor(Persona autor) {
        this.autor = autor;
    }

    public Persona getRevisor() {
        return revisor;
    }

    public void setRevisor(Persona revisor) {
        this.revisor = revisor;
    }

    
}
