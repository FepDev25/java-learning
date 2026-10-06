# P02 - Arreglos en Java

## Descripción General

Este módulo estudia los arreglos (arrays) en Java: declaración, recorrido,
búsqueda, ordenamiento y algoritmos de manipulación (inserción, eliminación,
desplazamiento, combinación y clasificación).

## Información del Proyecto

- **Artifact ID:** p02_arreglos
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

Todas las clases viven directamente en el paquete `com.cultodeportivo`.

### 1. Fundamentos y recorrido

- `EjemploArreglos.java` - Declaración, inicialización y acceso a elementos.
- `EjemploArreglosFor.java` - Iteración con bucle `for`.
- `EjemploArreglosForInverso.java` - Recorrido inverso.
- `EjemploArreglosForInversoMutable.java` - Inversión del arreglo original.
- `EjemploArreglosForOrdenamientoBurbuja.java` - Ordenamiento burbuja (O(n²)).

### 2. Búsqueda

- `EjemploArreglosBuscarNumero.java` - Búsqueda lineal de un número.
- `EjemploArreglosBuscarString.java` - Búsqueda lineal de una cadena.

### 3. Manipulación

- `EjemploArreglosDesplazarPosicion.java` - Desplazamiento para insertar en una posición.
- `EjemploArreglosDesplazarPosicion2.java` - Variante con validaciones.
- `EjemploArreglosDesplazarPosicion2b.java` - Implementación alternativa.
- `EjemploArreglosDesplazarPosicion3.java` - Desplazamiento con entrada de usuario.
- `EjemploArreglosDesplazarPosicion3b.java` - Versión mejorada con manejo de errores.
- `EjemplosArreglosEliminarElemento.java` - Eliminación con reorganización.
- `EjemploArreglosCombinados.java` - Combinación (merge) de arreglos.
- `EjemploArreglosOrdenPrincipioFinal.java` - Reparto alternando inicio y final.

### 4. Análisis y clasificación

- `EjemploArreglosDetectarOrden.java` - Determinar si está ordenado (ascendente/descendente).
- `EjemploArreglosNumMayor.java` - Elemento máximo.
- `EjemploArreglosParesImpares.java` - Separación de pares e impares.
- `EjemploArregloNotasAlumnos.java` - Gestión de calificaciones y promedio.

## Conceptos Clave Aprendidos

### Estructura de datos lineal

Los arreglos tienen tamaño fijo, almacenan elementos del mismo tipo en posiciones
contiguas y se acceden por índice (base cero).

### Complejidad algorítmica

| Operación | Complejidad |
|-----------|-------------|
| Acceso por índice | O(1) |
| Búsqueda lineal | O(n) |
| Inserción / eliminación | O(n) |
| Ordenamiento burbuja | O(n²) |

### Patrones de iteración

```java
// Hacia adelante
for (int i = 0; i < array.length; i++) { }

// Inverso
for (int i = array.length - 1; i >= 0; i--) { }

// For-each (solo lectura)
for (TipoDato elemento : array) { }
```

### Inmutabilidad del tamaño

Un arreglo no puede redimensionarse. Para insertar o eliminar se crea un nuevo
arreglo, se copian los elementos y se omite/agrega el que corresponda.

### Utilidad de `java.util.Arrays`

- `Arrays.sort()` - Ordenamiento.
- `Arrays.toString()` - Representación en cadena.
- `Arrays.copyOf()` - Copia.
- `Arrays.fill()` - Llenado con un valor.

## Ejecución de Ejemplos

```bash
cd p02_arreglos
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<NombreClase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.EjemploArreglosForOrdenamientoBurbuja"
```

## Estructura del Proyecto

```text
p02_arreglos/
├── pom.xml
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── cultodeportivo/
│                   ├── EjemploArreglos.java
│                   ├── EjemploArreglosFor.java
│                   ├── EjemploArreglosForOrdenamientoBurbuja.java
│                   ├── EjemploArreglosBuscarNumero.java
│                   ├── EjemploArreglosParesImpares.java
│                   ├── EjemploArregloNotasAlumnos.java
│                   └── ... (más ejemplos)
└── target/
```

## Limitaciones de los Arreglos

1. **Tamaño fijo:** no se redimensionan dinámicamente.
2. **Tipo homogéneo:** todos los elementos deben ser del mismo tipo.
3. **Inserciones/eliminaciones costosas:** requieren desplazar elementos.

## Alternativas Modernas

- `ArrayList<T>` - Arreglo dinámico.
- `LinkedList<T>` - Lista enlazada.
- `Vector<T>` - Arreglo sincronizado (legacy).

## Notas Técnicas

- Los arreglos son objetos y viven en el heap.
- Se inicializan con valores por defecto (`0`, `false`, `null`).
- La propiedad `length` es final y representa el tamaño total.
- Los arreglos son covariantes.

## Referencias

- [Java Arrays Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html)
- [Arrays Tutorial - Oracle](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
- [Time Complexity](https://en.wikipedia.org/wiki/Time_complexity)
