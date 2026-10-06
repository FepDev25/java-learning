package com.cultodeportivo.p02_paquetes.jardin;

import com.cultodeportivo.p02_paquetes.hogar.Persona;

public class Perro {

    protected String nombre;
    protected String raza;

    String jugar(Persona persona){
        return persona.lanzarPelota();
    }
}
