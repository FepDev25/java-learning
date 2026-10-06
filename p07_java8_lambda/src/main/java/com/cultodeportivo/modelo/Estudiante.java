package com.cultodeportivo.modelo;

public class Estudiante {

    private String nombre;
    private int edad;
    private String carrera;
    private double promedio;

    public Estudiante() {}

    public Estudiante(String nombre, int edad, String carrera, double promedio) {
        this.nombre   = nombre;
        this.edad     = edad;
        this.carrera  = carrera;
        this.promedio = promedio;
    }

    public String getNombre()   { return nombre; }
    public int    getEdad()     { return edad; }
    public String getCarrera()  { return carrera; }
    public double getPromedio() { return promedio; }

    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setEdad(int edad)            { this.edad = edad; }
    public void setCarrera(String carrera)   { this.carrera = carrera; }
    public void setPromedio(double promedio) { this.promedio = promedio; }

    @Override
    public String toString() {
        return nombre + " | " + carrera + " | edad: " + edad + " | promedio: " + promedio;
    }
}
