# 12 · Recursividad: reducir un problema hasta poder detenerse

## Contenido

- [1. Dos decisiones imprescindibles](#1-dos-decisiones-imprescindibles)
- [2. Recursión numérica y de texto: entender la bajada y el retorno](#2-recursión-numérica-y-de-texto-entender-la-bajada-y-el-retorno)
- [3. Representar una jerarquía en memoria](#3-representar-una-jerarquía-en-memoria)
- [4. Recorrido recursivo de árboles: dos recorridos del mismo árbol](#4-recorrido-recursivo-de-árboles-dos-recorridos-del-mismo-árbol)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Dos decisiones imprescindibles

Un método recursivo se llama a sí mismo con otra instancia del problema. Necesita un **caso base**, cuya respuesta ya conoce, y un **paso recursivo** que se acerque a ese caso. Llamarse con el mismo dato indefinidamente no reduce nada y puede agotar la pila.

Cada llamada conserva parámetros y trabajo pendiente en un frame. Cuando termina la más profunda, su resultado vuelve a la anterior. No hay una profundidad universal segura de diez mil llamadas: depende de tamaño de pila, JVM y método. Java no garantiza eliminar llamadas recursivas aunque estén al final.

## 2. Recursión numérica y de texto: entender la bajada y el retorno

El ejemplo de recursión numérica y de texto define factorial:

```java
public static long factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}
```

Para 4, la ejecución baja hasta 1 antes de resolver las multiplicaciones:

```text
factorial(4) = 4 × factorial(3)
factorial(3) = 3 × factorial(2)
factorial(2) = 2 × factorial(1)
factorial(1) = 1
retorno: 1 → 2 → 6 → 24
```

El método está esperando resultados, no ejecutando todas las multiplicaciones al mismo tiempo. Tiempo y pila crecen linealmente con n para entradas no negativas. El caso base también devuelve 1 para negativos, aunque el factorial ordinario no se define así; faltaría validar esa entrada. `long` tampoco permite factoriales arbitrarios: 20! cabe y 21! desborda.

### Fibonacci: una definición simple puede repetir muchísimo trabajo

`fibonacci(n)` suma `fibonacci(n-1)` y `fibonacci(n-2)` con bases 0 y 1. Al calcular 5, las ramas vuelven a calcular 3, 2 y 1 varias veces. Para 21 el ejemplo obtiene 10946. El número de llamadas crece exponencialmente; O(2ⁿ) es una cota superior habitual para explicar su costo. La pila máxima crece linealmente, no exponencialmente: muchas llamadas ocurren en momentos diferentes.

Una versión iterativa o memoizada evitaría repetir subproblemas, pero no está implementada en esta implementación. El límite de representación de `long` sigue existiendo incluso con un algoritmo más eficiente.

### Un subproblema que se repite en dos ramas

```java
public static long fibonacci(int n) {
    if (n <= 0) return 0;           // caso base 1
    if (n == 1) return 1;           // caso base 2
    return fibonacci(n - 1) + fibonacci(n - 2);
}
```

Las dos llamadas son problemas menores, pero comparten otros subproblemas. Para calcular `fibonacci(5)`, la rama de 4 y la rama de 3 vuelven a necesitar 2 y 1. Que cada llamada progrese hacia un caso base garantiza terminación para las entradas previstas, no eficiencia.

### Suma de arreglo: mover la frontera

```java
public static int sumaArray(int[] arr, int i) {
    if (i == arr.length) return 0;
    return arr[i] + sumaArray(arr, i + 1);
}
```

El caso base devuelve cero, identidad de suma. No reduce la longitud del objeto: incrementa el índice y reduce la cantidad pendiente. Para `{9,9,8,8,9,7}`, suma 50. El `main` divide `50 / 6` como enteros y muestra promedio 8, no 8.33. Adaptación para conservar decimales: `(double) total / notas.length`.

El índice inicial debe estar entre cero y longitud, y el arreglo debe existir. La condición `i == arr.length` no maneja un índice mayor: el acceso posterior falla. El arreglo vacío con `i=0` retorna cero inmediatamente.

### Potencia, palíndromo y dígitos

`potencia(base,exp)` devuelve 1 cuando exp es 0; si es positivo multiplica por una potencia de exponente menor. Si es negativo usa el recíproco: `potencia(2,-3)` da 0.125. El trabajo de esta versión es lineal en la magnitud del exponente. Para cero con exponente negativo no hay un valor matemático finito; las operaciones de `double` pueden producir infinito. Para `Integer.MIN_VALUE`, negar el exponente desborda y el caso negativo no progresa correctamente.

`esPalindromo` compara extremos, rechaza si difieren y continúa con `substring` interior. Cadenas vacías o de un carácter dan verdadero. `"reconocer"` se reduce a `"econoce"`, `"conoc"`, `"ono"`, `"n"`. El `main` pasa minúsculas, pero el método no elimina acentos ni espacios por sí solo. Copiar substrings repetidamente implica memoria y trabajo adicional; una alternativa con índices compartiría el texto original.

`contarDigitos` divide por diez hasta quedar por debajo de diez. Cero tiene un dígito; −12345 tiene cinco después de negar el signo. Pero `-Integer.MIN_VALUE` vuelve a producir el mismo negativo por desbordamiento y el código devuelve uno incorrectamente. Ese tratamiento de negativos no cubre todos los valores de `int`.

### Reducir un exponente, un intervalo y un número

```java
public static double potencia(double base, int exp) {
    if (exp == 0) return 1;
    if (exp < 0)  return 1.0 / potencia(base, -exp);  // soporte para exponentes negativos
    return base * potencia(base, exp - 1);
}
```

Con exponente positivo se conserva una multiplicación pendiente y se reduce en uno. Con negativo se calcula el recíproco de la potencia positiva. La negación del mínimo `int` no cabe en el mismo tipo: ese valor es un caso límite que necesita tratamiento diferente.

```java
public static boolean esPalindromo(String s) {
    if (s.length() <= 1) return true;                        // caso base: 0 o 1 char
    if (s.charAt(0) != s.charAt(s.length() - 1)) return false;
    return esPalindromo(s.substring(1, s.length() - 1));    // caso recursivo
}
```

La comparación de extremos puede decidir falso sin más llamadas. Cuando coinciden, la cadena interior es más corta y se acerca al caso base. El procedimiento comprueba los caracteres recibidos; la normalización de mayúsculas o espacios sería una regla adicional.

```java
public static int contarDigitos(int n) {
    if (n < 0)   n = -n;    // manejar negativos
    if (n < 10)  return 1;  // caso base: un solo dígito
    return 1 + contarDigitos(n / 10);
}
```

Dividir por diez descarta el último dígito de un entero positivo. El retorno agrega uno al conteo del resto. El caso base incluye cero, cuya representación tiene un dígito. El manejo del signo presupone que la magnitud positiva cabe en `int`.

## 3. Representar una jerarquía en memoria

`Nodo` tiene nombre, lista de hijos y nivel. El nivel se asigna durante el recorrido con stream; no se calcula en el constructor. `addHijo` retorna `this`, así `padre.addHijo(a).addHijo(b)` añade ambos al mismo padre y no cambia el receptor a uno de los hijos.

La clase no tiene parámetro de tipo `<T>`: «genérico» en el comentario significa reutilizable como árbol de nombres, no genéricos de Java. Un nodo sin hijos es una hoja; uno con hijos es una rama. El árbol del ejemplo se construye manualmente, no inspecciona el sistema de archivos real y puede incluir nombres de carpetas ilustrativos.

No hay validación que impida añadir un nodo como hijo de sí mismo, introducir ciclos o compartirlo entre padres. La recursión supone un árbol finito sin ciclos; si lo violas, puede no terminar o calcular niveles ambiguos.

### Construir la estructura antes de recorrerla

```java
public Nodo addHijo(Nodo hijo) {
    this.hijos.add(hijo);
    return this;
}
```

La relación se agrega a una lista; no se recorre el descendiente todavía. Retornar el receptor permite añadir varios hijos al mismo nodo. El método acepta cualquier referencia sin comprobar ciclos, así la propiedad de árbol debe mantenerse al construir los datos.

```java
// Subárbol reducido de los ejemplos de jerarquías.
Nodo raiz = new Nodo("Udemy_Master_Java/");
Nodo basicos = new Nodo("p01_basicos/");
basicos.addHijo(new Nodo("fundamentos/"))
        .addHijo(new Nodo("operadores/"));
raiz.addHijo(basicos);
```

Hay una raíz, una rama y dos hojas. Un recorrido en preorden emite raíz, básicos, fundamentos y operadores. El árbol solo conserva nombres en memoria; esos nombres no hacen que se creen directorios físicos.

## 4. Recorrido recursivo de árboles: dos recorridos del mismo árbol

La versión clásica imprime primero el nodo y después cada subárbol:

```java
public static void imprimirArbol(Nodo nodo, int nivel) {
    System.out.println("  ".repeat(nivel) + nodo.getNombre());
    if (nodo.tieneHijos()) {
        for (Nodo hijo : nodo.getHijos()) {
            imprimirArbol(hijo, nivel + 1);
        }
    }
}
```

Es un recorrido de profundidad primero en **preorden**: padre antes que descendientes, respetando el orden de la lista. El caso base es implícito: una hoja no entra en el bloque y retorna. `nivel + 1` aumenta la indentación, pero lo que garantiza terminar es llegar a nodos sin hijos, no aumentar el número de nivel.

La alternativa del ejemplo de recorrido recursivo de árboles produce un stream:

```java
public static Stream<Nodo> nodoStream(Nodo nodo, int nivel) {
    nodo.setNivel(nivel);
    return Stream.concat(
            Stream.of(nodo),
            nodo.getHijos().stream()
                .flatMap(hijo -> nodoStream(hijo, nivel + 1))
    );
}
```

`Stream.of(nodo)` aporta el actual. Cada hijo produce otro stream de su subárbol. `flatMap` aplana esos streams y `concat` coloca el padre delante de todos. Una terminal `forEach` los imprime. Es también recursión: escribirlo funcionalmente no elimina la profundidad ni garantiza seguridad para árboles muy profundos.

Esta versión muta `nivel` de los nodos. Compartirlos entre recorridos simultáneos o reutilizarlos en ramas diferentes puede cambiar los niveles observados. Para un árbol de N nodos sin ciclos, el recorrido clásico visita cada uno una vez, O(N), y su pila depende de la altura H, O(H). En jerarquías enormes puede requerirse una pila explícita y un recorrido iterativo.

## Preguntas de repaso

1. ¿Qué retorna el caso base de suma? **Cero, para no alterar la suma acumulada al volver.**
2. ¿Qué reduce el problema del árbol? **Recorrer subárboles que terminan en hojas.**
3. ¿Por qué Fibonacci es lento? **Recalcula los mismos subproblemas en muchas ramas.**
4. ¿El stream recursivo evita la recursión? **No; vuelve a llamar a `nodoStream`.**

El patrón [Composite](13_patrones_diseno.md) formaliza la colaboración entre hojas y ramas que aparece aquí.
