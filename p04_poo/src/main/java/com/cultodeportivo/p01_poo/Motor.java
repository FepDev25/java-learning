package com.cultodeportivo.p01_poo;

public class Motor {
    private double cilindrada;
    private TipoMotor tipo; // Tipo de motor utilizando la enumeracion TipoMotor

    // Sobrecarga de constructores: Se pueden definir multiples constructores con diferentes parametros.

    // Constructor vacio
    public Motor() {
    }

    // Constructor con parametros
    public Motor(double cilindrada, TipoMotor tipo) {
        this.cilindrada = cilindrada;
        this.tipo = tipo;
    }

    public double getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(double cilindrada) {
        this.cilindrada = cilindrada;
    }

    public TipoMotor getTipo() {
        return tipo;
    }

    public void setTipo(TipoMotor tipo) {
        this.tipo = tipo;
    }
}

