package com.cultodeportivo.p08_genericos.interfaces_repositorio_genericos_y_excepciones.modelo;

public class Producto extends BaseEntity {

    private String descripcion;
    private Double precio;

    public Producto(String descripcion, double precio) {
        super();
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "descripción='" + descripcion + '\''
                + ", precio=" + precio
                + ", id=" + id;
    }
}
