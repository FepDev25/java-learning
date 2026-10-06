package com.cultodeportivo.p06_clases_abstractas.validador;

// Clase abstracta que define el contrato para los validadores
abstract public class Validador {
    abstract public void setMensaje(String mensaje);
    abstract public String getMensaje();
    abstract public boolean esValido(String valor);
}
