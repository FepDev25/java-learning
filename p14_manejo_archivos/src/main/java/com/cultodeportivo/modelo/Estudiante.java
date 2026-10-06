package com.cultodeportivo.modelo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo para demostrar serialización.
 * Implementa Serializable para poder escribirse/leerse con ObjectOutputStream/ObjectInputStream.
 *
 * serialVersionUID — identificador de versión de la clase.
 *   Si cambia la clase pero no el UID → Java acepta el objeto deserializado (puede fallar con campos nuevos).
 *   Si no se declara → Java lo genera automáticamente; si cambia la clase → InvalidClassException.
 *   Buena práctica: declararlo siempre explícitamente.
 *
 * transient — campos que NO se serializan (contraseñas, caches, conexiones).
 */
public class Estudiante implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String nombre;
    private final int    edad;
    private final String pais;
    private final String carrera;
    private final List<String> materias;

    // transient: no se guarda en disco (información sensible o recalculable)
    private transient String token;
    private transient double promedioCacheado;

    public Estudiante(String nombre, int edad, String pais, String carrera) {
        this.nombre   = nombre;
        this.edad     = edad;
        this.pais     = pais;
        this.carrera  = carrera;
        this.materias = new ArrayList<>();
        this.token    = "TOKEN_" + nombre.hashCode();
    }

    public Estudiante agregarMateria(String materia) {
        materias.add(materia);
        return this;
    }

    public String getNombre()    { return nombre; }
    public int    getEdad()      { return edad; }
    public String getPais()      { return pais; }
    public String getCarrera()   { return carrera; }
    public List<String> getMaterias() { return materias; }
    public String getToken()     { return token; }

    @Override
    public String toString() {
        return "Estudiante{nombre='" + nombre + "', edad=" + edad +
               ", pais='" + pais + "', carrera='" + carrera + "'" +
               ", materias=" + materias +
               ", token='" + token + "'}";
    }
}
