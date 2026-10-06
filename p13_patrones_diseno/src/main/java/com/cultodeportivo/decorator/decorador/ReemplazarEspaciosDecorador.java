package com.cultodeportivo.decorator.decorador;

import com.cultodeportivo.decorator.Formateador;
import com.cultodeportivo.decorator.TextoDecorador;

public class ReemplazarEspaciosDecorador extends TextoDecorador {

    private final String reemplazo;

    public ReemplazarEspaciosDecorador(Formateador texto, String reemplazo) {
        super(texto);
        this.reemplazo = reemplazo;
    }

    @Override
    public String formatear() { return texto.formatear().replace(" ", reemplazo); }
}
