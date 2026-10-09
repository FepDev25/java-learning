package com.cultodeportivo.referenciametodos;

import com.cultodeportivo.modelo.Club;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class EjemploClub {
    public static void main(String[] args) {

        Function<String, Club> fabricarClubes = Club::new;
        Club club = fabricarClubes.apply("Felipe FC");
        club.setCiudad("Cuenca");
        club.setFundacion(LocalDate.of(2026, 1, 1));
        club.setJugadores(List.of("Hazard", "Messi", "Cristiano"));


        Function<Club, String> presentarJugadores = Club::presentarJugadores;
        String presentacion = presentarJugadores.apply(club);
        System.out.println(presentacion);

    }
}
