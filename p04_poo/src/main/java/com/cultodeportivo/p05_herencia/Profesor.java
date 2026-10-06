package com.cultodeportivo.p05_herencia;

public class Profesor extends Persona {

    // Profesor va a heredar de Persona sus atributos y métodos

    private String asignatura;

    public Profesor() {
        System.out.println("Profesor: inicializando constructor...");
    }

    public Profesor(String nombre, String apellido) {
        super(nombre, apellido); // Llamada al constructor de la clase padre Persona
    }

    public Profesor(String nombre, String apellido, String asignatura) {
        super(nombre, apellido);
        this.asignatura = asignatura;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    @Override
    public String saludar() {
        return "Buenos días soy el profesor de "
                + getAsignatura() + ", mi nombre es "
                + getNombre();
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nasignatura='" + asignatura;
    }
}
