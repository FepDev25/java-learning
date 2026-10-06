package com.cultodeportivo.p01_poo;

// Una clase es una plantilla o molde a partir del cual se crean objetos.
// Define atributos (propiedades) y metodos (comportamientos) que los objetos creados a partir de la clase tendran.

public class Rueda {

    // Atributos de la clase Rueda
    private String fabricante;
    private int aro;
    private double ancho;

    // Constructor de la clase Rueda
    public Rueda(String fabricante, int aro, double ancho) {
        this.fabricante = fabricante;
        this.aro = aro;
        this.ancho = ancho;
    }

    // Metodos getters y setters para acceder y modificar los atributos
    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public int getAro() {
        return aro;
    }

    public void setAro(int aro) {
        this.aro = aro;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    // Metodo para mostrar la informacion de la rueda
    @Override
    public String toString() {
        return "Rueda{" +
                "fabricante='" + fabricante + '\'' +
                ", aro=" + aro +
                ", ancho=" + ancho +
                '}';
    }
}

