package com.cultodeportivo.p01_poo;

public enum Color {

    // Una enumeracion es un tipo de dato especial que permite definir un conjunto de constantes con nombre.
    // Cada constante de la enumeracion es una instancia de la propia enumeracion.

    // Definicion de las constantes de la enumeracion Color
    VERDE("Verde"),
    ROJO("Rojo"),
    AMARILLO("Amarillo"),
    AZUL("Azul"),
    BLANCO("Blanco"),
    GRIS("Gris Oscuro"),
    NARANJO("Naranja");

    private final String color; // Atributo para almacenar el nombre del color

    Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return this.color;
    }
}
