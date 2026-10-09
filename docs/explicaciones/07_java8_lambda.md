# 07 · Lambdas: pasar comportamiento como argumento

## Contenido

- [1. Un método puede recibir qué hacer](#1-un-método-puede-recibir-qué-hacer)
- [2. Las firmas del JDK te dicen cómo usar cada función](#2-las-firmas-del-jdk-te-dicen-cómo-usar-cada-función)
- [3. Acciones y proveedores](#3-acciones-y-proveedores)
- [4. Transformación y composición](#4-transformación-y-composición)
- [5. Reglas que se combinan](#5-reglas-que-se-combinan)
- [6. Cuatro formas de delegar](#6-cuatro-formas-de-delegar)
- [7. Captura de variables y efectos](#7-captura-de-variables-y-efectos)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Un método puede recibir qué hacer

Cuando una calculadora siempre suma, su comportamiento queda fijo. Si recibe una operación, puede ejecutar suma, resta o máximo sin cambiar su método principal. Una lambda expresa ese comportamiento mediante una interfaz funcional: una interfaz con un único método abstracto funcional, que puede además tener métodos `default` y estáticos.

`Operacion` define:

```java
@FunctionalInterface
public interface Operacion {
    double calcular(double a, double b);
}
```

En el ejemplo de operaciones de una calculadora:

```java
Operacion suma  = (a, b) -> a + b;
Operacion resta = (a, b) -> a - b;
Operacion max = Math::max;
```

Los nombres `a` y `b` representan los parámetros de `calcular`. El tipo objetivo permite inferir que son `double`. La expresión de la derecha produce su retorno; un bloque con varias instrucciones necesita `return` si debe devolver algo.

Declarar la lambda no calcula aún: el cálculo ocurre cuando `Calculadora.computar` llama a `op.calcular(a,b)`. Para 4 y 3, las operaciones producen 7, 1 y 4. La llamada inline de multiplicación produce 12; `computarConBiFunction(2,10,Math::pow)` produce 1024.

Es una implementación del contrato funcional, pero la sintaxis no implica que Java la transforme exactamente en una clase anónima ordinaria. Aquí interesa su comportamiento, no su identidad interna.

## 2. Las firmas del JDK te dicen cómo usar cada función

| Interfaz | Entrada | Retorno | Método que la ejecuta | Ejemplo del módulo |
| --- | --- | --- | --- | --- |
| `Consumer<T>` | Un T | Ninguno | `accept` | Mostrar un estudiante |
| `BiConsumer<T,U>` | T y U | Ninguno | `accept` | Asignar carrera |
| `Supplier<T>` | Ninguna | Un T | `get` | Crear un estudiante vacío |
| `Function<T,R>` | Un T | Un R | `apply` | Texto → estudiante |
| `BiFunction<T,U,R>` | T y U | Un R | `apply` | Nombre y promedio → descripción |
| `Predicate<T>` | Un T | `boolean` | `test` | Comprobar aprobación |
| `BiPredicate<T,U>` | T y U | `boolean` | `test` | Comparar carreras |

Los parámetros genéricos representan objetos, por eso aparecen `Double` e `Integer`. El JDK también tiene variantes primitivas cuando conviene evitar boxing; el capítulo de Streams muestra `mapToDouble` y `mapToInt`.

## 3. Acciones y proveedores

El ejemplo de consumidores y proveedores crea un estudiante y ejecuta un consumidor que lo imprime. `mostrar.accept(felipe)` no devuelve una descripción: escribe en la consola. Un `BiConsumer` asigna su carrera y luego la muestra. Como el objeto es mutable, la modificación se observa en llamadas posteriores.

`Consumer<String> imprimir = System.out::println` se reutiliza mediante `materias.forEach(imprimir)`. `forEach` recibe un consumidor y lo aplica a cada valor. El comentario «Stream forEach» en algunos ejemplos del curso no significa necesariamente que se haya creado un stream: las colecciones también ofrecen su propio `forEach`.

`BiConsumer<Estudiante, Double> actualizarPromedio = Estudiante::setPromedio` usa el primer argumento como receptor y el segundo como valor. `actualizarPromedio.accept(felipe,9.2)` equivale a llamar a `felipe.setPromedio(9.2)`.

`Supplier<Estudiante> nuevoEstudiante = Estudiante::new` no recibe argumentos: cada `get()` llama al constructor vacío. Un supplier también puede devolver una constante o una consulta; su firma no obliga a crear siempre un objeto nuevo.

### Acciones con uno o dos argumentos

```java
Consumer<Estudiante> mostrar = e ->
        System.out.println("Estudiante: " + e.getNombre() + " | promedio: " + e.getPromedio());

Estudiante felipe = new Estudiante("Felipe", 21, "Ciencias de la Computación", 8.9);
mostrar.accept(felipe);
```

El cuerpo obtiene datos del objeto y produce un efecto en consola. El contrato no devuelve la cadena mostrada: `accept` termina sin valor. La creación del estudiante y la invocación del consumidor son momentos diferentes.

```java
BiConsumer<Estudiante, String> asignarCarrera = (e, carrera) -> {
    e.setCarrera(carrera);
    System.out.println(e.getNombre() + " ahora estudia: " + carrera);
};
asignarCarrera.accept(felipe, "Ingeniería de Software");
```

El primer argumento es el objeto afectado y el segundo es la carrera nueva. Modificar mediante el setter cambia el estado que verán otras referencias al mismo estudiante. Un consumidor no promete ausencia de efectos; al contrario, su firma describe una acción.

```java
Supplier<Estudiante> nuevoEstudiante = Estudiante::new;
Estudiante otro = nuevoEstudiante.get();
```

La referencia al constructor no recibe datos porque la firma del supplier no tiene parámetros. Cada `get` obtiene el resultado de esa construcción; asignar el supplier a una variable no crea todavía al estudiante.

## 4. Transformación y composición

En el ejemplo de transformaciones funcionales, `Function<String,String>` convierte a mayúsculas y `Function<String,Estudiante>` construye un objeto. Una función puede cambiar el tipo, no solo modificar texto.

```java
Function<String, Estudiante> crearEstudiante =
        nombre -> new Estudiante(nombre, 21, "CS", 8.5);

Function<String, String> nombreMayus = crearEstudiante.andThen(est -> est.getNombre().toUpperCase());
```

`andThen(g)` representa `g(f(x))`: primero crea el estudiante y luego obtiene su nombre en mayúsculas. Para `"felipe"`, resulta `"FELIPE"`. `compose(g)` invierte el orden de composición: ejecuta g antes de f. La implementación comenta `compose`, pero no contiene una llamada de demostración; una adaptación sería normalizar primero un nombre y luego pasarlo a `crearEstudiante`.

La `BiFunction<Integer,Integer,Long>` de suma convierte `(a + b)` a `long` **después** de sumar enteros. Para 4 y 3 funciona; para valores que desborden `int`, el cast posterior no arregla el desbordamiento. Una adaptación sería `(a,b) -> (long) a + b`.

El comentario que presenta `String::compareTo` como método estático es incorrecto: es un método de instancia sin receptor fijado. Con dos textos, el primero actúa como receptor y el segundo como parámetro.

### Dos entradas y una salida diferente

```java
BiFunction<String, Double, String> resumen =
        (nombre, prom) -> nombre + " de Ecuador tiene promedio " + prom;
System.out.println(resumen.apply("Felipe", 9.1));
```

El contrato recibe nombre y promedio y produce una descripción. Las dos entradas tienen tipos diferentes y el resultado es texto. `apply` es el punto en que se evalúa la concatenación con los argumentos suministrados.

### Orden de composición

```java
// Adaptación con dos operaciones numéricas que hacen visible el orden.
Function<Integer, Integer> duplicar = n -> n * 2;
Function<Integer, Integer> sumarTres = n -> n + 3;

int primeroDuplicar = duplicar.andThen(sumarTres).apply(4);
int primeroSumar = duplicar.compose(sumarTres).apply(4);
```

El primer resultado es 11: 4 se duplica a 8 y luego se suman 3. El segundo es 14: 4 se convierte en 7 antes de duplicarse. La composición une funciones compatibles por tipos, pero no hace intercambiable su orden.

## 5. Reglas que se combinan

El ejemplo de predicados compuestos declara `aprobado` desde 7 y `conHonores` desde 9. `and` exige ambos, `or` cualquiera y `negate` invierte. Como honores ya implica aprobación en estas reglas, `aprobado.and(conHonores)` equivale a honores y `aprobado.or(conHonores)` equivale a aprobación. No todas las combinaciones añaden una regla nueva.

Los predicados combinados también hacen cortocircuito: `and` no prueba el segundo si el primero falla; `or` no lo prueba si el primero basta. `test(8.9)` devuelve verdadero para aprobación, mientras que `test(5.5)` devuelve falso.

`BiPredicate<String,String> iguales = String::equals` compara contenido. La etiqueta que muestra `==` en la impresión no describe el operador realmente utilizado. `mismaCarrera` consulta los objetos y usa `equals` entre carreras; presupone que esas cadenas existen. Las lambdas no añaden protección automática contra nulos.

### Reglas independientes y combinaciones

```java
Predicate<Double> conHonores = promedio -> promedio >= 9.0;
Predicate<Double> aprobadoConHonores = aprobado.and(conHonores);
Predicate<Double> aprobadoOHonores   = aprobado.or(conHonores);
Predicate<Double> reprobado           = aprobado.negate();
```

`aprobado` se ha definido previamente con umbral 7.0. La combinación `and` exige ambos predicados; `or` acepta cualquiera y `negate` produce la regla contraria. Para 9.1 son verdaderos aprobación y honores; para 6.5 ambos son falsos y la regla de reprobado es verdadera.

```java
BiPredicate<Estudiante, Double> superaPromedio =
        (e, umbral) -> e.getPromedio() > umbral;
System.out.println("¿Felipe supera 9.0? " + superaPromedio.test(a, 9.0));
```

Una entrada representa al estudiante y otra al umbral. El resultado solo responde a la comparación y no modifica el promedio. El operador estricto `>` excluye un empate exacto con el umbral.

## 6. Cuatro formas de delegar

El ejemplo de referencias de métodos permite comparar escritura abreviada y lambda:

| Referencia | Equivalente conceptual | Cómo recibe el objeto |
| --- | --- | --- |
| `Math::max` | `(a,b) -> Math.max(a,b)` | Método estático |
| `System.out::println` | `s -> System.out.println(s)` | Receptor ya fijado |
| `String::toUpperCase` | `s -> s.toUpperCase()` | Receptor en el primer argumento |
| `Estudiante::new` | `() -> new Estudiante()` | Constructor según la firma objetivo |

La firma destino selecciona el método o constructor compatible. No todas las referencias sirven para cualquier interfaz. `Comparator.comparingDouble(Estudiante::getPromedio).reversed()` extrae promedios y genera un criterio descendente; el ranking del ejemplo empieza por Sara (9.4), seguida de Felipe (9.1), Ana (8.5) y Luis (7.8).

### El primer argumento puede ser el receptor

```java
Function<String, String> mayusculas = String::toUpperCase;
System.out.println(mayusculas.apply("ciencias de la computacion"));
```

`String::toUpperCase` no es un método estático: el texto recibido por `apply` actúa como receptor. La referencia equivale conceptualmente a `texto -> texto.toUpperCase()`. El método devuelve otra cadena y no modifica el texto original.

```java
lista.sort(Comparator.comparingDouble(Estudiante::getPromedio).reversed());
System.out.println("\nRanking por promedio:");
lista.forEach(est -> System.out.println("  " + est));
```

El extractor obtiene el promedio numérico y el comparador lo invierte para mostrar valores mayores primero. La lista se reordena, pero las notas de sus objetos no cambian. La referencia de método describe la extracción, no el momento ni la cantidad de veces que el algoritmo de ordenamiento la invoca.

### Construcción y consulta de clubes

El ejemplo de referencias de método con clubes contiene:

```java
Function<String, Club> fabricarClubes = Club::new;
Club club = fabricarClubes.apply("Felipe FC");
club.setCiudad("Cuenca");
club.setFundacion(LocalDate.of(2026, 1, 1));
club.setJugadores(List.of("Hazard", "Messi", "Cristiano"));

Function<Club, String> presentarJugadores = Club::presentarJugadores;
String presentacion = presentarJugadores.apply(club);
```

`Club::new` utiliza aquí el constructor con nombre, no el vacío, porque la función recibe `String`. `Club::presentarJugadores` recibe un club como receptor y no requiere parámetros adicionales. `String.join` dentro del modelo produce `"Hazard, Messi, Cristiano"`.

El constructor vacío inicializa una lista mutable, pero el setter la reemplaza por `List.of`, que no permite agregar elementos. El nombre del campo `Ciudad` usa mayúscula en el fuente; el acceso se hace mediante `getCiudad` y `setCiudad`. No existe una validación automática por usar un modelo en vez de un mapa.

## 7. Captura de variables y efectos

Una lambda puede leer variables locales externas si son finales o efectivamente finales: no se reasignan después de capturarlas. Esto no vuelve inmutables los objetos referenciados. La lambda que asigna carrera puede mutar un estudiante aunque no reasigne la variable local que lo contiene.

Pasar comportamiento tampoco significa ejecutar en otro hilo: `apply`, `test` y `accept` son llamadas normales. Solo hay concurrencia cuando un mecanismo como `Thread` o un executor decide ejecutarlas de ese modo.

### Referencia estable y objeto mutable

```java
// Adaptación del uso de un consumidor sobre un estudiante.
Estudiante estudiante = new Estudiante("Felipe", 21, "CS", 8.9);
Consumer<Double> cambiarPromedio = nota -> estudiante.setPromedio(nota);
cambiarPromedio.accept(9.2);
```

La variable local `estudiante` conserva la referencia asignada, de modo que es efectivamente final y puede capturarse. La lambda modifica el objeto al que apunta, no la variable local. Reasignar después `estudiante` a otro objeto impediría esa captura aunque ambos objetos tuvieran el mismo tipo.

## Preguntas de repaso

1. ¿Guardar una lambda calcula su resultado? **No; hay que invocar el método funcional.**
2. ¿`Consumer` devuelve texto? **No; ejecuta una acción sin retorno.**
3. ¿En `String::compareTo` el método es estático? **No; el primer texto es el receptor.**
4. ¿`andThen` aplica la segunda función primero? **No; la aplica al resultado de la primera.**
