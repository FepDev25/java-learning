package com.cultodeportivo.p08_genericos.clases_genericas;

public class EjemploGenericos {
    public static <T> void imprimirCamion(Camion<T> camion){
        for(T a: camion){
            switch (a) {
                case Animal animal -> System.out.println(animal.getNombre() + " tipo: " + animal.getTipo());
                case Automovil automovil -> System.out.println(automovil.getMarca());
                case Maquinaria maquinaria -> System.out.println(maquinaria.getTipo());
                default -> {
                }
            }
        }
    }
    public static void main(String[] args) {

        Camion<Animal> transporteCaballos = new Camion<>(5);
        transporteCaballos.add(new Animal("Peregrino", "Caballo"));
        transporteCaballos.add(new Animal("grillo", "Caballo"));
        transporteCaballos.add(new Animal("Tunquen", "Caballo"));
        transporteCaballos.add(new Animal("Topocalma", "Caballo"));
        transporteCaballos.add(new Animal("Longotoma", "Caballo"));
        System.out.println("Transporte de caballos:");
        imprimirCamion(transporteCaballos);

        Camion<Maquinaria> transMaquinas = new Camion<>(3);
        transMaquinas.add(new Maquinaria("Bulldozer"));
        transMaquinas.add(new Maquinaria("Grúa Horquilla"));
        transMaquinas.add(new Maquinaria("Perforadora"));
        System.out.println("\nTransporte de maquinarias:");
        imprimirCamion(transMaquinas);

        Camion<Automovil> transAuto = new Camion<>(3);
        transAuto.add(new Automovil("Toyota"));
        transAuto.add(new Automovil("Mitsubishi"));
        transAuto.add(new Automovil("Chevrolet"));
        System.out.println("\nTransporte de automóviles:");
        imprimirCamion(transAuto);
    }
}
