package com.cultodeportivo.p06_clases_abstractas.elementos;

public class InputForm extends ElementoForm { // extends ElementoForm para heredar sus propiedades y métodos

    private String tipo = "text";

    public InputForm(String nombre) {
        super(nombre); // Llama al constructor de la clase padre ElementoForm
    }

    public InputForm(String nombre, String tipo) {
        super(nombre); // Llama al constructor de la clase padre ElementoForm
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Implementación del método abstracto dibujarHtml() de la clase padre
    @Override
    public String dibujarHtml() {
        return "<input type=\"" + this.tipo
                + "\" name=\"" + this.nombre
                + "\" value=\"" + this.valor + "\">";
    }
}
