package com.cultodeportivo.decorator;

// Decorador abstracto: envuelve cualquier Formateador y delega a él.
// Al ser abstract, cada subclase aplica su transformación sobre texto.formatear().
public abstract class TextoDecorador implements Formateador {

    protected final Formateador texto;  // puede ser TextoSimple u otro decorador

    public TextoDecorador(Formateador texto) { this.texto = texto; }
}
