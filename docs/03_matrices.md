# P03 - Matrices en Java

## Descripción General

Este módulo profundiza en las matrices (arreglos bidimensionales): declaración,
recorrido, operaciones (suma, transposición), matrices especiales y búsqueda.

## Información del Proyecto

- **Artifact ID:** p03_matrices
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

Todas las clases viven directamente en el paquete `com.cultodeportivo`.

### 1. Fundamentos

- `EjemploMatrices.java` - Declaración, inicialización y acceso a elementos.
- `EjemploMatricesStringFor.java` - Iteración sobre matrices de cadenas con `for`.
- `EjemploMatricesStringFor2.java` - Variante de iteración sobre matrices de strings.
- `EjemploMatricesColumnasVariable.java` - Matrices con columnas de distinto tamaño (jagged).

### 2. Operaciones matriciales

- `EjemploMatricesSumar.java` - Suma de dos matrices elemento a elemento: `C[i][j] = A[i][j] + B[i][j]`.
- `EjemploMatricesSumarFilasColumnas.java` - Suma por filas y por columnas.

### 3. Transformaciones

- `EjemploMatricesTranspuesta.java` - Muestra la matriz transpuesta sin crear una nueva.
- `EjemploMatricesTransponerMatriz.java` - Crea la matriz transpuesta: `B[j][i] = A[i][j]`.

### 4. Búsqueda

- `EjemploMatricesBuscar.java` - Búsqueda de un valor recorriendo filas y columnas.

### 5. Matrices especiales

- `EjemploMatricesIdentidad.java` - Matriz identidad.
- `EjemploMatricesSimetrica.java` - Comprobación de matriz simétrica.
- `EjemploMatricesMarco.java` - Dibujo de un marco/patrón con los bordes.

## Conceptos Clave Aprendidos

- Una matriz es un arreglo de arreglos (`Tipo[][]`), accesible como `matriz[fila][columna]`.
- Se recorren con bucles anidados o con `for-each` anidado.
- La transposición intercambia filas por columnas.
- Las matrices pueden ser irregulares (cada fila con distinta longitud).
- La matriz identidad y la simetría son propiedades matemáticas verificables con bucles.

## Ejecución de Ejemplos

```bash
cd p03_matrices
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<NombreClase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.EjemploMatricesSumar"
```

## Estructura del Proyecto

```text
p03_matrices/
├── pom.xml
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── cultodeportivo/
│                   ├── EjemploMatrices.java
│                   ├── EjemploMatricesSumar.java
│                   ├── EjemploMatricesTransponerMatriz.java
│                   └── ... (más ejemplos)
└── target/
```

## Notas Técnicas

- Las matrices en Java son arreglos de referencias a otros arreglos.
- El acceso es `matriz.length` filas y `matriz[i].length` columnas de la fila `i`.
- Se inicializan con valores por defecto igual que los arreglos.
- Para matrices regulares se puede asumir `matriz[i].length` constante.

## Referencias

- [Java Arrays Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html)
- [The Java Tutorials - Arrays](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
