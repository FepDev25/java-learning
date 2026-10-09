# 08 · Streams: describir un procesamiento de datos

## Contenido

- [1. Fuente, operaciones intermedias y operación terminal](#1-fuente-operaciones-intermedias-y-operación-terminal)
- [2. Qué significa cada dato](#2-qué-significa-cada-dato)
- [3. `map` y `peek`: transformar y observar](#3-map-y-peek-transformar-y-observar)
- [4. `filter`, búsqueda y preguntas booleanas](#4-filter-búsqueda-y-preguntas-booleanas)
- [5. Pasar de grupos a elementos](#5-pasar-de-grupos-a-elementos)
- [6. `distinct`, orden, límites y paginación](#6-distinct-orden-límites-y-paginación)
- [7. Combinar en un resultado](#7-combinar-en-un-resultado)
- [8. Materializar y agrupar](#8-materializar-y-agrupar)
- [9. Estadísticas y paralelismo](#9-estadísticas-y-paralelismo)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Fuente, operaciones intermedias y operación terminal

Un stream representa una secuencia que se procesa; no almacena datos como una lista. Empieza en una fuente —arreglo, colección, valores directos—, encadena transformaciones y termina en una operación que obtiene un resultado o ejecuta una acción.

El ejemplo de creación de streams usa `Stream.of`, `Arrays.stream`, `Stream.builder` y `lista.stream`. Por ejemplo:

```java
Stream<String> materias = Stream.of("Algoritmos", "Redes", "BD", "SO", "IA");
materias.map(String::toUpperCase)
        .forEach(System.out::println);
```

`map` describe la transformación; `forEach` consume la secuencia. Las operaciones intermedias son perezosas: normalmente no procesan los datos hasta una terminal. Después de usar el pipeline no se puede consumirlo otra vez; otro recorrido requiere un stream nuevo desde la misma fuente.

| Tipo | Operaciones del capítulo | Qué devuelve |
| --- | --- | --- |
| Intermedia | `map`, `filter`, `flatMap`, `distinct`, `sorted`, `limit`, `skip`, `peek` | Otro paso del stream |
| Terminal | `collect`, `reduce`, `sum`, `count`, `forEach`, `findFirst`, `min`, `max` | Resultado o efecto final |

Algunas terminales cortan el recorrido cuando ya tienen respuesta. Ordenar, en cambio, necesita considerar el conjunto de elementos antes de emitirlos ordenados. La API permite optimizaciones y puede omitir etapas sin efecto sobre el resultado; por eso `peek` no debe sostener una acción de negocio imprescindible. Consulta el contrato en [Stream de Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/stream/Stream.html).

### Fuentes diferentes, la misma forma de procesamiento

```java
// Extractos reducidos de la creación de streams.
String[] ciudades = {"Quito", "Guayaquil", "Cuenca"};
Arrays.stream(ciudades)
        .map(String::toUpperCase)
        .forEach(System.out::println);

List<String> materias = List.of("Cálculo", "Programación", "Inglés", "Física");
materias.stream()
        .map(String::toUpperCase)
        .forEach(System.out::println);
```

La primera fuente es un arreglo y la segunda una colección. Las dos suministran texto al mismo tipo de transformación. Se crean secuencias de procesamiento, no contenedores alternativos que sustituyan automáticamente al arreglo o a la lista.

```java
// Construcción explícita de una secuencia, con datos del ejemplo.
Stream<String> construido = Stream.<String>builder()
        .add("Felipe")
        .add("21 años")
        .add("Ecuador")
        .add("CS Student")
        .build();
```

El builder reúne los elementos elegidos y `build` proporciona el stream para consumirlos. El parámetro `<String>` determina el tipo. Todavía falta una operación terminal para procesar la secuencia construida.

## 2. Qué significa cada dato

`Curso` tiene nombre, créditos y nota. `Estudiante` tiene nombre, apellido, edad, país y una lista de cursos. `getPromedio()` usa `mapToDouble(Curso::getNota).average().orElse(0.0)`: calcula una media aritmética, no ponderada por créditos, y da cero si no hay cursos. Esa elección pierde la distinción entre «sin notas» y «promedio real cero».

`equals` y `hashCode` de estudiante utilizan solo nombre y apellido. La edad, el país y los cursos no participan, de modo que deduplicar supone que ese par identifica al estudiante para la práctica. No es necesariamente una identidad suficiente en una aplicación real.

### Un cálculo definido desde el modelo

```java
public double getPromedio() {
    return cursos.stream()
            .mapToDouble(Curso::getNota)
            .average()
            .orElse(0.0);
}
```

El stream nace de la lista de cursos de ese estudiante. La extracción cambia cada objeto por una nota primitiva; `average` reduce a una posible media y `orElse` decide qué devolver sin datos. La operación no utiliza los créditos, así las materias pesan por igual.

## 3. `map` y `peek`: transformar y observar

El ejemplo de transformación de estudiantes transforma textos como `"Felipe Perez"` en objetos, observa los creados con `peek` y luego cambia su nombre a mayúsculas antes de recolectarlos.

`map` recibe una `Function<T,R>` y produce un resultado por elemento; puede cambiar tipo. `peek` recibe un `Consumer<T>` y permite observar cada dato que llega a esa etapa. El `map` que usa `setNombre` muta el objeto, mientras que un `map(String::toUpperCase)` produce valores inmutables nuevos. La preservación de los objetos depende de los efectos de las funciones suministradas.

Los textos de ejemplo tienen exactamente nombre y apellido separados por un espacio. `split(" ")[1]` fallaría si falta apellido y no interpreta adecuadamente nombres compuestos. Es una precondición de esos datos, no un parser general de personas.

### Cambio de tipo y modificación de estado

```java
List<Estudiante> estudiantes = Stream
        .of("Felipe Perez", "Ana Torres", "Luis Mora", "Sara Vega")
        .map(nombre -> new Estudiante(
                nombre.split(" ")[0],
                nombre.split(" ")[1],
                21, "Ecuador"))
        .peek(e -> System.out.println("creado: " + e))   // debug sin alterar el stream
        .map(e -> {
            e.setNombre(e.getNombre().toUpperCase());     // segunda transformación
            return e;
        })
        .collect(Collectors.toList());
```

El primer `map` crea un objeto por nombre; `peek` observa el que circula y el segundo `map` devuelve ese mismo objeto tras cambiar su nombre. El tipo del stream cambia de texto a estudiante en la primera transformación y se conserva en la segunda. La terminal construye una lista con las referencias resultantes.

## 4. `filter`, búsqueda y preguntas booleanas

El ejemplo de filtrado de estudiantes construye seis estudiantes. Tres se llaman Felipe. `filter(e -> e.getNombre().equals("Felipe"))` conserva esos tres, tanto al recolectar como al contar. Para buscar Torres, `findFirst` devuelve a Ana Torres porque aparece antes que Luis Torres en esa lista.

La terminal devuelve `Optional<Estudiante>` porque puede no haber coincidencias. El `orElseThrow()` añadido al ejemplo devuelve el valor o lanza si los datos cambian y la coincidencia deja de existir. El capítulo 9 desarrolla cómo manejar esa ausencia.

`anyMatch` pregunta si alguno cumple, `allMatch` si todos y `noneMatch` si ninguno. La implementación ejecuta las dos primeras; las seis personas son de Ecuador, así ambas dan verdadero. En una secuencia vacía, `anyMatch` es falso y `allMatch` y `noneMatch` son verdaderos: no se encontró ninguna contradicción a esas dos últimas condiciones.

`findAny` puede elegir cualquier coincidencia, especialmente en paralelo; no satisface una regla que exija el primero según el orden de la fuente.

### Selección de elementos y primer resultado

```java
List<Estudiante> felipes = lista.stream()
        .filter(e -> e.getNombre().equals("Felipe"))
        .peek(System.out::println)
        .collect(Collectors.toList());
```

El predicado deja pasar solo nombres iguales a Felipe. `peek` muestra los que llegan a esa etapa, por lo que no imprime nombres descartados. Recolectar conserva tres coincidencias en los datos del ejemplo.

```java
Optional<Estudiante> primero = lista.stream()
        .filter(e -> e.getApellido().equals("Torres"))
        .findFirst();
```

Este es un stream nuevo de la misma lista, porque el anterior ya se consumió. La búsqueda puede detenerse al primer Torres; con el orden dado encuentra Ana. El contenedor de retorno expresa que cambiar la lista podría dejar la búsqueda sin respuesta.

## 5. Pasar de grupos a elementos

El ejemplo de aplanado de colecciones contrasta:

```java
lista.stream()
        .map(Estudiante::getCursos)
        .forEach(System.out::println);

List<Curso> todosCursos = lista.stream()
        .flatMap(e -> e.getCursos().stream())
        .collect(Collectors.toList());
```

La segunda parte es un **extracto simplificado** del pipeline del archivo, omitiendo su `peek`. `map` da un `Stream<List<Curso>>`: un elemento por estudiante. `flatMap` pide un stream por estudiante y concatena sus elementos: da `Stream<Curso>`. Felipe tiene dos cursos y Ana tres, por eso el resultado contiene cinco.

Las frases también se aplanan en palabras con `Arrays.stream(frase.split(" "))`. Una función de `flatMap` puede devolver cero, uno o varios resultados; el ejemplo usa `Stream.empty()` para excluir notas inferiores a 9. Un `filter` sería más directo cuando solo se requiere excluir valores.

El `peek` completo busca al dueño del curso recorriendo otra vez la lista por cada curso. Eso ayuda a mostrar procedencia, pero introduce recorridos adicionales y no conviene copiarlo como una solución eficiente para grandes volúmenes. El aplanado pierde el vínculo con el dueño si ese dato no se conserva explícitamente.

## 6. `distinct`, orden, límites y paginación

El ejemplo de eliminación de duplicados deduplica materias y estudiantes. Los estudiantes se deduplican por la regla de `equals/hashCode` del modelo; no por todos sus campos. Repetir Felipe Perez conserva una sola entrada en ese ejercicio. Cambiar la política de identidad cambia el resultado.

El ejemplo de ordenamiento y límites ordena cursos por nota descendente y toma tres con `limit(3)`: IA 9.3, Algoritmos 9.1 y BD 8.8. El orden de las operaciones importa: `sorted(...).limit(3)` obtiene los mejores tres; `limit(3).sorted(...)` solo ordenaría los tres primeros originales.

`skip(2)` omite los primeros dos del orden ya calculado. La implementación llama «página 2» a esa consulta, pero no añade `limit`: imprime todos los restantes. Una paginación completa, como adaptación, combinaría `skip(numeroPagina * tamaño).limit(tamaño)`, usando una convención clara para numerar páginas.

`min` y `max` usan un comparador y devuelven `Optional`, por posible vacío. Los estudiantes del ejemplo tienen promedios distintos, así se puede escoger el mayor o menor; un comparador no obliga a que todos sean diferentes.

### Igualdad, orden y selección de los mejores

```java
Stream.of("Algoritmos", "Redes", "Algoritmos", "BD", "Redes", "IA")
        .distinct()
        .forEach(System.out::println);
```

Se conserva la primera aparición de cada materia en esta secuencia ordenada. La comparación de cadenas usa contenido, no identidad. El resultado contiene Algoritmos, Redes, BD e IA sin cambiar la fuente original.

```java
cursos.stream()
        .sorted(Comparator.comparingDouble(Curso::getNota).reversed())
        .limit(3)
        .forEach(c -> System.out.println("  " + c));
```

`sorted` crea el orden de procesamiento por nota descendente. `limit(3)` conserva tres posiciones de ese orden, y la terminal las muestra. Ordenar y luego limitar responde a una consulta de mejores valores; invertir esas etapas responde a otra pregunta.

## 7. Combinar en un resultado

En el ejemplo de reducción de valores:

```java
int totalCreditos = Stream.of(4, 3, 4, 2, 3)
        .reduce(0, Integer::sum);
```

El acumulador empieza en 0 y produce 4, 7, 11, 13 y 16. Cero es la identidad de suma: combinarlo con un valor conserva ese valor. Sin identidad, `reduce(Integer::max)` devuelve `Optional<Integer>` porque no puede inventar un máximo para el vacío; en esos datos vale 4.

Para el promedio ponderado, el ejemplo suma nota×créditos y divide por suma de créditos:

```java
double promPonderado = cursos.stream()
        .reduce(0.0,
                (acum, c) -> acum + c.getNota() * c.getCreditos(),
                Double::sum)
        / totalCred;
```

Los cuatro cursos dan suma ponderada `120.8`, créditos `14` y promedio aproximado `8.63`. El acumulador incorpora un curso a un `Double`; el combinador une resultados parciales si el procesamiento se divide. Ambos deben ser compatibles y la reducción, asociativa para una combinación segura. La suma decimal tiene límites de precisión.

El ejemplo secuencial que comienza una concatenación con `"Materias aprobadas: "` mezcla cabecera y acumulación; esa cabecera no es una identidad neutra para paralelizar. Para unir texto con prefijo y separador conviene `Collectors.joining`. Si todos los créditos sumaran cero, la división del promedio tampoco tendría un significado válido.

## 8. Materializar y agrupar

El ejemplo de recolección y agrupación obtiene nombres, texto unido, mapa de promedios y agrupaciones. `collect` es la terminal; `Collectors.toList()` y los demás crean las estrategias de recolección.

`toMap` usa nombre completo como clave y promedio como valor. Si dos datos generan la misma clave sin proporcionar una función de fusión, la recolección falla; no sustituye silenciosamente como un `put` ordinario. Nombre completo evita las colisiones de solo nombre en algunos casos, no en todos.

`groupingBy(Estudiante::getPais)` genera `Map<String,List<Estudiante>>`. `groupingBy(...,counting())` cambia los valores por conteos: Ecuador 2, Colombia 1 y Peru 1 en los datos del archivo. Al aplanar cursos y agrupar por nombre, Algoritmos aparece tres veces y Redes dos. Contar esas apariciones no es lo mismo que contar cursos distintos.

`Collectors.toList()` no promete una implementación ni mutabilidad concreta. `Stream.toList()` es una API diferente que devuelve una lista no modificable. Para obtener una implementación específica, una adaptación sería `toCollection(ArrayList::new)`.

### Claves de agrupación y reducción dentro de cada grupo

```java
Map<String, List<Estudiante>> porPais = estudiantes.stream()
        .collect(Collectors.groupingBy(Estudiante::getPais));
```

Cada país se vuelve una clave y cada valor es la lista de estudiantes que comparten ese país. No hay exclusión de estudiantes ni deduplicación automática: cada entrada de la fuente pertenece al grupo que determine el clasificador.

```java
Map<String, Long> cantPorPais = estudiantes.stream()
        .collect(
                Collectors.groupingBy(
                        Estudiante::getPais,
                        Collectors.counting()
                )
        );
```

El clasificador sigue siendo país, pero el recolector de cada grupo cuenta en lugar de conservar una lista. El tipo del valor cambia a `Long`. El procesamiento expresa dos niveles: cómo se forman grupos y cómo se resumen sus elementos.

```java
Map<String, Double> promedios = estudiantes.stream()
        .collect(Collectors.toMap(
                e -> e.getNombre() + " " + e.getApellido(),
                Estudiante::getPromedio)
        );
```

La clave combina nombre y apellido y el valor consulta la media del estudiante. Esta variante exige claves sin repetición o una regla explícita para resolver colisiones; sin esa regla, dos nombres completos iguales causan un error de recolección.

## 9. Estadísticas y paralelismo

El ejemplo de estadísticas de streams convierte cursos a `IntStream` con sus créditos y obtiene suma, cantidad, extremos y media mediante `summaryStatistics`. Para 4,3,4,3,4,2, la suma es 20, cantidad 6, mínimo 2, máximo 4 y media 20/6. `range(a,b)` excluye b; `rangeClosed(a,b)` lo incluye. Las semanas 1 a 16 filtradas por múltiplos de cuatro son 4,8,12,16.

El ejemplo de procesamiento paralelo compara secuencial y paralelo con una carga simulada de 500 ms. El secuencial usa `findFirst` y el paralelo `findAny`: pueden procesar cantidades diferentes por cortocircuito y planificación. Una medición de ese archivo no demuestra que paralelo siempre sea más rápido.

En paralelo, las funciones pueden ejecutarse en distintos hilos y los mensajes de `peek` mezclarse. Escribir en listas compartidas o depender del orden de efectos introduce riesgos de coordinación; la recolección adecuada administra los resultados. `forEachOrdered` puede conservar el orden de encuentro cuando sea necesario, con un costo potencial. Paralelizar añade coordinación y no sustituye el razonamiento de concurrencia del capítulo anterior.

### Resumen numérico en una sola terminal

```java
IntSummaryStatistics stats = cursos
        .peek(c -> System.out.println("  Procesando: " + c))
        .mapToInt(Curso::getCreditos)
        .summaryStatistics();
```

La extracción transforma cursos en créditos primitivos. El resumen obtiene cantidad, suma, mínimo, máximo y media a partir del mismo recorrido. Los mensajes de observación pueden mostrar el paso de valores, pero el objeto de estadísticas es el resultado que determina la operación.

### Búsqueda paralela y cantidad de trabajo variable

```java
Optional<String> resPar = estudiantes.stream()
        .parallel()
        .map(e -> {
            simularCarga();
            return e.toString().toUpperCase();
        })
        .peek(n -> System.out.println("PAR  | " + Thread.currentThread().getName() + " | " + n))
        .filter(n -> n.contains("FELIPE"))
        .findAny();      // findAny es más natural en paralelo
```

El stream paralelo puede distribuir elementos entre trabajadores. `findAny` permite devolver una coincidencia sin imponer la selección del primer elemento; algunas tareas ya iniciadas pueden avanzar mientras se resuelve la búsqueda. El orden de impresiones y el tiempo de ejecución no describen un contrato fijo del resultado.

## Preguntas de repaso

1. ¿Qué convierte `Stream<List<Curso>>` en `Stream<Curso>`? **`flatMap` con un stream por lista.**
2. ¿La segunda llamada terminal puede reutilizar el mismo stream? **No; hay que crear otro.**
3. ¿Media y media ponderada coinciden siempre? **No; solo en condiciones particulares, como pesos iguales.**
4. ¿Un stream garantiza que sus funciones no mutan datos? **No; depende de las funciones suministradas.**
