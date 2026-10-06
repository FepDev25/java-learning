# P14 - Manejo de Archivos

## Descripción General

Estudio del manejo de archivos en Java: la API clásica `java.io` (bytes y caracteres)
y la API moderna **NIO.2** (`java.nio.file`), además de `java.util.Properties`
y la serialización de objetos.

## Información del Proyecto

- **Artifact ID:** p14_manejo_archivos
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `archivos` — Archivos y rutas

- `EjemploFile.java` - `java.io.File`: crear, inspeccionar y manipular archivos/directorios.
- `EjemploEscritura.java` - Escribir con `FileWriter`/`BufferedWriter`, `PrintWriter` y `Files.writeString`.
- `EjemploLectura.java` - Leer con `BufferedReader`, `Scanner`, `Files.readAllLines`,
  `Files.readString` y `Files.lines`.
- `EjemploBytesStream.java` - Streams de bytes (`FileInputStream`/`FileOutputStream`) para binarios.
- `EjemploNIO.java` - NIO.2 (`Path`, `Files`): copiar, mover, opciones.
- `EjemploNIOWalk.java` - `Files.walk` y `Files.find` para recorrer árboles de directorios.

### 2. `properties` — Configuración

- `EjemploProperties.java` - `java.util.Properties`: escribir, leer y el patrón
  de carga desde `src/main/resources` con `getResourceAsStream`.

### 3. `serializacion` — Serialización

- `EjemploSerializacion.java` - `ObjectOutputStream` / `ObjectInputStream` para
  escribir y leer objetos.

### 4. `modelo`

- `Estudiante.java` - Implementa `Serializable`; usa `serialVersionUID` y `transient`.

## Conceptos Clave Aprendidos

- **`try-with-resources`:** cierra automáticamente los streams (`AutoCloseable`).
  Sin cerrar, los datos en buffer pueden no llegar al disco.
- **Streams de bytes vs caracteres:** bytes para binarios; caracteres para texto.
- **NIO.2 (`Path`/`Files`):** API moderna, rutas multiplataforma, errores con excepciones
  detalladas y operaciones atómicas.
- **`java.util.Properties`:** pares clave=valor (configuración, i18n).
- **Serialización:** convertir objetos a bytes y viceversa; `transient` excluye campos;
  `serialVersionUID` controla la compatibilidad de versiones.
- **`Files.walk` / `Files.find`:** recorrido de directorios mediante streams.

## Ejecución de Ejemplos

```bash
cd p14_manejo_archivos
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<paquete>.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.archivos.EjemploNIO"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.serializacion.EjemploSerializacion"
```

> Los ejemplos escriben y leen en el directorio temporal del sistema
> (`System.getProperty("java.io.tmpdir")`), por lo que no dependen de rutas fijas.

## Estructura de Paquetes

```text
com.cultodeportivo
├── archivos/
├── modelo/
├── properties/
└── serializacion/
```

## Notas Técnicas

- Preferir NIO.2 (`Files`) sobre `java.io.File` en código nuevo.
- `Files.readString` / `writeString` (Java 11+) simplifican leer/escribir texto.
- `Properties` también se puede cargar desde el classpath con
  `getClass().getResourceAsStream("/config.properties")`.
- Las alternativas modernas a la serialización Java son JSON (Jackson/Gson) y Protocol Buffers.

## Referencias

- [Basic I/O - Oracle](https://docs.oracle.com/javase/tutorial/essential/io/)
- [java.nio.file](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/file/package-summary.html)
