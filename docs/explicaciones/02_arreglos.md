# 02 · Arreglos: organizar datos por posición

## Contenido

- [1. El modelo mental: posiciones fijas](#1-el-modelo-mental-posiciones-fijas)
- [2. Recorrer: elegir el índice o el valor](#2-recorrer-elegir-el-índice-o-el-valor)
- [3. Leer al revés frente a invertir los datos](#3-leer-al-revés-frente-a-invertir-los-datos)
- [4. Ordenar y detectar orden](#4-ordenar-y-detectar-orden)
- [5. Buscar y conservar un candidato](#5-buscar-y-conservar-un-candidato)
- [6. Desplazar: el sentido del recorrido evita perder datos](#6-desplazar-el-sentido-del-recorrido-evita-perder-datos)
- [7. Eliminar significa mover y crear otro arreglo](#7-eliminar-significa-mover-y-crear-otro-arreglo)
- [8. Combinar, clasificar y acumular](#8-combinar-clasificar-y-acumular)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. El modelo mental: posiciones fijas

Un arreglo almacena una secuencia de valores del mismo tipo con longitud fija. Para representar siete productos, `new String[7]` reserva siete posiciones. La primera es 0, la última 6. El objeto arreglo no crece; para cambiar de tamaño se crea otro y se copian datos.

En el ejemplo de creación de arreglos, cuatro enteros se inicializan con `10`, `8`, `35` y `-1`, y se llama a `Arrays.sort(numeros)`. El resultado es `[-1, 8, 10, 35]`. Ordenar modifica el arreglo: después `numeros[0]` ya no es 10.

`length` es la cantidad de posiciones, no el último índice. Un acceso `numeros[4]` sobre esas cuatro posiciones lanza `ArrayIndexOutOfBoundsException`; el código lo deja comentado. Los enteros nuevos empiezan en cero y las referencias nuevas, como las de `String[]`, en `null`. Una variable local independiente no obtiene automáticamente ese valor inicial.

### Crear, llenar y ordenar una secuencia

```java
int[] numeros = new int[4];

numeros[0] = 10;
numeros[1] = 8;
numeros[2] = 35;
numeros[3] = -1;

Arrays.sort(numeros);
```

La reserva crea cuatro posiciones, inicialmente en cero. Las asignaciones sustituyen cada una antes del ordenamiento. El comentario de una quinta posición se conserva como contraste: acceder al índice 4 sería inválido. Tras ordenar, el arreglo contiene −1, 8, 10 y 35, y la longitud permanece en cuatro.

## 2. Recorrer: elegir el índice o el valor

El ejemplo de recorridos de arreglos recorre los mismos productos con `for`, `for-each`, `while` y `do / while`:

```java
for(int i = 0; i < total; i++){
    System.out.println("para indice " + i + " : " + productos[i]);
}
```

El índice comienza en cero y llega hasta `total - 1`. El límite válido utiliza `<`, porque `length` no es un índice accesible. El `for-each` simplifica leer valores, pero no expone el índice de la posición. El `do / while` mostrado supone que hay productos: con un arreglo vacío intentaría leer la posición 0 antes de comprobar la condición.

El segundo arreglo se llena con `numeros[k] = k * 3`. Son diez valores: `0, 3, 6, …, 27`. No se pide entrada; el índice genera el dato.

## 3. Leer al revés frente a invertir los datos

El ejemplo de lectura inversa ordena productos y los imprime desde el final. El cálculo `total - 1 - i` transforma `i=0` en el último índice. Cambia el orden de **lectura**, no la ubicación de los elementos.

En cambio, el ejemplo de inversión mediante intercambios intercambia posiciones:

```java
int total2 = arreglo.length;
int total = arreglo.length;
for(int i = 0; i < total2; i++){
    String actual = arreglo[i];
    String inverso = arreglo[total-1-i];
    arreglo[i] = inverso;
    arreglo[total-1-i] = actual;
    total2--;
}
```

La variable auxiliar conserva el valor que sería sobrescrito. `i` avanza y `total2` retrocede, así se detiene cerca del centro. Para cinco valores hay un intercambio central consigo mismo, que no cambia el resultado. Recorrer todos los índices con un límite fijo desharía los intercambios al llegar a la segunda mitad.

Traza didáctica: `[A,B,C,D] → [D,B,C,A] → [D,C,B,A]`. Se usa tiempo O(n) y espacio auxiliar O(1): aumentan los intercambios con el tamaño, pero no se crea otro arreglo. La mutación es visible desde el llamador porque ambos métodos comparten el objeto.

## 4. Ordenar y detectar orden

El ejemplo de ordenamiento por burbuja compara vecinos:

```java
if( ((Comparable) arreglo[j+1]).compareTo(arreglo[j]) > 0 ){
    Object auxiliar = arreglo[j];
    arreglo[j] = arreglo[j+1];
    arreglo[j+1] = auxiliar;
}
```

Si el siguiente es mayor, lo lleva hacia la izquierda. Esta variante ordena **descendentemente**. Para sus números devuelve `[35, 10, 7, -1]`. Tras cada pasada, uno de los menores queda al final, por eso el límite interno es `total - 1 - i`.

Con cuatro elementos hay 3 + 2 + 1 = 6 comparaciones. Su costo crece como O(n²) y no deja de comparar aunque ya esté ordenado. Es útil para entender el algoritmo; para ordenar datos ordinarios el módulo también muestra `Arrays.sort`.

Recibe `Object[]` y fuerza `Comparable` sin parámetros. Eso no prueba que cualquier objeto sea ordenable: mezclar valores incompatibles o usar objetos sin comparación puede fallar en ejecución. El capítulo 4 introduce genéricos para expresar restricciones mejor.

El ejemplo de detección del orden registra si hubo alguna subida y alguna bajada. Ambas banderas verdaderas indican desorden; ninguna, todos iguales; solo una, orden ascendente o descendente. Los iguales no invalidan ese orden: `[1,1,2]` se clasifica como ascendente, aunque no sea estrictamente creciente.

### Detectar tendencias entre vecinos

```java
boolean ascendente = false;
boolean descendente = false;
for(int i = 0; i < a.length - 1; i++){

    if(a[i] > a[i+1]){
        descendente = true;
    }

    if(a[i] < a[i+1]){
        ascendente = true;
    }
}
```

Cada comparación inspecciona una pareja contigua. Las banderas recuerdan si apareció una subida o una bajada en cualquier punto, no el sentido exclusivo de la última comparación. Las parejas iguales dejan ambas banderas como estaban.

## 5. Buscar y conservar un candidato

La búsqueda lineal visita posiciones hasta hallar un valor o llegar al final. Fragmento del ejemplo de búsqueda de números:

```java
int i = 0;
for(; i < a.length && a[i] != num; i++){}

if(i == a.length){
    System.out.println("Número no encontrado");
} else if(a[i] == num){
    System.out.println("Encontrado en la posición: " + i);
}
```

El cuerpo vacío es intencional: el avance ocurre en `i++`. Primero se comprueba el límite para evitar leer `a[a.length]`. Al salir, el índice representa el primer hallazgo o el final del arreglo. El peor caso visita todos los datos, O(n); no requiere que estén ordenados.

El ejemplo de búsqueda de texto usa `equalsIgnoreCase` durante la búsqueda. `Scanner.next` acepta nombres sin espacios. El mensaje «Número no encontrado» se imprime aunque la búsqueda procese texto; no cambia la lógica.

El ejemplo de búsqueda del máximo guarda en `max` el **índice** del mayor encontrado. Empieza en 0 y compara `a[max]` con cada siguiente valor. Así funciona incluso si todos son negativos; empezar con un valor máximo ficticio igual a cero produciría un error en ese caso. Con igualdad, su ternario escoge el índice más reciente.

### Actualizar el índice de un máximo

```java
int max = 0;
for(int i = 1; i < a.length; i++){
    max = (a[max] > a[i])? max: i;
}
System.out.println("max = " + a[max]);
```

El candidato inicial es una posición válida del arreglo. Cada vuelta conserva esa posición o la sustituye por `i`. Para `[−5, −2, −8, −1, −3]`, el índice termina en 3 y el valor impreso es −1. La comparación funciona sin suponer que exista un número positivo.

## 6. Desplazar: el sentido del recorrido evita perder datos

En el ejemplo de rotación de posiciones, el último se guarda y todos se mueven una posición a la derecha:

```java
ultimo = a[a.length-1];
for(int i = a.length -2; i >= 0; i--){
    a[i+1] = a[i];
}
a[0] = ultimo;
```

Traza con cuatro datos: `[1,2,3,4] → [1,2,3,3] → [1,2,2,3] → [1,1,2,3] → [4,1,2,3]`. Se empieza por la derecha porque copiar de izquierda a derecha sobrescribiría valores todavía pendientes de copia. Esto es una rotación: no se pierde ningún dato ni cambia la longitud.

Las otras variantes enseñan distintas precondiciones:

| Ejemplo | Estado inicial | Qué hace |
| --- | --- | --- |
| `DesplazarPosicion2` | Nueve datos en diez posiciones | Abre un hueco e inserta sin crecer |
| `DesplazarPosicion2b` | Diez datos en diez posiciones | Guarda el último, desplaza, copia a once posiciones e inserta |
| `DesplazarPosicion3` | Seis datos en siete posiciones | Busca dónde insertar para conservar orden ascendente |
| `DesplazarPosicion3b` | Siete datos y necesidad de crecer | Crea ocho posiciones y trata aparte el nuevo máximo |

Las dos variantes `3` necesitan entrada **ya ordenada**: no ordenan los datos previos. Sus límites usan el tamaño concreto del ejercicio, como `posicion < 6`; no constituyen una implementación general para cualquier longitud. Todas necesitan posiciones válidas. El mensaje al usuario no valida por sí mismo un índice negativo o demasiado grande.

### Abrir un espacio para una inserción

```java
for(int i = a.length -2; i >= posicion; i--){
    a[i+1] = a[i];
}
a[posicion] = elemento;
```

El arreglo tiene una posición libre al final. El último índice cargado es `length - 2`, y la copia empieza ahí para mover cada valor una posición a la derecha. Con `[10,20,30,0]`, tres datos cargados e inserción de 15 en el índice 1, el resultado es `[10,15,20,30]`.

### Encontrar el hueco en una secuencia ordenada

```java
posicion = 0;
while(posicion < 6 && numero > a[posicion]){
    posicion++;
}

for(int i = a.length - 2; i >= posicion; i--){
    a[i+1] = a[i];
}

a[posicion] = numero;
```

Mientras el dato nuevo supera al dato actual, la posición avanza. Al detenerse, se abre un espacio y se asigna el nuevo valor. Con seis valores ya ordenados y siete posiciones disponibles, se conserva el orden; el límite 6 pertenece a ese tamaño concreto. Si el arreglo inicial no está ordenado, el recorrido no lo corrige.

## 7. Eliminar significa mover y crear otro arreglo

El ejemplo de eliminación de posiciones copia cada siguiente elemento hacia la izquierda desde la posición elegida. Después:

```java
int[] b = new int[a.length-1];
System.arraycopy(a, 0, b, 0, b.length);
a = b;
```

`arraycopy` recibe origen, posición inicial del origen, destino, posición inicial del destino y cantidad. No redimensiona `a`: `new` crea otro objeto y `a = b` cambia la referencia local. El valor repetido que queda al final del original no se copia.

Si eliminas el índice 1 de `[10,20,30,40]`, después de desplazar queda `[10,30,40,40]` y el nuevo arreglo queda `[10,30,40]`. La longitud efectiva disminuye porque cambió el objeto referenciado.

## 8. Combinar, clasificar y acumular

El ejemplo de combinación por bloques genera `a = 1..12` y `b = 5,10,..60`. Copia bloques de tres de cada uno a un arreglo de 24 posiciones. `aux++` usa el índice actual y después avanza. El comienzo es `[1,2,3,5,10,15,4,5,6,20,25,30,...]`. No es una mezcla ordenada: solo alterna bloques, y supone longitudes compatibles y múltiplos de tres.

El ejemplo de alternancia de extremos alterna primero y último: para 1 a 10, `[1,10,2,9,3,8,4,7,5,6]`. La condición `i < numeros.length - i` cruza ambos extremos. Funciona en el caso par usado; con longitud impar, copiar dos elementos al llegar al centro repetiría ese valor e intentaría escribir más posiciones de las disponibles.

El ejemplo de clasificación por paridad hace dos pasadas: cuenta cuántos pares e impares hay, crea arreglos del tamaño exacto y luego los llena con índices separados `j` y `k`. El criterio `n % 2 == 0` también funciona para negativos. Esta clasificación conserva el orden relativo dentro de cada grupo.

El ejemplo de promedios de alumnos utiliza tres arreglos de siete notas. El mismo índice identifica al alumno en las tres materias. Calcula promedios por materia y por alumno. Su promedio general de promedios es válido porque todos los grupos tienen igual cantidad de notas; con tamaños distintos habría que ponderar por cantidad. El ID debe estar entre 0 y 6 y la entrada decimal depende del locale de `Scanner`; el ejemplo no valida el rango de las notas.

### Clasificación con tamaños exactos

```java
pares = new int[totalPares];
impares = new int[totalImpares];

int j = 0;
int k = 0;
for(int i = 0; i < a.length; i++){
    if(a[i] % 2 == 0){
        pares[j++] = a[i];
    } else {
        impares[k++] = a[i];
    }
}
```

Antes de este fragmento se cuentan los grupos. La reserva usa esas cantidades y la segunda pasada distribuye valores. `j` avanza solo cuando entra un par y `k` solo cuando entra un impar; el orden relativo dentro de cada grupo se conserva.

### Promedio por alumno en arreglos paralelos

```java
int id = s.nextInt();
double promedioAlumno = (claseHistoria[id] + claseLenguaje[id] + claseMatematicas[id])/3;
System.out.println("Promedio alumno Nro " + id + " : " + promedioAlumno);
```

El mismo índice representa al mismo alumno en historia, lenguaje y matemáticas. La media suma sus tres notas y divide por tres, con aritmética decimal porque las notas son `double`. Un ID representa aquí una posición, no un identificador persistente independiente del orden de los arreglos.

## Preguntas de repaso

1. ¿Por qué insertar hacia la derecha requiere recorrer de derecha a izquierda? **Para no sobrescribir el siguiente dato pendiente.**
2. ¿Leer índices inversos muta el arreglo? **No; los intercambios sí.**
3. ¿Qué significa `max = 0` en el ejemplo del mayor? **Que el candidato inicial está en el índice 0.**
4. ¿Puede crecer un mismo arreglo con `arraycopy`? **No; el destino debe existir y tener espacio.**
