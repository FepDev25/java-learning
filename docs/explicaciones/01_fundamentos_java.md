# 01 · Fundamentos de Java

## Contenido

- [Tipos, expresiones y control de ejecución](#tipos-expresiones-y-control-de-ejecución)
- [1. De un archivo a un programa](#1-de-un-archivo-a-un-programa)
- [2. Evaluar una expresión](#2-evaluar-una-expresión)
- [3. Elegir el camino](#3-elegir-el-camino)
- [4. Repetir manteniendo una condición](#4-repetir-manteniendo-una-condición)
- [5. Texto, igualdad e inmutabilidad](#5-texto-igualdad-e-inmutabilidad)
- [6. Un número como objeto](#6-un-número-como-objeto)
- [7. Java siempre pasa por valor](#7-java-siempre-pasa-por-valor)
- [8. Datos externos y errores](#8-datos-externos-y-errores)
- [9. Fechas clásicas, utilidades e introspección](#9-fechas-clásicas-utilidades-e-introspección)
- [10. Programa, JVM y sistema operativo](#10-programa-jvm-y-sistema-operativo)
- [Preguntas de repaso](#preguntas-de-repaso)

## Tipos, expresiones y control de ejecución

Los fundamentos del lenguaje permiten representar datos, evaluar expresiones y controlar el orden de ejecución. El estado de una variable se sigue desde su declaración hasta sus modificaciones, mientras que las condiciones determinan qué ramas y repeticiones se ejecutan.

El desarrollo parte de tipos y expresiones, continúa con decisiones y repeticiones, y aplica esas bases al manejo de texto, conversiones, entrada, fechas y entorno.

## 1. De un archivo a un programa

La estructura inicial del programa es:

```java
public static void main(String[] args) {
    System.out.println("Hola Mundo desde Java");

    String nombre = "Felipe";
    int edad = 20;
    System.out.println("Hola, mi nombre es " + nombre + " y tengo " + edad + " años.");
}
```

Una clase agrupa definiciones; el método `main` es el punto de entrada usado aquí. `public` permite acceder al método, `static` permite invocarlo sin crear un objeto de la clase y `void` significa que no devuelve un resultado. `String[] args` contiene los argumentos enviados al iniciar el programa. Las llaves delimitan bloques y el punto y coma termina una instrucción.

`String nombre` declara una variable cuyo tipo es texto; `=` guarda una referencia al valor asignado. `int edad` guarda un entero. Java verifica los tipos antes de ejecutar: no se puede asignar directamente `"veinte"` a un `int`. `println` imprime y termina la línea. Al intervenir una cadena, `+` concatena representaciones de los valores.

Las clases `PrimitivosEnteros`, `PrimitivosFlotantes`, `PrimitivosCaracteres` y `PrimitivosBoolean` exploran los ocho tipos primitivos:

| Tipo | Tamaño definido para el valor | Qué representa |
| --- | --- | --- |
| `byte` | 8 bits | Enteros de −128 a 127 |
| `short` | 16 bits | Enteros de −32768 a 32767 |
| `int` | 32 bits | Enteros de −2³¹ a 2³¹−1 |
| `long` | 64 bits | Enteros de −2⁶³ a 2⁶³−1 |
| `float` | 32 bits | Números de punto flotante con precisión limitada |
| `double` | 64 bits | Punto flotante con más precisión que `float` |
| `char` | 16 bits | Una unidad UTF-16, como `'@'` |
| `boolean` | Sin tamaño de almacenamiento fijado por el lenguaje | `true` o `false` |

`9223372036854775807L` necesita el sufijo `L`; `3.1416f` necesita `f` porque un literal decimal ordinario es `double`. Un `char` usa comillas simples; un `String`, dobles. `char` no equivale siempre a un carácter visual completo: algunos símbolos Unicode requieren dos unidades UTF-16. `\n` representa salto de línea y `\t`, tabulación.

`var enteroVar = 12` permite inferir `int` para una variable local. No vuelve dinámico su tipo: después no admite una cadena.

**Precisión y límites.** `Float.MIN_VALUE` y `Double.MIN_VALUE`, impresos por el ejemplo, son el menor valor **positivo** distinto de cero representable, no el número más negativo. Los decimales binarios son aproximados; convertir `float` a `double` no recupera precisión que ya se perdió. En enteros, superar el rango puede desbordar sin lanzar una excepción automática.

En `ConversionDeTipos` aparecen tres operaciones diferentes:

```java
int numeroInt = 100;
long numeroLong = numeroInt;

float numeroFloat = 13.4f;
double numeroDouble = numeroFloat;

numeroDouble = 99.99;
int numeroIntFromDouble = (int) numeroDouble;

String numeroStr = "123";
int numeroFromString = Integer.parseInt(numeroStr);
```

La ampliación `int → long` preserva ese entero. El cast `(int)` elimina la parte fraccionaria: resulta `99`, no `100`. `parseInt` interpreta texto y puede lanzar `NumberFormatException`; no es un cast entre objetos. `Boolean.parseBoolean` solo produce `true` para texto igual a `"true"` ignorando mayúsculas; otros textos producen `false`.

`SistemasNumericos` muestra que `0b11110`, `036` y `0x1e` representan el mismo entero decimal: `30`. La base pertenece a la escritura del literal, no a un tipo distinto. `Integer.toBinaryString` devuelve una **cadena** con su representación. `random.nextInt(100)` produce de 0 a 99, por eso esa parte cambia entre ejecuciones.

### Representación de números y caracteres

Los literales permiten escribir el mismo entero en diferentes bases. La conversión a texto cambia la representación, mientras que el valor numérico continúa siendo el mismo:

```java
int numeroBinario = 0b11110;
System.out.println("Numero binario: " + numeroBinario);

int numeroOctal = 036;
System.out.println("Numero octal: " + numeroOctal);

int numeroHexadecimal = 0x1e;
System.out.println("Numero hexadecimal: " + numeroHexadecimal);
```

Las tres impresiones producen 30. Un cero inicial identifica un literal octal; por eso `036` no representa el entero decimal 36. Los prefijos `0b` y `0x` expresan binario y hexadecimal respectivamente.

Un carácter también puede escribirse mediante su código Unicode o un entero constante compatible:

```java
char caracter = '\u0040'; // @
char decimal = 64; // @
System.out.println("caracter = " + caracter);
System.out.println("decimal = " + decimal);
System.out.println("decimal = caracter: " + (decimal == caracter));
```

Los dos valores representan `@`, y la comparación produce verdadero. La escritura Unicode permite expresar una unidad de texto aunque no se escriba directamente su símbolo.

## 2. Evaluar una expresión

Los operadores aritméticos combinan números: `+`, `-`, `*`, `/` y `%`. El módulo `%` devuelve el resto y permite reconocer pares con `n % 2 == 0`. La división entre enteros descarta la fracción: `5 / 2` vale `2`. Para obtener `2.5` al menos un operando debe ser decimal, como `(double) 5 / 2`.

La asignación compuesta `x += y` acumula; los unarios `-x` y `+x` actúan sobre un solo valor. Los relacionales (`<`, `>=`, `==`, `!=`) producen booleanos. En `OperadoresIncrementales`:

```java
int i = 1;
int j = ++i;

i = 2;
j = i++;
```

En el primer caso se incrementa y luego se obtiene el valor: `i=2`, `j=2`. En el segundo se obtiene el valor anterior y luego se incrementa: `i=3`, `j=2`. `--` funciona de forma análoga. Combinar varios incrementos en una expresión dificulta seguir el estado; separar los pasos facilita comprobar el resultado.

`&&` exige dos condiciones verdaderas y deja de evaluar si la primera es falsa. `||` acepta cualquiera y deja de evaluar si la primera es verdadera. Esa evaluación con cortocircuito permite proteger accesos: en la adaptación `texto != null && !texto.isEmpty()`, la segunda parte no se invoca si no hay objeto. `!` invierte el booleano.

`PrecedenciaOperadores` ayuda a separar precedencia de intuición: multiplicación y división se agrupan antes que suma; los paréntesis hacen explícita otra agrupación. `"Total: " + 2 + 3` produce `"Total: 23"`, mientras que `"Total: " + (2 + 3)` produce `"Total: 5"`.

`OperadoresLogicosLogin` recorre dos arreglos paralelos: el usuario y la contraseña de la misma posición forman un par. Su ternario conserva `esAutenticado` cuando un par no coincide; no borra un éxito anterior. Es una práctica de lógica y recorrido, no un sistema real de autenticación.

### Evaluación lógica de una credencial

La condición compara el usuario y la contraseña correspondientes a la misma posición:

```java
for(int i = 0; i < usernames.length; i++){
    esAutenticado = (usernames[i].equals(u) && passwords[i].equals(p))? true: esAutenticado;
}
```

`&&` impide considerar correcto un usuario cuya contraseña no coincide. El ternario mantiene un éxito anterior cuando el par actual no coincide. El recorrido completo se utiliza para reconocer al menos un par válido.

### Agrupación antes de concatenar

```java
int pago1 = 156;
int pago2 = 155;
System.out.println("El pago total es de: " + (pago1 + pago2));
```

Los paréntesis hacen que primero se sumen 156 y 155: el total numérico es 311. Después se concatena su representación al texto. Sin esa agrupación, el operador `+` pasaría a concatenar después de encontrar el primer operando de tipo `String`.

## 3. Elegir el camino

Un `if` ejecuta su bloque si la condición es verdadera. En una cadena `if / else if / else` se ejecuta la primera rama que coincide. `SentenciaIfElse` ordena los umbrales de promedio de mayor a menor; si se colocara primero `promedio >= 4.0`, un `6.5` entraría ahí antes de llegar al caso excelente.

El ternario `condición ? valorSiTrue : valorSiFalse` es una expresión que produce un valor. `OperadorTernario` calcula el promedio y asigna `"Aprobado"` desde `5.49`. `OperadorTernarioNumeroMayor` compara **cuatro** números: conserva el mayor acumulado y lo enfrenta al siguiente. No lee solo dos números, pese a lo que podría esperarse de un resumen corto.

`SentenciaSwitchCase` utiliza tanto una expresión `switch` que devuelve el nombre del mes como ramas con `->` que imprimen resultados. Con esta sintaxis no hay caída automática a la rama siguiente. `default` maneja valores sin coincidencia.

Los dos ejemplos de días del mes aplican la regla de bisiesto: divisible por 400, o divisible por 4 pero no por 100. Así, 2000 es bisiesto y 1900 no. Un mes fuera de 1–12 produce cero en estos ejercicios; eso es una decisión de la implementación, no una fecha válida. El `catch` vacío de `SentenciaIfElseNumDiasMes` oculta entradas inválidas, por lo que no es una pauta recomendable para informar errores.

### Elegir un valor con una expresión switch

```java
// Reducción del switch de nombres de mes.
String nombreMes = switch (mes) {
    case 1 -> "Enero";
    case 2 -> "Febrero";
    case 3 -> "Marzo";
    default -> "Otro mes o valor no contemplado";
};
```

Cada rama produce un valor del tipo requerido por la asignación. Las flechas separan alternativas sin caída a la rama siguiente, y el punto y coma termina la asignación completa. `default` mantiene un resultado para los valores no incluidos en esta versión reducida.

### Composición de decisiones

La regla de bisiesto combina comparaciones de restos con operadores booleanos:

```java
if (anio % 400 == 0 || ((anio % 4 == 0) && !(anio % 100 == 0))) {
    numeroDias = 29;
} else {
    numeroDias = 28;
}
```

El fragmento corresponde a la rama de febrero. Divisibilidad por 400 basta para elegir 29 días. En los demás casos, se necesita divisibilidad por 4 y ausencia de divisibilidad por 100. Para 1900 la segunda combinación es falsa y se eligen 28 días.

Un valor calculado también puede elegirse con un ternario:

```java
promedio = (matematicas + ciencias + historia) / 3;
System.out.println("promedio = " + promedio);

estado = promedio >= 5.49 ? "Aprobado" : "Rechazado";
```

El promedio es la media de tres notas. La expresión condicional produce una cadena: la rama verdadera devuelve `"Aprobado"` y la falsa `"Rechazado"`. Su umbral de 5.49 es una regla concreta de este ejemplo, no una propiedad del lenguaje.

## 4. Repetir manteniendo una condición

`for (inicio; condición; actualización)` inicializa una vez, comprueba antes de cada vuelta y actualiza al finalizarla. `while` también comprueba antes; `do / while` ejecuta el cuerpo al menos una vez. Esa diferencia importa si la condición empieza siendo falsa.

`SentenciaFor` imprime secuencias ascendentes, descendentes y pares. `continue` abandona la vuelta actual y pasa a la actualización del `for`; `break` termina el bucle. `SentenciaForEach` recibe los valores de un arreglo sin exponer el índice. Es útil para leerlos, pero asignar otra cosa a la variable local del recorrido no reemplaza la posición del arreglo.

`SentenciaForArreglo` combina `contains`, `equalsIgnoreCase`, búsqueda y ventanas de `JOptionPane`. Se detiene al encontrar el nombre. `SentenciasBucleEtiquetas` agrega etiquetas: `continue bucle` avanza el bucle externo y `break etiqueta` termina el bloque etiquetado. Una etiqueta no es una llamada a un método ni un salto arbitrario.

La búsqueda con etiquetas se desarrolla mediante los recorridos completos de matrices; una clase vacía no aporta una implementación de búsqueda.

### Comprobar antes o después del cuerpo

```java
// Versión reducida de los bucles de condición.
int i = 0;
while (i <= 5) {
    System.out.println("i = " + i);
    i++;
}

boolean prueba = false;
do {
    System.out.println("se ejecuta al menos una vez");
} while (prueba);
```

El primer bucle imprime seis valores, de cero a cinco. El incremento permite que la condición deje de cumplirse. El segundo imprime una vez aunque su condición sea falsa desde el principio, porque la comprobación ocurre después del cuerpo.

```java
// Recorrido reducido de los números mediante for-each.
int[] numeros = {1, 3, 5, 7, 9, 11, 13, 15};
for (int num : numeros) {
    System.out.println("num = " + num);
}
```

En cada vuelta se asigna a `num` el valor de la posición siguiente. No se expone el índice ni se modifica el arreglo por imprimir. Asignar otro número a `num` solo cambiaría la variable local de esa vuelta.

### Repetición con descarte de una vuelta

```java
for(int i = 0; i <= 10; i++) {
    System.out.println("i = " + i);
}
```

Este recorrido incluye los extremos 0 y 10. La inicialización se ejecuta una vez; la comparación controla la entrada a cada vuelta y el incremento prepara la siguiente.

La variante que selecciona pares utiliza el mismo recorrido, con una condición que omite los impares:

```java
// Versión reducida del recorrido de pares.
for (int i = 0; i <= 10; i++) {
    if (!(i % 2 == 0)) {
        continue;
    }
    System.out.println("i = " + i);
}
```

`continue` no termina el bucle: pasa a su actualización y se evalúa la siguiente vuelta. Se imprimen 0, 2, 4, 6, 8 y 10.

## 5. Texto, igualdad e inmutabilidad

Un `String` es un objeto inmutable. En el ejemplo de inmutabilidad de texto:

```java
String curso = "Java Master";
String profesor = "Felipe Peralta";
String resultado = curso.concat(profesor);
```

`curso` conserva `"Java Master"`; `resultado` contiene `"Java MasterFelipe Peralta"`. `concat` no añade espacios por su cuenta. Lo mismo ocurre con `replace`, `toUpperCase` y `transform`: el resultado debe tomarse del valor devuelto.

`equals` compara contenido, `equalsIgnoreCase` ignora diferencias de mayúsculas y `compareTo` indica orden lexicográfico: negativo, cero o positivo, sin prometer exclusivamente −1 o 1. `==` entre referencias pregunta identidad, no contenido.

El ejemplo de operaciones sobre texto recorre las herramientas básicas: `length` cuenta unidades UTF-16; `charAt` empieza en cero; `substring(inicio, fin)` incluye inicio y excluye fin. Para `"Felipe Peralta"`, `substring(1, 4)` produce `"eli"`. `indexOf` y `lastIndexOf` devuelven −1 cuando no encuentran; `contains`, `startsWith` y `endsWith` producen booleanos. `trim` elimina determinados espacios de los extremos, no los internos.

El ejemplo de conversión de texto en arreglos convierte texto en `char[]` y separa con `split`, cuyo separador es una expresión regular. El ejemplo de extracción de extensión busca el último punto: para `"alguna.imagen.pdf"` devuelve `"pdf"`. Si no hay punto, su cálculo termina tomando el texto completo; hay que validar antes para distinguir «sin extensión».

El ejemplo de validación de texto distingue ausencia (`null`), vacío (`""`) y blanco (`"   "`). Una llamada de instancia requiere una referencia a un objeto existente. El código sustituye `null` por un texto antes de usar `length`, `isEmpty` e `isBlank`.

El ejemplo de concatenación repetida contrasta reconstruir cadenas con acumular en `StringBuilder`. Un builder modifica su buffer con `append`, evitando copiar repetidamente toda la cadena creciente. Los milisegundos comentados no son garantías: dependen de JVM, calentamiento y máquina, y los dos primeros bucles ni siquiera arrancan con cadenas del mismo tamaño. La implementación no implementa una medición equivalente con `StringBuffer`.

### Ausencia, vacío y espacios

```java
// Adaptación de la validación de texto en una sola condición.
String curso = "   ";
boolean tieneContenido = curso != null && !curso.isBlank();
System.out.println(tieneContenido);
```

La referencia no es nula, pero la cadena no contiene caracteres distintos de espacios y el resultado es falso. Con `curso = null`, el cortocircuito evita invocar `isBlank`. Con una cadena vacía, `isEmpty` también sería verdadero; una cadena de espacios tiene longitud positiva y por eso distingue ambos criterios.

### Extraer segmentos y transformar texto

```java
String archivo = "alguna.imagen.pdf";
int i = archivo.lastIndexOf(".");
System.out.println("archivo.length() = " + archivo.length());
System.out.println("archivo.substring(archivo.length()-4) = " + archivo.substring(i+1));
```

`lastIndexOf` localiza el último punto, y `substring(i + 1)` toma todo lo que viene después. El valor extraído es `pdf`; los puntos anteriores forman parte del nombre. El mensaje de salida menciona una expresión diferente, pero el cálculo que se ejecuta usa `i + 1`.

La separación de un texto produce un arreglo que puede recorrerse:

```java
String[] arreglo2 = trabalenguas.split("a");
System.out.println("\nrabalenguas.split(\"a\") = " + Arrays.toString(arreglo2));
```

Separar `trabalenguas` por `a` produce los segmentos `tr`, `b`, `lengu` y `s`. El separador no aparece en ellos. Como `split` interpreta expresiones regulares, dividir por un punto literal requeriría escaparlo.

La acumulación mutable utiliza una secuencia de llamadas sobre el mismo constructor de texto:

```java
StringBuilder sb = new StringBuilder(a);
inicio = System.currentTimeMillis();
for (int i = 0; i < 100000; i++) {
    sb.append(a).append(b).append("\n"); // 500 => 0ms, 1000 => 0ms, 10000 => 2ms, 100000 => 8ms
}
```

Cada `append` añade contenido al buffer y devuelve el mismo builder, permitiendo encadenar. La ventaja conceptual es evitar reconstruir en cada vuelta toda la cadena anterior.

## 6. Un número como objeto

`Integer`, `Double` y `Boolean` son clases envoltorio: permiten representar valores en colecciones genéricas y también admitir `null`. `WrapperInteger` muestra boxing (`int → Integer`) y unboxing (`Integer → int`). `AutoboxingInteger` suma los pares tanto con `intValue()` explícito como con conversión automática; ambas sumas dan `56` para 1 a 15.

Desenvolver un wrapper nulo produce `NullPointerException`. En `WrapperOperadoresRelacionales`, la etiqueta «Son el mismo objeto?» imprime `Objects.equals(num1, num2)`, que verifica **igualdad de valores** para `Integer`. No comprueba identidad. Para identidad sería `num1 == num2`; para valores, `equals` o `Objects.equals`. La caché de algunos wrappers puede hacer engañosas las pruebas con `==`.

### Conversión automática durante un recorrido

```java
for (Integer i : enteros) {
    if (i.intValue() % 2 == 0) {
        suma += i.intValue();
    }
}
```

Esta versión utiliza `intValue()` explícitamente; la aritmética se realiza sobre enteros primitivos. La forma reducida equivalente es:

```java
int suma = 0;
for (Integer valor : enteros) {
    if (valor % 2 == 0) {
        suma += valor;
    }
}
```

El compilador añade las conversiones necesarias al evaluar el resto y la suma. Un elemento nulo no puede convertirse a `int`, aunque el arreglo y su tipo genérico sean válidos.

## 7. Java siempre pasa por valor

`PasarPorValor` cambia a `35` su parámetro local, pero el `i` del llamador permanece en `10`. Cada método tiene su propia variable. Cuando el argumento es un objeto, el valor copiado es una **referencia** al objeto.

La operación que modifica el arreglo compartido es:

```java
public static void test(int[] edadArr) {
    System.out.println("Iniciamos el método test");
    for (int i = 0; i < edadArr.length; i++) {
        edadArr[i] = edadArr[i] + 20;
    }
    System.out.println("Finaliza el método test");
}
```

El `edad` de `main` y `edadArr` apuntan al mismo arreglo. Cambiar sus posiciones transforma `{10, 11, 12}` en `{30, 31, 32}` y el llamador ve el resultado. Eso **no** significa que Java pase la variable del llamador por referencia. Si el método reasignara `edadArr = new int[]{99}`, cambiaría solo su referencia local. `PasoPorReferencia2` repite la idea con una persona: `modificarNombre` muta el objeto compartido de `"Felipe"` a `"Pedrito"`.

### Cambiar una variable local frente a mutar un objeto

```java
public static void test(int i){
    System.out.println("Iniciamos el método test con i = " + i);
    i = 35;
    System.out.println("Finaliza el método test con i = " + i);
}
```

Asignar 35 cambia el parámetro del método. Si el llamador tenía `int i = 10`, su variable sigue siendo 10 después de retornar: las dos variables solo compartían el valor inicial.

La modificación de una persona actúa sobre el objeto apuntado por la referencia:

```java
public static void metodoTest(Persona persona) {
    System.out.println("Iniciamos el método test");
    persona.modificarNombre("Pedrito");
    System.out.println("Finaliza el método test");
}
```

La referencia se copia al parámetro, pero ambos accesos siguen alcanzando la misma persona. El estado del objeto cambia a `Pedrito`. Una reasignación del parámetro a otra persona sería una operación distinta y no reemplazaría la referencia del llamador.

## 8. Datos externos y errores

`Scanner.nextInt` interpreta un entero; `next` obtiene un token y `nextLine` una línea. Después de leer un número puede quedar el salto de línea pendiente: un `nextLine` posterior puede consumirlo y parecer vacío. El capítulo de facturas aplica precisamente esa limpieza.

`SistemasNumericosEntradaScanner` captura `InputMismatchException` y continúa con cero. Es un valor de recuperación elegido por el ejemplo. La calculadora de argumentos exige tres textos: operación y dos enteros. Para división convierte el primer operando a `double` y comprueba denominador cero. Si falla el parseo, asigna −1 a ambos y continúa; no se debe confundir ese resultado con una operación válida del usuario.

`try (Scanner s = ...)` cierra el recurso al salir, incluso si hay una excepción. Un scanner sobre `System.in` también cierra esa entrada; estos ejemplos se ejecutan como programas independientes.

### Parseo de argumentos y división decimal

```java
a = Integer.parseInt(args[1]);
b = Integer.parseInt(args[2]);
```

Los argumentos llegan como texto. La conversión permite operar con números y puede fallar si el contenido no representa un entero. Esta situación se diferencia de recibir una cantidad incorrecta de argumentos, que exige comprobar la longitud antes del acceso.

```java
// Operación de división utilizada por la calculadora.
resultado = (double) a / b;
```

El cast se aplica antes de dividir y hace que la operación conserve fracciones. Con `a = 5` y `b = 2`, el resultado es 2.5; asignar a `double` el resultado de `a / b` sin el cast produciría 2.0 porque la división entera ya habría ocurrido.

## 9. Fechas clásicas, utilidades e introspección

`Date` representa un instante; `SimpleDateFormat` lo convierte a texto o interpreta una cadena. El ejemplo de fecha clásica y medición resta tiempos en milisegundos para observar un bucle; no es un benchmark preciso. El ejemplo de interpretación de fechas clásicas usa `yyyy-MM-dd`, pero el formateador clásico es tolerante por defecto y puede normalizar ciertas fechas inválidas. Capturar `ParseException` no basta para imponer una validación estricta.

`Calendar` permite modificar campos. Sus meses se indexan desde cero, por eso conviene usar `Calendar.JANUARY` o `Calendar.OCTOBER`. `getInstance()` arranca con fecha y hora actuales; al cambiar solo año, mes y día, se conserva la hora, como sucede en la construcción de una fecha de nacimiento. `Date` y `Calendar` siguen existiendo; no todos sus métodos están deprecados. El capítulo 10 explica una API temporal más expresiva e inmutable.

El ejemplo de funciones matemáticas calcula valor absoluto, extremos, techo, piso, redondeo, exponencial, logaritmo, potencia, raíz y trigonometría. `ceil(3.5)` da `4.0`, `floor(3.5)` da `3.0`; seno y coseno reciben radianes. Los resultados decimales pueden incluir errores de representación.

`getClass()` obtiene la clase real del objeto; `getSuperclass` sigue la jerarquía y `getMethods` enumera métodos públicos. Los dos ejemplos de `operadorinstanceof` verifican pertenencia a un tipo: un `Integer` también es `Number` y `Object`. El segundo usa variables amplias como `Object` y `Number`; su nombre no significa que implemente genéricos o pattern matching. `null instanceof Tipo` siempre es falso.

### Campos de calendario y formato de presentación

```java
calendario.set(Calendar.YEAR, 2025);
calendario.set(Calendar.MONTH, Calendar.OCTOBER);
calendario.set(Calendar.DAY_OF_MONTH, 13);
```

Las constantes nombran los campos que se sustituyen. Utilizar `Calendar.OCTOBER` hace explícita la intención y evita escribir manualmente su índice de mes basado en cero. Los otros campos conservan su estado hasta que se asignan.

```java
SimpleDateFormat df = new SimpleDateFormat("EEEE dd 'de' MMMM, yyyy");
String fechaStr = df.format(fecha);
```

El patrón distingue día de semana, día del mes, nombre del mes y año. Las comillas simples alrededor de `de` indican texto literal, que no se interpreta como parte de la sintaxis de fechas. El idioma del resultado depende del locale del formateador.

### Cálculos y consulta del tipo real

```java
double techo = Math.ceil(3.5);
System.out.println("techo = " + techo);

double piso = Math.floor(3.5);
System.out.println("piso = " + piso);
```

`ceil` aproxima hacia arriba y `floor` hacia abajo: para 3.5 producen 4.0 y 3.0. No son la misma operación que convertir directamente a un entero o redondear al más cercano.

```java
Object texto = "Creando un objeto de la clase String ... que tal!";
Boolean b1 = texto instanceof String;
```

La variable declara un tipo amplio, pero el objeto real es una cadena. `instanceof` comprueba esa pertenencia durante la ejecución; no cambia el tipo declarado de la variable ni crea un objeto nuevo.

## 10. Programa, JVM y sistema operativo

`System.getenv` lee variables de entorno heredadas por el proceso. `System.getProperty` lee propiedades de la JVM, como `user.dir` o `java.version`. Son fuentes diferentes. El ejemplo de carga de propiedades de sistema carga config.properties desde el classpath: `/config.properties` señala la raíz de recursos, no la raíz del disco. La carga verifica que el stream exista y añade propiedades propias a unas que usan las actuales como valores por defecto.

El ejemplo de ejecución de procesos externos inicia `ls -l` con `ProcessBuilder`, lee su salida y espera con `waitFor`. El resultado depende del directorio y de que exista ese comando en el sistema; no es una operación portable a cualquier sistema operativo. Una aplicación robusta también gestiona salida de error, recursos e interrupciones.

### Cargar configuración y administrar un proceso

```java
Properties p = new Properties(System.getProperties());
p.load(archivo);
p.setProperty("mi.propiedad.personalizada", "Mi valor guardado en el objeto properties");
System.setProperties(p);
```

Las propiedades actuales actúan como valores por defecto y el recurso cargado aporta propiedades específicas. `setProperty` añade una clave; `System.setProperties` instala ese objeto como configuración de propiedades de la JVM. Es una modificación global para el proceso Java.

```java
ProcessBuilder pb = new ProcessBuilder("ls", "-l");
pb.directory(new File(System.getProperty("user.dir")));
proceso = pb.start();
```

El programa externo y sus argumentos se suministran separados. `directory` establece el directorio de trabajo del proceso creado; `start` es la operación que lo inicia. Construir el objeto de configuración no ejecuta el comando todavía.

## Preguntas de repaso

Para la operación `resta` con argumentos 67 y 12, la calculadora obtiene `55.0`. Las edades iniciales y modificadas corresponden al estado del mismo arreglo antes y después de la llamada.

1. ¿Por qué convertir `99.99` a `int` produce 99? **Porque el cast trunca, no redondea.**
2. ¿Por qué `curso.concat(...)` no cambia `curso`? **Porque el objeto `String` es inmutable y el resultado se devuelve aparte.**
3. ¿Por qué un método puede cambiar un arreglo del llamador? **Porque recibe una copia de la referencia al mismo objeto.**
4. ¿Qué protege `i < a.length && a[i] != buscado`? **El cortocircuito evita acceder fuera del arreglo.**
