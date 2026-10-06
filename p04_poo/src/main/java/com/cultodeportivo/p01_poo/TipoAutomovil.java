package com.cultodeportivo.p01_poo;

public enum TipoAutomovil {

    // Definicion de las constantes de la enumeracion TipoAutomovil
    SEDAN("Sedan", "Auto mediano", 4),
    STATION_WAGON("Station Wagon", "Auto grande", 5),
    HATCHBACK("Hatchback", "Auto compacto", 5),
    PICKUP("Pickup", "Camioneta", 4),
    COUPE("Coupé", "Auto pequeño", 2),
    CONVERTIBLE("Convertible", "Auto deportivo", 2),
    FURGON("Furgón", "Auto utilitario", 3),
    SUV("SUV", "Todo terreno deportivo", 5);

    // Atributos de la enumeracion TipoAutomovil
    private final String nombre;
    private final int numeroPuerta;
    private final String descripcion;

    // Constructor de la enumeracion TipoAutomovil
    TipoAutomovil(String nombre, String descripcion, int numeroPuerta) {
        this.nombre = nombre;
        this.numeroPuerta = numeroPuerta;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumeroPuerta() {
        return numeroPuerta;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
