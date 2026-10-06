package com.cultodeportivo.p06_clases_abstractas.elementos;

import java.util.ArrayList;
import java.util.List;

import com.cultodeportivo.p06_clases_abstractas.validador.Validador;
import com.cultodeportivo.p06_clases_abstractas.validador.mensaje.MensajeFormateable;

// Esta clase es abstracta porque no tiene sentido crear
// instancias genéricas de ElementoForm, solo de sus subclases.

abstract public class ElementoForm {

    protected String valor;
    protected String nombre;

    private List<Validador> validadores;
    private List<String> errores;

    public ElementoForm() {
        this.validadores = new ArrayList<>();
        this.errores = new ArrayList<>();
    }

    public ElementoForm(String nombre) {
        this();
        this.nombre = nombre;
    }

    public ElementoForm addValidador(Validador validador){
        this.validadores.add(validador);
        return this;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean esValido(){
        for(Validador v: validadores){
            if(!v.esValido(this.valor)){
                if(v instanceof MensajeFormateable mensajeFormateable) {
                    this.errores.add(mensajeFormateable.getMensajeFormateado(nombre));
                } else {
                    this.errores.add(String.format(v.getMensaje(), nombre));
                }
            }
        }
        return this.errores.isEmpty();
    }

    abstract public String dibujarHtml();
}
