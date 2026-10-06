# P11 - Anotaciones y Reflection

## Descripción General

Estudio de las anotaciones en Java: anotaciones integradas del JDK, definición de
anotaciones personalizadas y procesamiento en tiempo de ejecución mediante Reflection
(ejemplo: un serializador JSON propio).

## Información del Proyecto

- **Artifact ID:** p11_anotaciones
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `anotaciones` — Anotaciones

- `EjemploAnotacionesJDK.java` - `@Override`, `@Deprecated`, `@SuppressWarnings`,
  `@FunctionalInterface`.
- `JsonAtributo.java` - Anotación personalizada con atributos
  (`@Documented`, `@Target(FIELD)`, `@Retention(RUNTIME)`).
- `Init.java` - Anotación marcadora (`@Target(METHOD)`, `@Retention(RUNTIME)`).
- `EjemploAnotacion.java` - Demo del procesamiento de anotaciones.

### 2. `procesador` — Procesamiento con Reflection

- `JsonSerializador.java` - Lee anotaciones en runtime:
  1. invoca métodos `@Init` para inicializar el objeto;
  2. serializa a JSON solo los campos `@JsonAtributo`.
- `AnotacionException.java` - `RuntimeException` para errores de procesamiento.

### 3. `modelo`

- `Estudiante.java` - Modelo anotado; los campos sin `@JsonAtributo` se omiten del JSON.

## Conceptos Clave Aprendidos

- Las anotaciones son **metadatos**: instrucciones para el compilador, la JVM o herramientas.
- `@Retention` controla dónde vive la anotación:
  - `SOURCE`: solo en el código fuente.
  - `CLASS`: en el bytecode, no en runtime (por defecto).
  - `RUNTIME`: disponible en runtime, leíble con Reflection.
- `@Target` restringe dónde se puede aplicar (`FIELD`, `METHOD`, `TYPE`, etc.).
- `@Documented` la incluye en el Javadoc.
- Las anotaciones con atributos parecen métodos, pero son valores (con `default` opcional).
- **Reflection** permite inspeccionar clases, campos y métodos, e invocarlos en runtime.

## Ejecución de Ejemplos

```bash
cd p11_anotaciones
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.anotaciones.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.anotaciones.EjemploAnotacion"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.anotaciones.EjemploAnotacionesJDK"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── anotaciones/
├── modelo/
└── procesador/
```

## Notas Técnicas

- La Reflection es potente pero más lenta que el acceso directo; úsala con criterio.
- Para que una anotación sea leíble en runtime debe tener `@Retention(RUNTIME)`.
- Este módulo muestra cómo funcionan por dentro frameworks como Jackson o JPA.

## Referencias

- [Annotations - Oracle](https://docs.oracle.com/javase/tutorial/java/annotations/)
- [Reflection - Oracle](https://docs.oracle.com/javase/tutorial/reflect/)
