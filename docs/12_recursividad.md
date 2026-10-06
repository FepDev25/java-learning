# P12 - Recursividad

## Descripción General

Estudio de la recursividad: métodos que se llaman a sí mismos para resolver un
problema dividiéndolo en subproblemas más pequeños, incluyendo su aplicación al
recorrido de árboles con enfoque clásico y funcional (Streams).

## Información del Proyecto

- **Artifact ID:** p12_recursividad
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `recursividad`

- `EjemploBasico.java` - Estructura de la recursión: caso base y caso recursivo.
  Incluye factorial y otros ejemplos clásicos.
- `EjemploArbolRecursivo.java` - Recorrido de un árbol de carpetas de dos formas:
  1. **Recursión clásica:** llamada directa + bucle sobre los hijos.
  2. **Recursión Java 8:** `Stream.concat` + `flatMap` para producir un stream plano.

### 2. `modelo`

- `Nodo.java` - Nodo de árbol genérico (patrón Composite). `addHijo` devuelve `this`
  para permitir encadenamiento.

## Conceptos Clave Aprendidos

- **Estructura obligatoria:**
  1. **Caso base:** condición de parada (sin él → `StackOverflowError`).
  2. **Caso recursivo:** llamada a sí mismo con un problema más pequeño.
- Cada llamada ocupa un *frame* en el call stack; la profundidad es limitada (~10.000 por defecto).
- La recursión es natural para estructuras jerárquicas (árboles, carpetas, expresiones).
- En Java 8 se puede reemplazar el bucle por `Stream.concat` + `flatMap`.

## Ejecución de Ejemplos

```bash
cd p12_recursividad
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.recursividad.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.recursividad.EjemploBasico"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.recursividad.EjemploArbolRecursivo"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── modelo/
└── recursividad/
```

## Notas Técnicas

- `Nodo.addHijo` retorna `this` → permite `nodo.addHijo(a).addHijo(b)`.
- `flatMap` aplana `Stream<Stream<Nodo>>` en `Stream<Nodo>`.
- Alternativa a la recursión: usar una pila explícita (iterativo) para evitar el límite del stack.

## Referencias

- [Recursion (computer science)](https://en.wikipedia.org/wiki/Recursion_(computer_science))
- [Stream API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/package-summary.html)
