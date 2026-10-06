package com.cultodeportivo.decorator;

// Componente concreto: el texto base sin ningún formato.
public class TextoSimple implements Formateador {

    private final String texto;

    public TextoSimple(String texto) { this.texto = texto; }

    @Override
    public String formatear() { return texto; }
}
