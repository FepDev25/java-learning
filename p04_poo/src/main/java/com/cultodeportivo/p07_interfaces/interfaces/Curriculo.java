package com.cultodeportivo.p07_interfaces.interfaces;

import java.util.ArrayList;
import java.util.List;

// Clase Curriculo que extiende Hoja e implementa Imprimible
public class Curriculo extends Hoja implements Imprimible {

    private Persona persona;
    private String carrera;
    private List<String> experiencias;

    public Curriculo(Persona persona, String carrera, String contenido) {
        super(contenido);
        this.persona = persona;
        this.carrera = carrera;
        this.experiencias = new ArrayList<>();
    }

    public Curriculo addExperiencia(String exp){
        experiencias.add(exp);
        return this;
    }

    // Al implementar la interfaz Imprimible, debemos definir el método imprimir()
    @Override
    public String imprimir() {
        StringBuilder sb = new StringBuilder("Nombre: ");
        sb.append(persona).append("\n")
                .append("Resumen: ").append(this.contenido)
                .append("\n").append("Profesión: ")
                .append(this.carrera).append("\n")
                .append("Experiencias: \n");
        for(String exp: this.experiencias){
            sb.append("- ").append(exp).append("\n");
        }
        return sb.toString();
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public List<String> getExperiencias() {
        return experiencias;
    }

    public void setExperiencias(List<String> experiencias) {
        this.experiencias = experiencias;
    }

    
}
