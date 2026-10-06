# P08 - Java 8: API Stream

## Descripción General

Estudio de la API `Stream` de Java 8 para procesar colecciones de forma declarativa:
creación de streams, operaciones intermedias y terminales, `Collectors`, `reduce`,
`flatMap`, estadísticas y procesamiento paralelo.

## Información del Proyecto

- **Artifact ID:** p08_api_stream
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `stream` — Operaciones sobre streams

- `EjemploCrearStream.java` - Formas de crear streams (`Stream.of`, `Arrays.stream`, `Stream.builder`).
- `EjemploStreamMap.java` - `map()` y `peek()` (intermedias), `collect()` (terminal).
- `EjemploStreamFilter.java` - `filter()` y terminales `count`, `findFirst`, `findAny`, `anyMatch`, `allMatch`, `noneMatch`.
- `EjemploStreamReduce.java` - `reduce()` en sus variantes.
- `EjemploStreamFlatMap.java` - Aplanar estructuras anidadas (`map` vs `flatMap`).
- `EjemploStreamDistinct.java` - Eliminación de duplicados con `equals`/`hashCode`.
- `EjemploStreamSortedLimit.java` - `sorted`, `limit`, `skip`, `min`, `max`.
- `EjemploStreamCollectors.java` - `toList`, `toSet`, `toMap`, `groupingBy`, `joining`, `counting`.
- `EjemploStreamEstadisticas.java` - Streams primitivos (`IntStream`) y `summaryStatistics`.
- `EjemploStreamParallel.java` - `parallel()` y el `ForkJoinPool` común.

### 2. `modelo`

- `Estudiante.java`, `Curso.java` - Modelos usados en los ejemplos.

## Conceptos Clave Aprendidos

### Naturaleza de los streams

- Un `Stream` **no almacena** datos: procesa una fuente.
- Es **lazy**: las operaciones intermedias no se ejecutan hasta la terminal.
- Es de **un solo uso**: una vez consumido no se puede reutilizar.

### Operaciones

| Tipo | Ejemplos |
|------|----------|
| Intermedias | `map`, `filter`, `flatMap`, `distinct`, `sorted`, `limit`, `skip`, `peek` |
| Terminales | `collect`, `forEach`, `reduce`, `count`, `findFirst`, `anyMatch`, `min`, `max` |

### Collectors

- `groupingBy` agrupa; `joining` concatena; `counting` cuenta como downstream.
- `summaryStatistics` calcula suma, máximo, mínimo, media y cuenta en una pasada.

### Paralelismo

- `parallel()` divide el trabajo en el `ForkJoinPool` común.
- El orden no está garantizado; usar `findAny()` y evitar efectos secundarios.

## Ejecución de Ejemplos

```bash
cd p08_api_stream
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.stream.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.stream.EjemploStreamCollectors"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.stream.EjemploStreamParallel"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── modelo/
└── stream/
```

## Notas Técnicas

- Un stream es perezoso: nada ocurre hasta la operación terminal.
- Los streams primitivos (`IntStream`, `LongStream`, `DoubleStream`) evitan el boxing.
- No reutilizar un stream; si se necesita, crear uno nuevo desde la fuente.

## Referencias

- [Stream API - Oracle](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [java.util.stream](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/package-summary.html)
