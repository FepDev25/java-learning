# 03 · Matrices: razonar con filas y columnas

## Contenido

- [1. Una matriz Java es un arreglo de arreglos](#1-una-matriz-java-es-un-arreglo-de-arreglos)
- [2. Recorrido por filas: qué hace cada bucle](#2-recorrido-por-filas-qué-hace-cada-bucle)
- [3. Filas variables: el límite correcto pertenece a la fila](#3-filas-variables-el-límite-correcto-pertenece-a-la-fila)
- [4. Buscar y salir de dos bucles](#4-buscar-y-salir-de-dos-bucles)
- [5. Construir patrones a partir de coordenadas](#5-construir-patrones-a-partir-de-coordenadas)
- [6. Sumar: elemento con elemento](#6-sumar-elemento-con-elemento)
- [7. Transponer: intercambiar el papel de los índices](#7-transponer-intercambiar-el-papel-de-los-índices)
- [8. Simetría: compararse con la propia transpuesta](#8-simetría-compararse-con-la-propia-transpuesta)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Una matriz Java es un arreglo de arreglos

En el ejemplo de creación de matrices, `new int[2][4]` crea dos filas con cuatro enteros cada una. `numeros[1][2]` significa: toma la fila de índice 1 y luego su posición 2. El primer índice es fila y el segundo columna, ambos desde cero.

`numeros.length` es la cantidad de filas. `numeros[i].length` es la longitud de la fila i. No existe una propiedad única «cantidad de columnas» para todos los arreglos bidimensionales de Java: cada fila puede tener un tamaño diferente.

El ejemplo usa filas `[1,2,3,4]` y `[11,12,13,14]`; el primer dato es 1 y el último, 14. Acceder manualmente demuestra la estructura, pero los bucles permiten trabajar con tamaños mayores sin repetir instrucciones.

### Reserva bidimensional y lectura de posiciones

```java
int[][] numeros = new int[2][4];

numeros[0][0] = 1;
numeros[0][1] = 2;
numeros[0][2] = 3;
numeros[0][3] = 4;
```

La primera dimensión reserva las filas y la segunda define la longitud de cada una en este caso rectangular. Estas asignaciones llenan la fila de índice cero; la segunda fila conserva sus valores iniciales hasta que se asigna. La expresión `numeros[0]` es un arreglo completo y `numeros[0][2]` es un entero de ese arreglo.

## 2. Recorrido por filas: qué hace cada bucle

Fragmento del ejemplo de recorrido de matrices de texto:

```java
for (int i = 0; i<nombres.length;i++) {
    for (int j = 0; j < nombres[i].length; j++) {
        System.out.print(nombres[i][j] + "\t");
    }
    System.out.println();
}
```

El bucle exterior selecciona una fila. El interior recorre todas sus columnas. Cuando termina, el salto de línea separa visualmente esa fila de la siguiente. Para los datos dla implementación se imprime:

```text
Pepe    Pepa
Josefa  Paco
Lucas   Pancha
```

La inicialización explícita y la compacta permiten usar `for-each`: primero se obtiene un `String[] fila`, luego cada `String nombre`. Los índices permiten consultar coordenadas o modificar posiciones; el recorrido de valores simplifica la lectura. Un recorrido completo de R filas y C columnas de una matriz rectangular visita R×C elementos; en una irregular visita la suma de sus longitudes.

## 3. Filas variables: el límite correcto pertenece a la fila

El ejemplo de filas de longitud variable crea:

```java
int[][] matriz = new int[3][];

matriz[0] = new int[2];
matriz[1] = new int[3];
matriz[2] = new int[4];
```

Después de la primera instrucción hay tres referencias a filas inicialmente nulas. Las siguientes crean cada arreglo interior. Acceder a `matriz[0].length` antes de inicializar la fila produciría `NullPointerException`.

La asignación `matriz[i][j] = i * j` produce filas `[0,0]`, `[0,1,2]` y `[0,2,4,6]`. El bucle usa `matriz[i].length`; usar siempre el tamaño de la primera fila omitiría datos de las otras. Usar siempre cuatro provocaría accesos inválidos en las filas cortas.

## 4. Buscar y salir de dos bucles

En el ejemplo de búsqueda por coordenadas se busca 1999. Está en la fila 2, columna 3. La pieza central es:

```java
buscar: for(i = 0; i < matrizDeEnteros.length; i++){
    for(j = 0; j < matrizDeEnteros[i].length; j++){
        if(matrizDeEnteros[i][j] == elementoBuscar){
            encontrado = true;
            break buscar;
        }
    }
}
```

Un `break` sin etiqueta solo terminaría el bucle de columnas: el de filas seguiría y podría perder la coordenada del hallazgo. `break buscar` termina el exterior etiquetado. `encontrado` indica si las coordenadas son válidas; no hay que interpretar `i` y `j` como un hallazgo cuando el recorrido llegó al final sin éxito.

La búsqueda encuentra la primera coincidencia en orden por filas. En el peor caso recorre toda la matriz; no depende de que esté ordenada.

## 5. Construir patrones a partir de coordenadas

El ejemplo de construcción de la identidad empieza con una matriz 5×5 de ceros y asigna 1 cuando `i == j`. Esa condición representa la diagonal principal. El resultado tiene unos en `(0,0)`, `(1,1)`, …, `(4,4)` y ceros en el resto. Una matriz identidad matemática es cuadrada.

El ejemplo de construcción de marco y diagonal comprueba primera o última fila, primera o última columna **o** diagonal (`i == j`). Por eso dibuja un marco y una diagonal, no solo un contorno. El interior que no pertenece a la diagonal conserva cero. La idea general es útil: traducir una figura a una condición sobre coordenadas.

### Condiciones geométricas sobre índices

```java
// Versión reducida de la construcción de la matriz identidad.
int[][] matriz = new int[5][5];
for (int i = 0; i < matriz.length; i++) {
    for (int j = 0; j < matriz[i].length; j++) {
        if (i == j) {
            matriz[i][j] = 1;
        }
    }
}
```

La igualdad de índices selecciona la diagonal. Las otras posiciones no necesitan una asignación explícita de cero porque el arreglo de enteros recién creado ya tiene ese valor inicial.

```java
// Condición del marco y la diagonal, con formato reducido.
if (i == 0 || i == matriz.length - 1
        || j == 0 || j == matriz[i].length - 1 || i == j) {
    matriz[i][j] = 1;
}
```

Cada alternativa del `||` representa una región: borde superior, inferior, izquierdo, derecho y diagonal. Un elemento que cumpla varias condiciones se asigna una sola vez porque hay una única rama.

## 6. Sumar: elemento con elemento

En el ejemplo de suma de matrices:

```java
int[][] suma = new int[a.length][a[0].length];

for(int i = 0; i < a.length; i++){
    for(int j = 0; j < a[i].length; j++){
        suma[i][j] = a[i][j] + b[i][j];
    }
}
```

Cada posición combina los datos de la misma coordenada. Sus matrices son 3×3, una con 1 a 9 y otra con 10 a 90. La salida es:

```text
11  22  33
44  55  66
77  88  99
```

Se crea otro arreglo y no se modifican `a` ni `b`. La precondición es que tengan formas compatibles. El ejemplo supone una matriz rectangular no vacía al leer `a[0].length`; no verifica tamaños ni filas nulas. La suma elemento a elemento no es una multiplicación de matrices.

El ejemplo de sumas por fila y columna fija `i` y, por cada `j`, acumula `a[i][j]` para la fila y `a[j][i]` para la columna. Reinicia ambas sumas al comenzar cada `i`. Con los datos 1 a 9:

| Índice | Suma de fila | Suma de columna |
| --- | --- | --- |
| 0 | 6 | 12 |
| 1 | 15 | 15 |
| 2 | 24 | 18 |

La técnica combinada dla implementación supone una matriz cuadrada. En una rectangular, filas y columnas necesitan límites diferentes; no basta con reutilizar el mismo bucle.

### Dos acumuladores con distinta orientación

```java
for(int i = 0; i < a.length; i++){
    sumaColumna = 0;
    sumaFila = 0;
    for(int j = 0; j < a[i].length; j++){
        sumaFila += a[i][j];
        sumaColumna += a[j][i];
    }
    System.out.println("Total fila " + i + ": " + sumaFila);
    System.out.println("Total columna " + i + ": " + sumaColumna);
}
```

`a[i][j]` mantiene fija la fila y `a[j][i]` mantiene fija la columna. Reiniciar ambas sumas dentro de cada vuelta exterior evita arrastrar los resultados de la anterior. Esta implementación usa una matriz cuadrada; una forma rectangular necesita recorridos separados o límites específicos.

## 7. Transponer: intercambiar el papel de los índices

Transponer transforma una matriz R×C en C×R. La regla es `b[j][i] = a[i][j]`: la columna original se vuelve fila y viceversa.

El ejemplo de transposición con un destino nuevo crea `a` de 8×4 y `b` de 4×8. Llena `a[i][j] = i + j * 3` y copia las posiciones con los índices intercambiados. Por ejemplo, `a[2][1]` vale 5 y pasa a `b[1][2]`. Es válido para su matriz rectangular porque el destino tiene las dimensiones invertidas.

El ejemplo de transposición en el mismo arreglo usa una estrategia distinta: modifica una matriz cuadrada en el lugar.

```java
for(int i = 1; i < matriz.length; i++){
    for(int j = 0; j < i; j++){
        aux = matriz[i][j];
        matriz[i][j] = matriz[j][i];
        matriz[j][i]= aux;
    }
}
```

Solo visita el triángulo inferior, `j < i`. Intercambia cada pareja una vez y no toca la diagonal, que ya está en su lugar. Recorrer las dos mitades desharía la transposición. Para los números 1 a 16, la primera fila final es `1,5,9,13`. Esta técnica usa espacio auxiliar constante, pero necesita una matriz cuadrada; no puede convertir en el mismo arreglo una forma 8×4 en 4×8.

## 8. Simetría: compararse con la propia transpuesta

Una matriz cuadrada es simétrica cuando `a[i][j] == a[j][i]` para toda pareja. El ejemplo de comprobación de simetría inspecciona solo el triángulo inferior: si una pareja difiere, marca falso y termina ambos bucles. Revisar la diagonal es innecesario porque cada elemento es igual a sí mismo.

Su matriz ya es simétrica y lo informa. La simetría no exige que toda la diagonal sea 1: el último elemento diagonal dla implementación es 7. Tampoco equivale a identidad; la identidad es un caso especial de matriz simétrica.

### Rechazar al encontrar una pareja diferente

```java
salir: for(int i = 0; i < matriz.length; i++){

   for(int j = 0;j < i; j++){
       if(matriz[i][j] != matriz[j][i]){
           simetrica = false;
           break salir;
       }
   }
}
```

La parte relevante del recorrido compara solo `j < i`, una mitad de las parejas simétricas. Una diferencia basta para rechazar toda la matriz, y la etiqueta termina los dos bucles. Si todas coinciden, la bandera conserva su valor verdadero inicial.

## Preguntas de repaso

1. ¿Cuál es la cantidad de columnas de una fila irregular? **`matriz[i].length`.**
2. ¿Qué forma tiene la transpuesta de 8×4? **4×8.**
3. ¿Por qué `j < i` al intercambiar? **Para visitar una sola vez cada pareja de posiciones opuestas.**
4. ¿Qué pasaría al cambiar solo `matriz[1][0]` en la simétrica? **Dejaría de ser simétrica si no coincide con `matriz[0][1]`.**
