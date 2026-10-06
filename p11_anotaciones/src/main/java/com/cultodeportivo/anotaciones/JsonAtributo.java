package com.cultodeportivo.anotaciones;

import java.lang.annotation.*;

// Anotación con atributos → instrucción para el serializador JSON.
// @Documented → aparece en el Javadoc generado de las clases que la usen.
// @Target(FIELD) → solo en campos (atributos de instancia).
// @Retention(RUNTIME) → leída por Reflection en JsonSerializador.
//
// Atributos de una anotación: parecen métodos pero son valores.
// default "" → valor por defecto si no se especifica al usar la anotación.
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAtributo {
    String nombre()       default "";      // nombre de clave JSON; vacío = usar nombre del campo
    boolean capitalizar() default false;   // convertir a TitleCase antes de serializar
}
