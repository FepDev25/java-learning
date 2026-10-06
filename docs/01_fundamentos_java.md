# P01 - Fundamentos de Java

## Descripción General

Este módulo cubre los conceptos fundamentales del lenguaje Java: sintaxis básica,
tipos de datos primitivos, operadores, estructuras de control, manejo de cadenas,
entrada/salida, clases utilitarias del JDK y nociones del entorno de ejecución.

## Información del Proyecto

- **Artifact ID:** p01_basicos
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `fundamentos` — Tipos de datos y conversiones

- `HolaMundo.java` - Programa mínimo: estructura de una clase y `main`.
- `PrimitivosEnteros.java` - Tipos `byte`, `short`, `int`, `long`.
- `PrimitivosFlotantes.java` - Tipos `float` y `double`.
- `PrimitivosCaracteres.java` - Tipo `char` y representación Unicode.
- `PrimitivosBoolean.java` - Tipo `boolean`.
- `ConversionDeTipos.java` - Casting implícito y explícito entre primitivos.
- `SistemasNumericos.java` - Decimal, binario, octal y hexadecimal.

### 2. `operadores` — Operadores del lenguaje

- `OperadoresAritmeticos.java` - Suma, resta, multiplicación, división y módulo.
- `OperadoresAsignacion.java` - Asignación compuesta (`+=`, `-=`, `*=`, `/=`, `%=`).
- `OperadoresIncrementales.java` - Pre y post incremento/decremento (`++`, `--`).
- `OperadoresRelacionales.java` - Comparaciones (`==`, `!=`, `<`, `>`, `<=`, `>=`).
- `OperadoresLogico.java` - Operadores AND (`&&`), OR (`||`) y NOT (`!`).
- `OperadoresLogicosLogin.java` - Ejemplo práctico de validación de credenciales.
- `OperadoresUnarios.java` - Operadores unarios de negación.
- `PrecedenciaOperadores.java` - Orden de evaluación de expresiones.

### 3. `condicionales` — Estructuras condicionales

- `SentenciaIfElse.java` - Estructura `if` / `else`.
- `SentenciaIfElseNumDiasMes.java` - Determinar los días de un mes.
- `SentenciaSwitchCase.java` - Estructura `switch` clásica.
- `SentenciaSwitchCaseNumDiasMes.java` - Días del mes con `switch`.
- `OperadorTernario.java` - Operador condicional `? :`.
- `OperadorTernarioNumeroMayor.java` - El mayor de dos números con ternario.

### 4. `bucles` — Estructuras repetitivas

- `SentenciaFor.java` - Bucle `for` básico.
- `SentenciaForArreglo.java` - Iteración sobre arreglos con `for` (usa `JOptionPane`).
- `SentenciaForEach.java` - Bucle `for-each`.
- `SentenciaWhile.java` - Bucle `while`.
- `SentenciasBucleEtiquetas.java` - Uso de etiquetas (`labels`).
- `SentenciasBucleEtiquetasBuscar.java` - Búsqueda con `break` y etiquetas.

### 5. `strings` — Cadenas de texto

- `EjemploString.java` - Creación y manipulación básica de cadenas.
- `EjemploStringMetodos.java` - Métodos principales de `String`.
- `EjemploStringMetodosArreglo.java` - Conversión entre `String` y arreglos de `char`.
- `EjemploStringConcatenacion.java` - Concatenación de cadenas.
- `EjemploStringInmutable.java` - Demostración de la inmutabilidad de `String`.
- `EjemploStringValidar.java` - Validación de cadenas.
- `EjemploStringExtensionArchivo.java` - Extracción de la extensión de un archivo.
- `EjemploStringTestRendimientoConcat.java` - Rendimiento de `String`, `StringBuilder` y `StringBuffer`.

### 6. `wrapperautoboxing` — Wrapper classes y autoboxing

- `WrapperInteger.java` - Clase envoltorio `Integer`.
- `WrapperBoolean.java` - Clase envoltorio `Boolean`.
- `AutoboxingInteger.java` - Autoboxing y unboxing automático.
- `WrapperOperadoresRelacionales.java` - Comparación de objetos wrapper.

