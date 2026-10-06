package com.cultodeportivo.p01_poo;

public enum TipoMotor {
    GASOLINA("Gasolina"),
    DIESEL("Diesel"),
    ELECTRICO("Eléctrico"),
    HIBRIDO("Híbrido");

    private final String tipo; // Atributo para almacenar el tipo de motor

    TipoMotor(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return this.tipo;
    }
}
