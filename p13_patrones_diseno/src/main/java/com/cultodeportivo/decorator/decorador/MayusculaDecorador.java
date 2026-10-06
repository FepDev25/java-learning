package com.cultodeportivo.decorator.decorador;

import com.cultodeportivo.decorator.Formateador;
import com.cultodeportivo.decorator.TextoDecorador;

public class MayusculaDecorador extends TextoDecorador {
    public MayusculaDecorador(Formateador texto) { super(texto); }

    @Override
    public String formatear() { return texto.formatear().toUpperCase(); }
}
