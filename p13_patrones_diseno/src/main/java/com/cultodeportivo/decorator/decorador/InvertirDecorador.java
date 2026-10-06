package com.cultodeportivo.decorator.decorador;

import com.cultodeportivo.decorator.Formateador;
import com.cultodeportivo.decorator.TextoDecorador;

public class InvertirDecorador extends TextoDecorador {
    public InvertirDecorador(Formateador texto) { super(texto); }

    @Override
    public String formatear() {
        return new StringBuilder(texto.formatear()).reverse().toString();
    }
}
