package com.cultodeportivo.p01_poo.pruebas;

import com.cultodeportivo.p01_poo.Automovil;
import com.cultodeportivo.p01_poo.Color;
import com.cultodeportivo.p01_poo.Estanque;
import com.cultodeportivo.p01_poo.Motor;
import com.cultodeportivo.p01_poo.Persona;
import com.cultodeportivo.p01_poo.Rueda;
import com.cultodeportivo.p01_poo.TipoAutomovil;
import com.cultodeportivo.p01_poo.TipoMotor;

public class EjemploAutomovilRelacionesObjetos {
    public static void main(String[] args) {

        // Ejemplo 1: Construcción básica con setters
        Automovil auto1 = new Automovil("Toyota", "Corolla");
        auto1.setMotor(new Motor(1.8, TipoMotor.GASOLINA));
        auto1.setEstanque(new Estanque(45));
        auto1.setColor(Color.BLANCO);
        auto1.setTipo(TipoAutomovil.SEDAN);
        auto1.setConductor(new Persona("Juan", "Pérez"));
        
        // Agregar ruedas con un ciclo
        for(int i = 0; i < 5; i++){
            auto1.addRueda(new Rueda("Bridgestone", 16, 7.0));
        }

        // Ejemplo 2: Construcción con constructor parcial
        Automovil auto2 = new Automovil("Honda", "Civic", Color.ROJO, 
                new Motor(2.0, TipoMotor.GASOLINA));
        auto2.setTipo(TipoAutomovil.SEDAN);
        auto2.setEstanque(new Estanque(50));
        auto2.setConductor(new Persona("María", "García"));
        
        // Agregar ruedas de forma encadenada
        auto2.addRueda(new Rueda("Michelin", 17, 7.5))
             .addRueda(new Rueda("Michelin", 17, 7.5))
             .addRueda(new Rueda("Michelin", 17, 7.5))
             .addRueda(new Rueda("Michelin", 17, 7.5))
             .addRueda(new Rueda("Michelin", 17, 7.5));

        // Ejemplo 3: Construcción con array de ruedas predefinido
        Rueda[] ruedasAuto3 = {
            new Rueda("Pirelli", 18, 8.0),
            new Rueda("Pirelli", 18, 8.0),
            new Rueda("Pirelli", 18, 8.0),
            new Rueda("Pirelli", 18, 8.0),
            new Rueda("Pirelli", 18, 8.0)
        };
        
        Automovil auto3 = new Automovil("Ford", "Ranger", Color.AZUL,
                new Motor(3.2, TipoMotor.DIESEL), 
                new Estanque(80),
                new Persona("Carlos", "López"),
                ruedasAuto3);
        auto3.setTipo(TipoAutomovil.PICKUP);

        // Configuración global
        Automovil.setColorPatente(Color.VERDE);

        // Mostrar detalles de cada automóvil
        System.out.println("\n=== Detalles de los automóviles ===");
        System.out.println(auto1.verDetalle());
        System.out.println("\n" + auto2.verDetalle());
        System.out.println("\n" + auto3.verDetalle());
    }
}
