package com.cultodeportivo.modelo;

import java.time.LocalDateTime;

/**
 * POJO que mapea la tabla 'estudiantes'.
 * Sin frameworks (no JPA): el mapeo se hace manualmente en el DAO.
 */
public class Estudiante {

    private int           id;
    private String        nombre;
    private int           edad;
    private String        pais;
    private String        carrera;
    private double        promedio;
    private boolean       activo;
    private LocalDateTime creadoEn;

    public Estudiante() {}

    public Estudiante(String nombre, int edad, String pais, String carrera, double promedio) {
        this.nombre   = nombre;
        this.edad     = edad;
        this.pais     = pais;
        this.carrera  = carrera;
        this.promedio = promedio;
        this.activo   = true;
    }

    // Getters & Setters
    public int           getId()        { return id; }
    public void          setId(int id)  { this.id = id; }

    public String        getNombre()               { return nombre; }
    public void          setNombre(String nombre)  { this.nombre = nombre; }

    public int           getEdad()              { return edad; }
    public void          setEdad(int edad)      { this.edad = edad; }

    public String        getPais()              { return pais; }
    public void          setPais(String pais)   { this.pais = pais; }

    public String        getCarrera()                 { return carrera; }
    public void          setCarrera(String carrera)   { this.carrera = carrera; }

    public double        getPromedio()                  { return promedio; }
    public void          setPromedio(double promedio)   { this.promedio = promedio; }

    public boolean       isActivo()                { return activo; }
    public void          setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getCreadoEn()                      { return creadoEn; }
    public void          setCreadoEn(LocalDateTime creadoEn){ this.creadoEn = creadoEn; }

    @Override
    public String toString() {
        return "Estudiante{id=%d, nombre='%s', edad=%d, pais='%s', carrera='%s', promedio=%.2f, activo=%b}"
                .formatted(id, nombre, edad, pais, carrera, promedio, activo);
    }
}
