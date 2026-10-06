package com.cultodeportivo.p06_clases_abstractas.validador;

public class NumeroValidador extends Validador{

    protected String mensaje = "el campo %s debe ser un número";

    @Override
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String getMensaje() {
        return mensaje;
    }

    @Override
    public boolean esValido(String valor) {
        try {
            Integer.valueOf(valor);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
