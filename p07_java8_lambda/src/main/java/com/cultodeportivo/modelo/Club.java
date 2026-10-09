package com.cultodeportivo.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Club {
    private String nombre;
    private LocalDate fundacion;
    private List<String> jugadores;
    private String Ciudad;

    public Club(){
        this.jugadores = new ArrayList<>();
    }

    public Club(String nombre){
        this();
        this.nombre = nombre;
    }

    public Club(String nombre, LocalDate fundacion, List<String> jugadores, String ciudad) {
        this.nombre = nombre;
        this.fundacion = fundacion;
        this.jugadores = jugadores;
        Ciudad = ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFundacion() {
        return fundacion;
    }

    public void setFundacion(LocalDate fundacion) {
        this.fundacion = fundacion;
    }

    public List<String> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<String> jugadores) {
        this.jugadores = jugadores;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String presentarJugadores(){
        return String.join(", ", jugadores);
    }
}
