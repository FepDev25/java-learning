# 05 · Colecciones: elegir estructura, igualdad y orden

## Contenido

- [1. Qué aporta una colección frente a un arreglo](#1-qué-aporta-una-colección-frente-a-un-arreglo)
- [2. Identidad, igualdad y orden de objetos](#2-identidad-igualdad-y-orden-de-objetos)
- [3. Insertar, reemplazar y eliminar](#3-insertar-reemplazar-y-eliminar)
- [4. Orden natural frente a comparador externo](#4-orden-natural-frente-a-comparador-externo)
- [5. Dos sentidos de «único»](#5-dos-sentidos-de-único)
- [6. Claves, valores y consultas](#6-claves-valores-y-consultas)
- [7. Un comparador puede colapsar claves](#7-un-comparador-puede-colapsar-claves)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Qué aporta una colección frente a un arreglo

Un arreglo conserva una longitud fija. Las colecciones permiten añadir y quitar datos mediante una API común, con estructuras internas diferentes. El tipo `List<Alumno>` expresa las operaciones disponibles y qué objetos contiene; `new ArrayList<>()` decide cómo implementarlas.

| Necesidad | Tipo del capítulo | Propiedad relevante |
| --- | --- | --- |
| Secuencia con índices y duplicados | `ArrayList` | Lectura por índice rápida; insertar en medio desplaza |
| Secuencia y operaciones en extremos | `LinkedList` | Acceder por índice recorre nodos |
| Valores sin duplicados, sin orden garantizado | `HashSet` | Unicidad por igualdad y hash |
| Valores únicos según comparación y ordenados | `TreeSet` | Un resultado de comparación cero implica duplicado |
| Asociación clave → valor | `HashMap` | Claves únicas, sin orden de iteración garantizado |
| Asociación ordenada por claves | `TreeMap` | El comparador decide orden y equivalencia de claves |

`Map` forma parte del framework de colecciones, pero no extiende `Collection`. Tener métodos parecidos no los convierte en la misma interfaz.

### Interfaz de uso y mecanismo concreto

```java
List<Alumno> al = new ArrayList<>();
```

El tipo de la variable es la capacidad requerida —lista de alumnos— y el objeto concreto ofrece esa capacidad mediante un arreglo interno. La creación está vacía; agregar elementos cambia su tamaño lógico sin requerir que el cliente cree manualmente otro arreglo.

## 2. Identidad, igualdad y orden de objetos

En `Alumno`, el orden natural depende de nombre, mientras que igualdad y hash dependen de nombre **y** nota:

```java
return Objects.equals(nombre, alumno.nombre)
        && Objects.equals(nota, alumno.nota);
```

Esta expresión de `equals` significa que dos Lucas con notas 2 y 3 son diferentes. `hashCode` usa los mismos campos. Los comentarios «comparar alumnos por nota» son incompletos: se usan los dos valores. `compareTo`, por otra parte, compara nombres y devuelve cero entre ambos Lucas.

Identidad (`==`), igualdad (`equals`) y orden (`compareTo` o `Comparator`) son decisiones distintas. Dos objetos nuevos pueden ser iguales; dos objetos desiguales pueden comparar como cero con un criterio demasiado limitado. Esta distinción determina la unicidad dentro de los conjuntos.

El caso `nombre == null` devuelve cero desde `compareTo`, lo que no define un orden robusto para todos los nulos. Para las prácticas usa nombres presentes. Modificar nombre o nota después de insertar puede invalidar la organización de una estructura cuyo hash u orden depende de ellos.

## 3. Insertar, reemplazar y eliminar

El ejemplo de lista con arreglo interno empieza vacío y agrega Pato, Cata y Luci. `add(2, Jano)` inserta y mueve a Luci; `set(3, Andres)` reemplaza esa posición sin aumentar tamaño. La secuencia queda `[Pato, Cata, Jano, Andres]`. `remove(new Alumno("Jano", 7))` encuentra por `equals` y la deja con tres elementos.

`contains(new Alumno("Cata", 6))` también usa igualdad lógica. No hace falta pasar la misma referencia que se guardó. `toArray()` devuelve `Object[]`; una adaptación con `toArray(Alumno[]::new)` mantendría el tipo del arreglo.

En listas numéricas, atención a las sobrecargas de `remove`: `remove(2)` elimina el índice 2; `remove(Integer.valueOf(2))` busca el valor 2. El tipo del argumento cambia la operación.

El ejemplo de lista enlazada agrega y consulta ambos extremos con `addFirst`, `addLast`, `getFirst` y `getLast`, luego elimina y reemplaza datos. Un `ListIterator` avanza con `next` y retrocede con `previous`; después de recorrer hacia delante, está al final y puede volver hacia atrás.

`ArrayList.get(i)` es O(1). Su inserción en medio es O(n). `LinkedList` modifica extremos en O(1), pero encontrar una posición arbitraria cuesta O(n). Por eso una lista enlazada no vuelve rápidas todas las inserciones: primero hay que llegar al nodo.

### Insertar y reemplazar tienen efectos distintos

```java
al.add(new Alumno("Luci", 4));
System.out.println("Agregado: Luci con nota 4");
al.add(2, new Alumno("Jano", 7));
System.out.println("Agregado en posición 2: Jano con nota 7");
al.set(3, new Alumno("Andres", 3));
```

Después de agregar Luci, insertar Jano en el índice 2 desplaza a Luci al índice 3. Reemplazar ese índice por Andrés conserva la cantidad de elementos y retira la referencia anterior de esa posición. `add` y `set` no son sinónimos.

```java
ListIterator<Alumno> li = enlazada.listIterator();
while(li.hasNext()){
    Alumno alumno = li.next();
    System.out.println("  -> " + alumno);
}
```

El iterador avanza con `next` solo cuando `hasNext` lo permite. Al completar el recorrido puede desplazarse en sentido inverso mediante `hasPrevious` y `previous`; el cursor conserva su posición entre llamadas.

## 4. Orden natural frente a comparador externo

El ejemplo de ordenamiento de listas muestra:

```java
Collections.sort(sa);
Collections.sort(sa, (a, b) -> a.getNota().compareTo(b.getNota()));
Collections.sort(sa, comparing(Alumno::getNota));
sa.sort(comparing(Alumno::getNombre).reversed());
```

La primera usa `Alumno.compareTo`, por nombre. La segunda y tercera ordenan por nota ascendente con distinta escritura. La última invierte el orden de nombres. `Comparable` pertenece al objeto y define su orden natural; `Comparator` se pasa desde fuera y permite criterios alternativos.

Ordenar una lista no elimina empates. Lucas con notas diferentes sigue apareciendo dos veces. Adaptación didáctica para desempatar: `Comparator.comparing(Alumno::getNota).thenComparing(Alumno::getNombre)`. Un comparador necesita ser consistente y transitivo; no basta con que produzca un resultado en una llamada aislada.

## 5. Dos sentidos de «único»

`HashSet.add` devuelve `true` si cambió el conjunto y `false` si el dato ya existía. El ejemplo de inserción en un conjunto hash aprovecha esa respuesta al repetir `"tres"`; la segunda inserción no cambia tamaño. El orden impreso no se garantiza, aunque parezca estable en una ejecución.

El ejemplo de detección de repetidos conserva una copia de cada especie y señala las repeticiones. Con Corvina, Lenguado, Pejerrey, Robalo, Atún y Lenguado quedan cinco especies distintas. Eso es **deduplicar**.

El ejemplo de valores que aparecen una sola vez hace algo adicional:

```java
for(String pez: peces){
    if(!unicos.add(pez)){
        duplicados.add(pez);
    }
}
unicos.removeAll(duplicados);
```

Primero reúne distintos y repetidos. Después elimina del primer conjunto todo lo que se repitió alguna vez. Quedan solo especies que aparecieron **exactamente una vez**: Corvina, Pejerrey y Robalo. Atún y Lenguado quedan en `duplicados`. No es lo mismo una copia por valor distinto que conservar exclusivamente los valores sin repeticiones.

El ejemplo de unicidad de alumnos utiliza `Alumno`. Lucas 2 y Lucas 3 permanecen porque `equals` distingue notas. El ejemplo de conjunto ordenado ordena cadenas y enteros al revés; el ejemplo de conjunto ordenado por nota usa `comparing(Alumno::getNota).reversed()`, pese a su nombre. En ese conjunto, dos alumnos con **igual nota** serían equivalentes para la inserción y solo uno se conservaría. El criterio de comparación de un árbol determina también su unicidad.

### Detectar un dato ya registrado

```java
Set<String> unicos = new HashSet<>();
for (String pez : peces) {
    if (!unicos.add(pez)) {
        System.out.println("Elemento Duplicado: " + pez);
    }
}
System.out.println(unicos.size() + " elementos no duplicados: " + unicos);
```

El booleano de `add` reúne inserción y detección: falso indica que el conjunto ya tenía un elemento igual. El tamaño final cuenta valores distintos, y no garantiza el orden en que se imprimirán.

### Comparación como criterio de unicidad

```java
Set<Alumno> sa = new TreeSet<>(comparing(Alumno::getNota).reversed());
sa.add(new Alumno("Pato", 5));
sa.add(new Alumno("Cata", 6));
```

El conjunto compara por nota descendente. Estas dos notas son diferentes y ambas entradas se conservan. Si se agregara otro alumno con nota 5, su comparación con Pato daría cero y no produciría una entrada adicional, aunque su nombre fuera distinto.

## 6. Claves, valores y consultas

El ejemplo de asociaciones clave–valor crea `Map<String,Object> persona`, con datos de distinto tipo y un mapa de dirección anidado. Al repetir `put(null, ...)`, la segunda escritura sustituye el valor de la misma clave nula: `HashMap` permite una clave nula, no múltiples entradas distintas con ella.

`put` con clave existente reemplaza; `get` obtiene un valor o nulo. Si una clave admite valores nulos, `get == null` no permite distinguir ausencia de valor nulo: se usa `containsKey`. `getOrDefault` devuelve un valor alternativo cuando la clave no está; el barrio ausente recibe `"La playa"`.

`remove(clave, valor)` elimina solo si coinciden ambos; `replace(clave, anterior, nuevo)` también exige la coincidencia. `containsValue` busca un valor, no una clave. `values`, `keySet` y `entrySet` son vistas del mapa; la última permite recorrer cada pareja directamente, sin una segunda búsqueda.

Como `persona` usa `Object`, obtener un texto o un mapa requiere casts. El compilador no garantiza que la dirección tenga claves y valores de texto; las supresiones de warnings no validan los datos. Una clase de dominio más explícita reduciría esas conversiones, aunque aquí el mapa sirve para explorar la API.

### Reemplazar una asociación y recorrer parejas

```java
persona.put(null, "1234");
persona.put(null, "12345");
persona.put("nombre", "John");
persona.put("apellido", "Doe");
persona.put("apellidoPaterno", "Doe");
persona.put("email", "john.doe@email.com");
persona.put("edad", 30);
```

Las dos asignaciones a la clave nula representan una sola asociación: la segunda sustituye el valor por `12345`. Las otras claves identifican datos independientes, y repetir una de ellas sustituiría igualmente su valor.

```java
// Recorrido reducido de las asociaciones del ejemplo.
for (Map.Entry<String, Object> par : persona.entrySet()) {
    System.out.println(par.getKey() + " => " + par.getValue());
}
```

Cada entrada suministra clave y valor juntos. Como el tipo de valor es `Object`, el mapa puede contener edad, texto o una dirección anidada; una operación específica sobre ese valor necesita comprobar o convertir su tipo.

## 7. Un comparador puede colapsar claves

En el ejemplo de mapa ordenado aparece:

```java
Map<String, Object> persona = new TreeMap<>(Comparator.comparing(String::length).reversed());
```

El comentario dice orden natural, pero la instrucción ordena por **longitud descendente**. Además, `"estado"`, `"ciudad"` y `"numero"` tendrían igual longitud para ese criterio. Dos claves que comparan cero se consideran la misma clave en un `TreeMap`, aunque `equals` diga que son distintas. La API oficial describe esa equivalencia en [TreeMap de Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/TreeMap.html).

El mapa exterior del ejemplo tiene claves de longitudes distintas, así no muestra una colisión en esos datos concretos. El mapa interior usa `new TreeMap<>()` y orden natural, por lo que conserva las claves de dirección aunque midan igual. Para mantener claves distintas con orden por longitud, una **adaptación didáctica** sería:

```java
Comparator<String> orden = Comparator.comparingInt(String::length)
        .reversed()
        .thenComparing(Comparator.naturalOrder());
Map<String, Object> persona = new TreeMap<>(orden);
```

El desempate compara el contenido, separando textos distintos de la misma longitud. No es una modificación del fuente original.

## Preguntas de repaso

1. ¿`set` y `add(indice, ...)` hacen lo mismo en una lista? **No: reemplazar no aumenta tamaño; insertar sí.**
2. ¿Dos alumnos con igual nombre y distinta nota son iguales aquí? **No según `equals`; pueden comparar cero por nombre.**
3. ¿Un árbol ordenado consulta exclusivamente `equals` para deduplicar? **No: utiliza su comparación.**
4. ¿`HashMap` y `HashSet` prometen el orden de impresión? **No.**
