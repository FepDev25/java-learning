package com.cultodeportivo.modelo;

import com.cultodeportivo.anotaciones.Init;
import com.cultodeportivo.anotaciones.JsonAtributo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

// Modelo que usa las anotaciones personalizadas.
// @JsonAtributo → el campo será incluido en el JSON con la clave indicada.
// @Init         → el método se invocará automáticamente antes de serializar.
// Los campos SIN @JsonAtributo (fecha, pais) no aparecerán en el JSON resultante.
public class Estudiante {

    @JsonAtributo(capitalizar = true)           // clave = "nombre" (nombre del campo), aplica TitleCase
    private String nombre;

    @JsonAtributo(nombre = "edad_años")         // clave personalizada en el JSON
    private int edad;

    @JsonAtributo(nombre = "carrera", capitalizar = true)
    private String carrera;

    @JsonAtributo(nombre = "promedio_gpa")
    private double promedio;

    // Sin @JsonAtributo → no aparecerá en el JSON
    private LocalDate fechaNacimiento;
    private String pais;

    public Estudiante() {}

    public Estudiante(String nombre, int edad, String carrera, double promedio,
                      LocalDate fechaNacimiento, String pais) {
        this.nombre          = nombre;
        this.edad            = edad;
        this.carrera         = carrera;
        this.promedio        = promedio;
        this.fechaNacimiento = fechaNacimiento;
        this.pais            = pais;
    }

    // @Init: el procesador llama este método antes de serializar.
    // Normaliza el nombre y carrera a TitleCase si capitalizar = true no fue suficiente.
    @Init
    private void normalizar() {
        if (nombre != null) {
            this.nombre = Arrays.stream(nombre.split(" "))
                    .map(p -> p.substring(0, 1).toUpperCase() + p.substring(1).toLowerCase())
                    .collect(Collectors.joining(" "));
        }
    }

    public String getNombre()          { return nombre; }
    public int    getEdad()            { return edad; }
    public String getCarrera()         { return carrera; }
    public double getPromedio()        { return promedio; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getPais()            { return pais; }

    public void setNombre(String nombre)   { this.nombre = nombre; }
    public void setEdad(int edad)          { this.edad = edad; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public void setPromedio(double promedio) { this.promedio = promedio; }

    @Override
    public String toString() { return nombre + " | " + carrera + " | GPA: " + promedio; }
}
