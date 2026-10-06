package com.cultodeportivo.decorator;

// Componente: interfaz que el objeto base y todos los decoradores implementan.
// Clave: los decoradores tienen la misma interfaz → se pueden anidar indefinidamente.
@FunctionalInterface
public interface Formateador {
    String formatear();
}
