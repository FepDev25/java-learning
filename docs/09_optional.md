# P09 - Optional

## Descripción General

Estudio de `java.util.Optional`: un contenedor que puede tener o no un valor, pensado
para eliminar los `NullPointerException` y hacer explícita la ausencia de valor.

## Información del Proyecto

- **Artifact ID:** p09_optional
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `optional` — API de Optional

- `EjemploOptional.java` - Creación (`of`, `ofNullable`, `empty`), `isPresent`,
  `ifPresent`, `ifPresentOrElse` y buenas prácticas.
- `EjemploOrElse.java` - Diferencia entre `orElse` (siempre evalúa),
  `orElseGet` (lazy) y `orElseThrow`.
- `EjemploMapFilter.java` - `map`, `filter`, `flatMap` y encadenamiento
  ("Optional chaining") para navegar estructuras anidadas.

### 2. `repositorio`

- `Repositorio.java` - Interfaz genérica de búsqueda.
- `MateriaRepositorio.java` - Repositorio en memoria que devuelve `Optional<Materia>`.

### 3. `modelo`

- `Materia.java` - Modelo con getters que pueden devolver `Optional`.
- `Docente.java` - Docente asociado (puede faltar).

## Conceptos Clave Aprendidos

- `Optional<T>` evita `null` explícito y obliga a contemplar la ausencia de valor.
- **Nunca** llamar `get()` sin verificar `isPresent()`.
- Preferir métodos funcionales: `ifPresent`, `map`, `filter`, `orElse`, `orElseGet`.
- `map` transforma el valor; `filter` lo mantiene según una condición.
- `flatMap` evita `Optional<Optional<T>>` cuando la función ya devuelve `Optional`.
- `orElse` siempre evalúa su argumento; `orElseGet` es perezoso (mejor si es costoso).
- Devolver `Optional` en getters es idiomático cuando el valor puede faltar.

## Ejecución de Ejemplos

```bash
cd p09_optional
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.optional.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.optional.EjemploOptional"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.optional.EjemploMapFilter"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── modelo/
├── optional/
└── repositorio/
```

## Notas Técnicas

- Usar `Optional` principalmente como tipo de retorno, no como campo.
- No usar `Optional` para colecciones: una colección vacía ya expresa ausencia.
- `Optional.empty()` es un singleton; comparar con `isEmpty()` / `isPresent()`.

## Referencias

- [Optional - Oracle](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Optional.html)
