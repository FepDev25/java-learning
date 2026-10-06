package com.cultodeportivo.decorator.decorador;

import com.cultodeportivo.decorator.Formateador;
import com.cultodeportivo.decorator.TextoDecorador;

// Añade un prefijo y sufijo — útil para cabeceras de apuntes
public class CorcheteDecorador extends TextoDecorador {

    private final String prefijo;
    private final String sufijo;

    public CorcheteDecorador(Formateador texto, String prefijo, String sufijo) {
        super(texto);
        this.prefijo = prefijo;
        this.sufijo  = sufijo;
    }

    @Override
    public String formatear() { return prefijo + texto.formatear() + sufijo; }
}
