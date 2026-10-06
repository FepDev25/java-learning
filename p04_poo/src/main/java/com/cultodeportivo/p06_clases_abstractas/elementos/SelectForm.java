package com.cultodeportivo.p06_clases_abstractas.elementos;

import java.util.ArrayList;
import java.util.List;

import com.cultodeportivo.p06_clases_abstractas.elementos.select.Opcion;

public class SelectForm extends ElementoForm {

    private List<Opcion> opciones;

    public SelectForm(String nombre) {
        super(nombre);
        this.opciones = new ArrayList<>();
    }

    public SelectForm(String nombre, List<Opcion> opciones) {
        super(nombre);
        this.opciones = opciones;
    }

    public SelectForm addOpcion(Opcion opcion){
        this.opciones.add(opcion);
        return this;
    }

    // Implementación del método abstracto dibujarHtml() de la clase padre
    @Override
    public String dibujarHtml() {
        StringBuilder sb =  new StringBuilder("<select ");
        sb.append("name='")
                .append(this.nombre)
                .append("'>");

        for(Opcion opcion: this.opciones){
            sb.append("\n<option value='")
                    .append(opcion.getValor())
                    .append("'");
            if(opcion.isSelected()){
                sb.append(" selected");
                this.valor = opcion.getValor();
            }
            sb.append(">")
                    .append(opcion.getNombre())
                    .append("</option>");
        }
        sb.append("</select>");
        return sb.toString();
    }

    public List<Opcion> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<Opcion> opciones) {
        this.opciones = opciones;
    }

}
