package com.cultodeportivo.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Retention(RUNTIME) → la anotación se conserva en bytecode y es visible vía Reflection.
//   SOURCE  → solo en código fuente, descartada por el compilador (@Override, @SuppressWarnings)
//   CLASS   → en bytecode, pero no en runtime (default)
//   RUNTIME → en bytecode Y en runtime → necesario para leerla con Reflection
//
// @Target(METHOD) → solo se puede aplicar sobre métodos.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Init {
    // Sin atributos: anotación de marcado (marker annotation)
    // Su presencia significa "este método debe ejecutarse al inicializar el objeto"
}
