package com.cultodeportivo.p07_interfaces.interfaces;

// Interfaz Imprimible
public interface Imprimible {

    // Una interfaz es una especie de contrato que obliga a las clases que la implementan a definir ciertos métodos.
    // En este caso, cualquier clase que implemente Imprimible debe proporcionar 
    // una implementación para el método imprimir().

    String TEXTO_DEFECTO = "Imprimiendo un valor por defecto"; // Constante de interfaz

    // Método abstracto que debe ser implementado por las clases que implementen esta interfaz
    default String imprimir(){
        return TEXTO_DEFECTO;
    }

    // Método estático que puede ser llamado sin necesidad de una instancia
    static void imprimir(Imprimible imprimible){
        System.out.println(imprimible.imprimir());
    }
}
