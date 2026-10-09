# 11 · Anotaciones y reflection: metadatos que un procesador interpreta

## Contenido

- [1. Una anotación describe una intención](#1-una-anotación-describe-una-intención)
- [2. Definir alcance y retención](#2-definir-alcance-y-retención)
- [3. Elegir qué campos se exportan](#3-elegir-qué-campos-se-exportan)
- [4. Inspeccionar una clase en ejecución](#4-inspeccionar-una-clase-en-ejecución)
- [5. Seguir un resultado de extremo a extremo](#5-seguir-un-resultado-de-extremo-a-extremo)
- [6. Límites de la implementación](#6-límites-de-la-implementación)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Una anotación describe una intención

Una anotación añade metadatos a una declaración. No es por sí misma una llamada a un método ni ejecuta mágicamente una tarea. Puede ser comprobada por el compilador, consultada por herramientas o leída por código de ejecución. El módulo construye un procesador que convierte objetos a un texto de apariencia JSON según esas marcas.

El ejemplo de anotaciones del JDK muestra cuatro usos habituales: `@Override` comprueba sobrescritura; `@Deprecated` indica una API desaconsejada; `@SuppressWarnings` silencia avisos concretos; `@FunctionalInterface` exige un contrato funcional válido. Suprimir un aviso no elimina el problema que lo produjo ni valida una conversión insegura.

### Un contrato comprobado por el compilador

```java
@FunctionalInterface
public interface Operacion {
    double calcular(double a, double b);
}
```

La anotación exige que la interfaz mantenga un único método abstracto funcional. Los dos `double` son sus entradas y el otro `double`, el resultado. Añadir otra operación abstracta incompatible haría fallar la comprobación aunque ninguna lambda se ejecutara todavía.

## 2. Definir alcance y retención

`JsonAtributo` declara:

```java
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAtributo {
    String nombre()       default "";
    boolean capitalizar() default false;
}
```

`@interface` define un tipo de anotación. `nombre` y `capitalizar` son elementos cuyos valores se suministran al usarla. `default` permite omitirlos. No se invocan como métodos de negocio del estudiante: el procesador consulta el objeto de metadatos para obtener esos valores.

`Target(FIELD)` limita su uso a campos. `Retention(RUNTIME)` permite verla por reflection al ejecutar. `Documented` permite incluir su uso en Javadoc. La anotación `Init` no tiene elementos y marca métodos: su presencia significa para **este procesador** que deben ejecutarse antes de leer los campos.

| Retención | Dónde se conserva | ¿La lee reflection en ejecución? |
| --- | --- | --- |
| `SOURCE` | Fuente durante compilación | No |
| `CLASS` | Bytecode, valor predeterminado | No como anotación runtime |
| `RUNTIME` | Bytecode y disponible al ejecutar | Sí |

Cambiar una anotación a `CLASS` puede hacer que compile y que el procesador deje de encontrarla. La marca existe en otro nivel del ciclo de vida, pero ya no satisface la forma de lectura usada aquí.

### Una marca de inicialización sin argumentos

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Init {
}
```

El cuerpo de la anotación está vacío: no declara datos propios. `METHOD` limita el lugar de uso y `RUNTIME` conserva la marca para inspección durante ejecución. Su significado concreto depende de que un procesador decida buscarla e invocar el método marcado.

## 3. Elegir qué campos se exportan

`Estudiante` marca nombre, edad, carrera y promedio. Nombre y carrera se capitalizan; edad y promedio cambian sus claves a `"edad_años"` y `"promedio_gpa"`. Fecha de nacimiento y país no están anotados y se omiten.

El método privado `normalizar` tiene `@Init`. Divide el nombre en palabras y pone inicial mayúscula y resto minúsculo. Ser privado no impide que el procesador del ejemplo lo invoque, porque solicita acceso mediante reflection.

Esta política es optativa por campo: solo aparece lo marcado. No hay una regla automática que lea todos los getters ni una conversión automática de fechas. Que el modelo tenga un getter no cambia qué campos selecciona el procesador.

### Selección explícita de campos

```java
@JsonAtributo(capitalizar = true)           // clave = "nombre" (nombre del campo), aplica TitleCase
private String nombre;

@JsonAtributo(nombre = "edad_años")         // clave personalizada en el JSON
private int edad;

@JsonAtributo(nombre = "carrera", capitalizar = true)
private String carrera;

@JsonAtributo(nombre = "promedio_gpa")
private double promedio;
```

Las marcas están en los campos y no en los getters. Los elementos de cada anotación configuran inclusión, nombre y transformación. Un campo sin `nombre` explícito conserva su nombre Java como clave; uno sin la marca se excluye bajo la regla del procesador.

```java
private void normalizar() {
    if (nombre != null) {
        this.nombre = Arrays.stream(nombre.split(" "))
                .map(p -> p.substring(0, 1).toUpperCase() + p.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
```

La operación divide el nombre, transforma cada palabra y vuelve a unirlas. El control de nulo evita intentar dividir una referencia ausente, pero no evita palabras vacías producidas por ciertos espacios. La marca `@Init` que acompaña al método en el modelo es lo que permite al procesador seleccionarlo.

## 4. Inspeccionar una clase en ejecución

Reflection permite obtener la clase real con `getClass`, inspeccionar campos y métodos y operar sobre ellos. `JsonSerializador` trabaja sobre `Object`: decide qué hacer según la clase encontrada, en vez de llamar getters de `Estudiante` de forma fija.

El flujo completo es:

1. Rechazar objeto nulo con `AnotacionException`.
2. Buscar métodos declarados con `@Init` y ejecutarlos.
3. Buscar campos declarados con `@JsonAtributo`.
4. Leer metadatos, elegir nombre de clave y obtener valor.
5. Capitalizar si corresponde y construir los pares de texto.
6. Unir pares con comas entre llaves.

La inicialización utiliza `getDeclaredMethods`, `isAnnotationPresent`, `setAccessible(true)` y `invoke(objeto)`. La lectura utiliza equivalentes de campos:

```java
f.setAccessible(true);
JsonAtributo meta = f.getAnnotation(JsonAtributo.class);
String clave = meta.nombre().isEmpty() ? f.getName() : meta.nombre();
Object valor = f.get(objeto);
```

La clave usa el nombre original cuando el elemento `nombre` está vacío. `f.get` obtiene el valor del objeto concreto y envuelve primitivos en objetos. Si la marca pide capitalización y es texto, el procesador cambia el valor **y lo escribe de vuelta** con `f.set`: serializar aquí muta el estudiante.

`AnotacionException` extiende `RuntimeException`; no exige un `catch` obligatorio en el cliente. Los errores de invocación y acceso se convierten en esa excepción con mensaje. El constructor actual no conserva una causa encadenada, lo que limita el contexto disponible para depurar.

### Descubrir y ejecutar inicialización

```java
Method[] metodos = objeto.getClass().getDeclaredMethods();
Arrays.stream(metodos)
        .filter(m -> m.isAnnotationPresent(Init.class))
        .forEach(m -> {
            m.setAccessible(true);  // permite invocar métodos private
            try {
                m.invoke(objeto);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AnotacionException("Error al invocar @Init: " + e.getMessage());
            }
        });
```

La consulta obtiene métodos declarados y el filtro conserva solo los anotados. `setAccessible` solicita acceso al método privado y `invoke` lo llama sobre el objeto suministrado. Un error del método invocado puede llegar envuelto en `InvocationTargetException`, y la captura del ejemplo lo transforma en un error del procesamiento.

### Consultar y modificar un campo desde sus metadatos

```java
if (meta.capitalizar() && valor instanceof String) {
    String s = (String) valor;
    valor = Arrays.stream(s.split(" "))
            .map(p -> p.substring(0, 1).toUpperCase() + p.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));
    f.set(objeto, valor);   // actualizar el campo
}
```

La condición exige tanto configuración de capitalización como un valor de texto. Tras transformar, `f.set` modifica el campo del objeto original. Esto distingue el procesador mostrado de una conversión puramente de lectura: cambia estado además de construir una representación.

## 5. Seguir un resultado de extremo a extremo

El procesamiento de anotaciones crea un nombre en minúsculas, edad 21, carrera en minúsculas y promedio 9.1. `@Init` normaliza nombre; luego la capitalización de campos normaliza carrera y nombre. El resultado esperado tiene estos pares, sin exigir orden de reflection:

```json
{
  "nombre": "Felipe Andres Perez",
  "edad_años": "21",
  "carrera": "Ciencias De La Computacion",
  "promedio_gpa": "9.1"
}
```

El procesador imprime una sola línea; aquí se presenta con saltos para facilitar lectura. Los valores `"21"` y `"9.1"` son **cadenas JSON**, no números JSON. País y fecha no aparecen. Después de procesar, los campos capitalizados del objeto también han cambiado.

## 6. Límites de la implementación

`getDeclaredFields` y `getDeclaredMethods` inspeccionan lo declarado en la clase concreta, no todos los miembros heredados. Tampoco prometen el orden en que aparecerán. Al extender el modelo o agregar varios métodos `@Init`, la implementación no define un orden de inicialización ni recorre automáticamente padres.

El formato se construye con `"\"" + clave + "\":\"" + valor + "\""`: convierte todo a texto entre comillas, incluso nulo como `"null"`. No escapa comillas, barras invertidas o saltos de línea del contenido. Por eso un valor como `Ana "A"` puede producir texto JSON inválido. No procesa recursivamente listas u objetos anidados. Es una práctica de metadatos, no un serializador JSON general.

La capitalización usa `split(" ")` y `substring(0,1)`. Palabras vacías por espacios consecutivos, un nombre vacío o espacios iniciales pueden fallar. `@Init` tampoco valida que el método tenga cero parámetros o una firma apropiada antes de invocarlo. `setAccessible(true)` solicita omitir ciertos controles de acceso, pero el sistema de módulos puede rechazarlo en contextos restringidos.

Estos límites explican por qué los procesadores reales necesitan reglas de tipos, escape, validación y manejo de errores. La implementación presentada no incluye todas esas responsabilidades.

### Formato de salida y significado de los tipos

```java
return "\"" + clave + "\":\"" + valor + "\"";
```

El operador de concatenación convierte `valor` a texto y siempre lo coloca entre comillas. Por tanto, un entero como 21 se representa como una cadena JSON. Para un nulo se genera la cadena `"null"`, que tampoco equivale al literal JSON `null`.

```text
Valor Java: 21
Salida de esta regla: "edad_años":"21"
Número JSON: "edad_años":21
```

Distinguir estas representaciones importa cuando un consumidor necesita operar numéricamente. Construir JSON mediante concatenación también requiere reglas de escape; el contenido que ya tenga comillas o saltos de línea no queda protegido por añadir únicamente las comillas exteriores.

## Preguntas de repaso

1. ¿La anotación ejecuta `normalizar` por sí sola? **No; lo ejecuta el procesador que busca `@Init`.**
2. ¿Qué hace falta para leer la marca en ejecución? **Retención `RUNTIME`.**
3. ¿Se exportan todos los campos privados? **No; únicamente los seleccionados por la anotación.**
4. ¿El objeto permanece intacto? **No; `@Init` y la capitalización pueden modificarlo.**
5. ¿`"21"` y `21` son el mismo tipo JSON? **No; el primero es texto y el segundo número.**