### 7. `entradasalida` — Entrada y salida

- `SistemasNumericosEntradaScanner.java` - Lectura y validación con `Scanner`.
- `ArgumentosLineaComando.java` - Procesamiento de argumentos de `main`.
- `ArgumentosLineaComandoCalculadora.java` - Calculadora simple por argumentos.

### 8. `utildate` — Fecha y hora (java.util)

- `EjemploJavaUtilDate.java` - Uso básico de `Date`.
- `EjemploJavaUtilDateParse.java` - Parseo de fechas con `SimpleDateFormat`.
- `EjemploJavaUtilCalendar.java` - Manipulación de fechas con `Calendar`.

### 9. `adicionales` — Clases utilitarias

- `EjemploClaseMath.java` - Funciones de `Math` (`abs`, `max`, `min`, `pow`, `sqrt`, `random`, etc.).
- `EjemploMetodoGetClass.java` - Introspección básica con `getClass()`.

### 10. `operadorinstanceof` — Verificación de tipos

- `OperadorInstanceOf.java` - `instanceof` en tiempo de ejecución.
- `OperadorInstanceOfTiposGenericos.java` - `instanceof` con tipos genéricos y pattern matching.

### 11. `pasovalorreferencia` — Paso de parámetros

- `PasarPorValor.java` - Paso por valor de tipos primitivos.
- `PasarPorReferencia.java` - Paso por referencia de objetos.
- `PasoPorReferencia2.java` - Ejemplos adicionales de paso por referencia.

### 12. `entornosistema` — Entorno del sistema

- `EjemploVariablesDeEntorno.java` - Acceso a variables de entorno.
- `EjemploPropiedadesDeSistema.java` - Propiedades del sistema (`System.getProperties`).
- `EjemploAsignarPropiedadesDeSistema.java` - Carga de `config.properties` desde el classpath.
- `EjemploEjecutarProgramaSO.java` - Ejecución de programas externos con `ProcessBuilder`.

## Conceptos Clave Aprendidos

1. **Tipos primitivos:** los 8 tipos de Java y sus rangos.
2. **Operadores:** aritméticos, relacionales, lógicos, de asignación y unarios.
3. **Estructuras de control:** condicionales (`if`, `switch`, ternario) y bucles (`for`, `while`).
4. **Inmutabilidad de `String`:** y uso de `StringBuilder` / `StringBuffer` para optimizar.
5. **Wrapper classes:** autoboxing y unboxing.
6. **Entrada/Salida:** `Scanner`, argumentos de línea de comandos y `JOptionPane`.
7. **API de utilidades:** `Math`, `Date`, `Calendar`.
8. **Introspección básica:** `instanceof` y `getClass()`.
9. **Interacción con el sistema:** propiedades, variables de entorno y procesos.

## Ejecución de Ejemplos

```bash
cd p01_basicos
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<paquete>.<NombreClase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.operadores.OperadoresAritmeticos"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.fundamentos.HolaMundo"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── adicionales/
├── bucles/
├── condicionales/
├── entradasalida/
├── entornosistema/
├── fundamentos/
├── operadores/
├── operadorinstanceof/
├── pasovalorreferencia/
├── strings/
├── utildate/
└── wrapperautoboxing/
```

## Notas Técnicas

- Todos los ejemplos son independientes y pueden ejecutarse por separado.
- `EjemploAsignarPropiedadesDeSistema` carga `src/main/resources/config.properties`
  como recurso del classpath, por lo que funciona sin depender del directorio de trabajo.
- `EjemploEjecutarProgramaSO` usa `user.dir` como directorio, en lugar de una ruta fija.
- `SentenciaForArreglo` y varios ejemplos antiguos usan `JOptionPane` (requieren entorno gráfico).

## Referencias

- [Java SE 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Java Language Specification](https://docs.oracle.com/javase/specs/)
- [Maven Documentation](https://maven.apache.org/guides/)
