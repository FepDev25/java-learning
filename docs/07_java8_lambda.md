# P07 - Java 8: Expresiones Lambda

## Descripción General

Estudio de las expresiones lambda y la programación funcional en Java 8: interfaces
funcionales propias y del JDK, composición de funciones, `Predicate`, `Consumer`,
`Function` y referencias a métodos.

## Información del Proyecto

- **Artifact ID:** p07_java8_lambda
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `interfacefuncional` — Interfaces funcionales propias

- `Operacion.java` - Interfaz anotada con `@FunctionalInterface` (un único método abstracto).
- `Calculadora.java` - Recibe una lambda como parámetro (inyección de comportamiento)
  y su equivalente con `BiFunction`.
- `EjemploInterfaceFuncional.java` - Lambda, referencia a método y lambda inline.

### 2. `predicate` — Predicados

- `EjemploPredicado.java` - `Predicate<T>`, `BiPredicate<T,U>` y composición
  (`and`, `or`, `negate`).

### 3. `consumer` — Consumidores

- `EjemploConsumer.java` - `Consumer<T>`, `BiConsumer<T,U>`, `Supplier<T>`,
  referencia de método y `forEach`.

### 4. `function` — Funciones

- `EjemploFunction.java` - `Function<T,R>`, `BiFunction<T,U,R>` y composición con
  `andThen()` y `compose()`.

### 5. `referenciametodos` — Referencias a métodos

- `EjemploReferenciaMetodos.java` - Las 4 formas:
  1. `Clase::metodoEstatico`
  2. `instancia::metodoInstancia`
  3. `Clase::metodoInstancia`
  4. `Clase::new`

### 6. `modelo`

- `Estudiante.java` - Modelo usado en los ejemplos.

## Conceptos Clave Aprendidos

- Una **interfaz funcional** tiene exactamente un método abstracto; puede llevar
  `default` y `static`.
- Una **lambda** es una implementación anónima de una interfaz funcional.
- **Funciones de primera clase:** asignar lambdas a variables y pasarlas como parámetros.
- **Composición:** `andThen` / `compose` en `Function`; `and` / `or` / `negate` en `Predicate`.
- **Referencia a método:** azúcar sintáctico para lambdas que solo delegan a un método.
- Interfaces funcionales clave del JDK: `Function`, `Consumer`, `Supplier`, `Predicate`
  y sus variantes `Bi*`.

## Ejecución de Ejemplos

```bash
cd p07_java8_lambda
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<paquete>.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.interfacefuncional.EjemploInterfaceFuncional"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.referenciametodos.EjemploReferenciaMetodos"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── consumer/
├── function/
├── interfacefuncional/
├── modelo/
├── predicate/
└── referenciametodos/
```

## Notas Técnicas

- Las lambdas capturan variables efectivamente finales.
- `@FunctionalInterface` hace que el compilador rechace una segunda firma abstracta.
- Las referencias a métodos mejoran la legibilidad frente a lambdas triviales.

## Referencias

- [Lambda Expressions - Oracle](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
- [java.util.function](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/package-summary.html)
