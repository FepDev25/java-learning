# P05 - API de Colecciones

## Descripción General

Estudio del framework de colecciones de Java: listas (`List`), conjuntos (`Set`)
y mapas (`Map`), incluyendo implementaciones basadas en hash y árboles, ordenamiento
natural y personalizado.

## Información del Proyecto

- **Artifact ID:** p05_api_colecciones
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `list` — Listas ordenadas

- `EjemploArrayList.java` - `ArrayList`: agregar, eliminar, acceder y convertir a arreglo.
- `EjemploLinkedList.java` - `LinkedList`: operaciones en extremos y recorrido.
- `EjemploListComparableComparator.java` - Ordenamiento natural (`Comparable`) y con `Comparator`.

### 2. `set` — Conjuntos (sin duplicados)

- `EjemploHashSetAgregar.java` - Agregar elementos a un `HashSet`.
- `EjemploHashSetUnicidad.java` - Garantía de unicidad.
- `EjemploHashSetBuscarDuplicado.java` - Detección de duplicados.
- `EjemploHashSetBuscarDuplicado2.java` - Variante de detección de duplicados.
- `EjemploTreeSet.java` - `TreeSet` ordenado con `Comparator` personalizado.
- `EjemploTreeSetComparable.java` - `TreeSet` usando `Comparable` (orden por nota).

### 3. `map` — Mapas clave-valor

- `EjemploHashMap.java` - `HashMap` con valores de distintos tipos y mapas anidados.
- `EjemploTreeMap.java` - `TreeMap`: llaves ordenadas en orden natural.

### 4. `modelo`

- `Alumno.java` - Modelo con `Comparable` (por nombre y por nota) y `Comparator`.

## Conceptos Clave Aprendidos

### Jerarquía de colecciones

| Interfaz | Implementaciones | Orden | Duplicados |
|----------|------------------|-------|------------|
| `List` | `ArrayList`, `LinkedList` | Inserción | Sí |
| `Set` | `HashSet`, `TreeSet` | `HashSet` no garantiza; `TreeSet` ordenado | No |
| `Map` | `HashMap`, `TreeMap` | `TreeMap` ordena llaves | No en llaves |

### Ordenamiento

- `Comparable` define el orden natural de una clase (`compareTo`).
- `Comparator` define órdenes alternativos sin modificar la clase (`compare`).
- `TreeSet` y `TreeMap` requieren que los elementos/llaves sean comparables.

### Unicidad y hashing

`HashSet` / `HashMap` usan `equals()` y `hashCode()`. Para modelos propios hay que
sobrescribir ambos métodos correctamente.

### Genéricos

Las colecciones usan genéricos (`List<Alumno>`, `Map<String, Object>`), lo que aporta
seguridad de tipos en tiempo de compilación.

## Ejecución de Ejemplos

```bash
cd p05_api_colecciones
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<paquete>.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.list.EjemploListComparableComparator"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.map.EjemploHashMap"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── list/
├── map/
├── modelo/
└── set/
```

## Notas Técnicas

- `ArrayList` es un arreglo dinámico; acceso O(1), inserción/eliminación al final amortizado O(1).
- `LinkedList` implementa también `Deque`; inserción/eliminación O(1) en los extremos.
- `HashMap` no mantiene orden; `TreeMap` mantiene las llaves ordenadas (O(log n)).
- `TreeSet`/`TreeMap` se basan en árboles rojo-negro.

## Referencias

- [Collections Framework - Oracle](https://docs.oracle.com/javase/tutorial/collections/)
- [Java 21 API - java.util](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/package-summary.html)
