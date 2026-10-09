# 09 · Optional: hacer explícita la ausencia

## Contenido

- [1. Una búsqueda puede no tener respuesta](#1-una-búsqueda-puede-no-tener-respuesta)
- [2. Consumir sin extraer manualmente](#2-consumir-sin-extraer-manualmente)
- [3. Expresar ausencia en la firma](#3-expresar-ausencia-en-la-firma)
- [4. `orElse`, `orElseGet` y `orElseThrow`](#4-orelse-orelseget-y-orelsethrow)
- [5. `map` y `filter`: conservar la estructura de ausencia](#5-map-y-filter-conservar-la-estructura-de-ausencia)
- [6. Navegar datos opcionales con flatMap](#6-navegar-datos-opcionales-con-flatmap)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Una búsqueda puede no tener respuesta

Una búsqueda no siempre encuentra datos. Si retorna nulo, el cliente tiene que recordar comprobarlo antes de llamar métodos. `Optional<T>` representa explícitamente dos estados: existe un T o no hay valor. No representa una lista de varios elementos ni elimina automáticamente todos los errores de nulos.

El ejemplo de creación de valores opcionales muestra tres formas de crearlo:

```java
Optional<String> opt1 = Optional.of("Felipe");
String beca = null;
Optional<String> opt2 = Optional.ofNullable(beca);
Optional<String> opt3 = Optional.empty();
```

`of` exige un valor no nulo y falla si recibe nulo. `ofNullable` transforma nulo en vacío. `empty` declara ausencia directamente. El objeto Optional mismo debe existir: devolver `null` en vez de `Optional.empty()` vuelve a introducir la ambigüedad.

`isPresent` e `isEmpty` consultan el estado. `get` extrae el valor o lanza `NoSuchElementException`; usarlo sin una garantía de presencia cambia una excepción posible por otra. El ejemplo lo usa dentro de una comprobación y después demuestra alternativas más expresivas.

## 2. Consumir sin extraer manualmente

`ifPresent(consumer)` realiza una acción solo si hay valor. `ifPresentOrElse(consumer,runnable)` añade la acción del vacío. En el ejemplo, `opt1` saluda a Felipe, mientras que `opt2` informa que no tiene beca.

La acción presente recibe el valor. La alternativa no lo recibe porque precisamente no existe. Estos métodos no producen el texto como retorno: ejecutan acciones. Obtener un texto o un objeto final requiere transformar el valor y aplicar una política como `orElse`.

### Acciones según presencia o ausencia

```java
opt2.ifPresentOrElse(
        b  -> System.out.println("Beca: " + b),
        () -> System.out.println("Felipe no tiene beca asignada aún")
);
```

La acción de presencia recibe la beca; la alternativa no recibe argumentos. Cuando el valor original de beca es nulo, `ofNullable` crea un contenedor vacío y se ejecuta solamente la segunda acción. No se extrae un valor inexistente antes de decidir la rama.

## 3. Expresar ausencia en la firma

`Repositorio` declara `Optional<T> buscarPorNombre(String nombre)`. El repositorio decide si encuentra; el cliente decide qué hacer cuando no encuentra. `MateriaRepositorio` tiene cuatro materias iniciales:

| Materia | Nota | Docente | Departamento |
| --- | --- | --- | --- |
| Algoritmos | 9.1 | Dr. Rivera | Ciencias de la Computación |
| Redes | 8.5 | Ing. Paredes | Ausente |
| Inglés Técnico | 8.0 | Ausente | Ausente |
| Cálculo I | 7.8 | Ausente | Ausente |

La búsqueda del fuente es:

```java
return datos.stream()
        .filter(m -> m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
        .findFirst();
```

Es parcial y sensible a acentos, aunque ignora mayúsculas mediante conversión. `"red"` encuentra Redes; `"cálculo"` encuentra Cálculo I; `"calculo"` no. Una cadena vacía coincide con cualquier nombre y devuelve la primera materia. Un argumento nulo provoca error antes de producir Optional. La firma describe ausencia del resultado, no entrada inválida.

### Ausencia individual en un modelo

```java
public Optional<String>  getDescripcion() { return Optional.ofNullable(descripcion); }
public Optional<Docente> getDocente()     { return Optional.ofNullable(docente); }
```

Los campos pueden estar en nulo internamente, pero los getters convierten ese estado a ausencia explícita. La descripción y el docente son independientes: una materia puede tener uno sin el otro. La política del consumidor se aplica a cada Optional retornado.

## 4. `orElse`, `orElseGet` y `orElseThrow`

En el ejemplo de valores alternativos y excepciones:

```java
Materia m1 = repo.buscarPorNombre("algoritmos").orElse(new Materia("Materia no encontrada", 0));
Materia m3 = repo.buscarPorNombre("redes").orElseGet(EjemploOrElse::materiaDefecto);
```

Java evalúa los argumentos de una llamada antes de invocarla. Por eso el `new Materia(...)` de `orElse` se ejecuta incluso si Algoritmos existe; después se devuelve la materia encontrada y se descarta el objeto alternativo.

`orElseGet` recibe un `Supplier`, no un valor ya calculado. El método solo llama al proveedor si está vacío. Para Redes no aparece el mensaje de `materiaDefecto`; para Química sí. Esto importa cuando construir la alternativa tiene costo o efectos externos.

`orElseThrow()` expresa que la ausencia impide continuar y lanza `NoSuchElementException`. La variante con supplier permite una excepción contextual, como `IllegalArgumentException("Materia no encontrada...")`. Elige según la regla del caso: un valor de recuperación, una acción o un error; no escondas automáticamente toda ausencia bajo un dato ficticio.

**Discrepancia ejecutable:** la implementación busca `"ingles"` sin tilde y comenta «encontrada». El nombre almacenado contiene `"Inglés"`, así el Optional queda vacío y `orElseThrow()` lanza `NoSuchElementException`. El `catch` dla implementación solo captura `IllegalArgumentException`, que no es su clase padre, por lo que el programa se interrumpe y no llega a buscar Filosofía. Una adaptación del flujo previsto consiste en buscar `"inglés"`. Esta explicación no modifica el código original.

### Creación diferida de una alternativa

```java
public static Materia materiaDefecto() {
    System.out.println("  [creando materia por defecto...]");
    return new Materia("Sin asignar", 0.0);
}
```

El mensaje hace visible cuándo se ejecuta la fábrica. Al pasar su referencia a `orElseGet`, esta llamada solo ocurre si no hubo materia. Con `orElse(materiaDefecto())`, se ejecutaría siempre antes de que Optional decida qué devolver.

```java
// Adaptación con el nombre acentuado para expresar la búsqueda prevista.
Materia materia = repo.buscarPorNombre("inglés")
        .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
```

La fábrica de excepción no se ejecuta si la materia existe. Cuando la búsqueda queda vacía, proporciona el error que detiene el flujo. El nombre acentuado es necesario porque la regla de búsqueda normaliza mayúsculas, pero no elimina tildes.

## 5. `map` y `filter`: conservar la estructura de ausencia

El ejemplo de transformación y filtrado opcional transforma Redes con `map(m -> m.getNombre().toUpperCase())` y obtiene `"REDES"`. Si no hubiera materia, la función no se ejecutaría y seguiría vacío. `map` cambia un valor T a R, conservando el contenedor; si la función retorna nulo, su resultado es vacío.

`filter(m -> m.getNota() >= 7)` conserva Algoritmos. Filtrar Cálculo por nota ≥10 produce vacío. Después del filtro ya no distingues «materia inexistente» de «materia existente que no pasó»: ambas situaciones tienen el mismo estado vacío. Si esa diferencia es importante, conserva el resultado original o representa la causa de otra forma.

Optional no captura excepciones de las funciones: si dentro de `map` o `filter` la función falla, el error se propaga. Tampoco hace procesamiento asíncrono ni funciona como un stream perezoso: sus operaciones actúan sobre su valor presente al llamarlas.

### Transformación sin reemplazar la política del vacío

```java
// Transformación del nombre de una materia encontrada.
String nombreMayus = repo.buscarPorNombre("redes")
        .map(m -> m.getNombre().toUpperCase())
        .orElse("no encontrada");
```

El valor pasa de materia a texto en mayúsculas. Si existe Redes, el resultado es `REDES`; si no existe, `map` conserva el vacío y `orElse` suministra el texto alternativo. No se crea una materia ficticia para poder acceder a su nombre.

### Descartar un resultado ya encontrado

```java
repo.buscarPorNombre("algoritmos")
        .filter(m -> m.getNota() >= 7.0)
        .ifPresentOrElse(
                m  -> System.out.println("Materia aprobada: " + m),
                () -> System.out.println("Materia reprobada o no encontrada")
        );
```

La presencia de una materia no garantiza presencia después del filtro. El predicado exige nota suficiente, y `ifPresentOrElse` describe el estado resultante. Tanto una ausencia inicial como una nota rechazada llevan a la acción de vacío.

## 6. Navegar datos opcionales con flatMap

`Materia.getDocente()` retorna `Optional<Docente>` y `Docente.getDepartamento()` retorna `Optional<String>`. Si aplicas `map(Materia::getDocente)` al Optional de materia, terminas con `Optional<Optional<Docente>>`: una capa adicional que no resuelve la navegación.

Fragmento del ejemplo de transformación y filtrado opcional:

```java
String departamento = repo.buscarPorNombre("algoritmos")
        .flatMap(Materia::getDocente)
        .flatMap(com.cultodeportivo.modelo.Docente::getDepartamento)
        .map(String::toUpperCase)
        .orElse("Departamento no asignado");
```

Traza por tipos: `Optional<Materia> → Optional<Docente> → Optional<String> → String`. `flatMap` recibe una función que ya retorna Optional y conserva una única capa. En Algoritmos resulta `"CIENCIAS DE LA COMPUTACIÓN"`. Redes pasa el primer nivel, pero su departamento está vacío. Inglés no pasa siquiera al docente; las funciones posteriores no se ejecutan.

El mensaje «Sin docente asignado» del caso Inglés se obtiene para cualquier vacío de esa cadena, incluso si faltara la materia o el departamento; es una etiqueta de recuperación, no una comprobación exclusiva de docente.

La extracción de extensión aplica `ofNullable`, verifica que exista punto y usa el último: `"tesis_felipe_ecuador.pdf"` produce `"PDF"`. Si termina en punto, la extensión queda como cadena vacía; tener punto no garantiza una extensión con contenido.

### Acceso a un departamento opcional

```java
public Optional<String> getDepartamento() { return Optional.ofNullable(departamento); }
public void setDepartamento(String departamento) { this.departamento = departamento; }
```

El getter devuelve una única capa opcional. La cadena que usa `flatMap` consume ese contrato directamente: si el docente existe pero su departamento no, el resultado queda vacío y las transformaciones posteriores se omiten. El setter permite cambiar el campo sin convertir una ausencia en un texto ficticio.

## Preguntas de repaso

El ejemplo de valores alternativos y excepciones conserva la excepción descrita mientras su búsqueda no cambie.

1. ¿`of(null)` representa vacío? **No; lanza. `ofNullable` representa el nulo como vacío.**
2. ¿Por qué `orElse(new ...)` crea aunque exista valor? **Porque se evalúa el argumento antes de llamar.**
3. ¿Cuándo usar `flatMap`? **Cuando la función ya devuelve Optional.**
4. ¿`filter` permite distinguir inexistencia y rechazo? **No; ambos pueden producir vacío.**
