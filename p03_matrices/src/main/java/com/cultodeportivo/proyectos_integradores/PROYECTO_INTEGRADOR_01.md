# Proyecto Integrador 01 - Módulo Matrices

## Calculadora Avanzada de Matrices y Procesador de Datos Bidimensionales

### Descripción General

Desarrollar una aplicación que funcione como calculadora completa de matrices, capaz de realizar operaciones algebraicas, transformaciones, análisis de propiedades y procesamiento de datos bidimensionales. El sistema debe manejar diferentes tipos de matrices y proporcionar visualización clara de resultados.

### Objetivos de Aprendizaje

- Declarar e inicializar matrices de diferentes dimensiones
- Implementar operaciones algebraicas matriciales
- Realizar transformaciones de matrices (transposición, rotación)
- Detectar propiedades especiales (identidad, simétrica, diagonal)
- Recorrer matrices con bucles anidados
- Manipular índices bidimensionales
- Trabajar con matrices irregulares (jagged arrays)
- Realizar análisis de datos bidimensionales

### Requerimientos Funcionales

#### 1. Menú Principal

- Crear/Ingresar matriz
- Operaciones aritméticas
- Transformaciones
- Propiedades y validaciones
- Búsqueda en matrices
- Análisis estadístico
- Operaciones especiales
- Matrices predefinidas
- Visualización avanzada
- Salir

#### 2. Creación e Ingreso de Matrices

**Métodos de Creación:**

- Manual: solicitar dimensiones y valores elemento por elemento
- Automática: generar con números aleatorios (rango configurable)
- Por filas: ingresar fila completa separada por espacios
- Matriz identidad de orden N
- Matriz nula (todos ceros) de dimensión M x N
- Matriz con valor constante

**Validaciones:**

- Dimensiones válidas (1-10 para filas y columnas)
- Valores numéricos correctos
- Confirmar antes de sobrescribir matriz existente

**Almacenamiento:**

- Mantener hasta 5 matrices en memoria (A, B, C, D, E)
- Mostrar qué matrices están actualmente cargadas

#### 3. Operaciones Aritméticas

**Suma de Matrices:**

- Validar dimensiones iguales (M x N + M x N)
- C = A + B
- Mostrar resultado

**Resta de Matrices:**

- Validar dimensiones iguales
- C = A - B

**Multiplicación Escalar:**

- Solicitar matriz y escalar
- Multiplicar cada elemento por el escalar
- C = k * A

**Multiplicación de Matrices:**

- Validar compatibilidad (columnas de A = filas de B)
- Implementar algoritmo de multiplicación

```bash
- C[i][j] = suma de A[i][k] * B[k][j]
```

- Mostrar paso a paso (opcional)

**División Escalar:**

- Dividir cada elemento entre un escalar
- Validar división por cero

**Potencia de Matriz:**

- A^n donde n es entero positivo
- Validar que sea matriz cuadrada
- Usar multiplicación repetida

#### 4. Transformaciones

**Transposición:**

- Convertir filas en columnas

```bash
- A[i][j] -> A^T[j][i]
```

- Mantener matriz original y crear transpuesta

**Rotación:**

- 90 grados horario
- 90 grados antihorario
- 180 grados
- Mostrar matriz rotada

**Reflexión:**

- Espejo horizontal (invertir filas)
- Espejo vertical (invertir columnas)
- Espejo diagonal (transpuesta)

**Inversión de Elementos:**

- Invertir orden de todos los elementos
- Primer elemento -> último, etc.

#### 5. Propiedades y Validaciones

**Detectar Tipo de Matriz:**

- Matriz Cuadrada (filas = columnas)
- Matriz Rectangular
- Matriz Fila (1 x N)
- Matriz Columna (M x 1)
- Matriz Nula (todos ceros)
- Matriz Identidad
- Matriz Diagonal (solo diagonal principal tiene valores)
- Matriz Triangular Superior
- Matriz Triangular Inferior
- Matriz Simétrica (A = A^T)
- Matriz Antisimétrica (A = -A^T)
- Matriz Ortogonal (A * A^T = I)

**Validación de Igualdad:**

- Comparar dos matrices elemento por elemento
- Determinar si son idénticas

**Cálculo de Propiedades:**

- Traza (suma de diagonal principal, solo cuadradas)
- Norma (diferentes tipos: Frobenius, infinito)
- Rango aproximado (número de filas/columnas no nulas)

#### 6. Búsqueda en Matrices

**Buscar Valor Específico:**

- Búsqueda lineal en toda la matriz
- Mostrar todas las posiciones i j donde aparece
- Contar ocurrencias

**Buscar en Fila o Columna:**

- Buscar en una fila específica
- Buscar en una columna específica

**Encontrar Máximo y Mínimo:**

- En toda la matriz
- En una fila específica
- En una columna específica
- En diagonal principal
- En diagonal secundaria

**Buscar Elemento de Silla de Montar:**

- Elemento que es máximo en su fila y mínimo en su columna
- (O viceversa)

#### 7. Análisis Estadístico

**Por Fila:**

- Suma de cada fila
- Promedio de cada fila
- Máximo y mínimo de cada fila
- Mediana de cada fila

**Por Columna:**

- Suma de cada columna
- Promedio de cada columna
- Máximo y mínimo de cada columna

**Diagonales:**

- Suma diagonal principal
- Suma diagonal secundaria
- Promedio de diagonales

**General:**

- Suma total de todos los elementos
- Promedio general
- Desviación estándar
- Varianza
- Rango (máximo - mínimo)

**Distribución:**

- Contar positivos, negativos y ceros
- Contar pares e impares
- Frecuencia de cada valor

#### 8. Operaciones Especiales

**Submatrices:**

- Extraer submatriz dados rangos de filas y columnas
- Ejemplo: extraer filas 1-3, columnas 2-4

**Concatenación:**

- Horizontal: [A | B] (mismo número de filas)
- Vertical: [A sobre B] (mismo número de columnas)

**Eliminación:**

- Eliminar una fila específica
- Eliminar una columna específica

**Intercambio:**

- Intercambiar dos filas
- Intercambiar dos columnas

**Relleno de Bordes:**

- Crear marco de un valor específico
- Patrón de tablero de ajedrez

**Reorganización:**

- Ordenar cada fila independientemente
- Ordenar cada columna independientemente
- Ordenar toda la matriz como arreglo unidimensional

#### 9. Matrices Predefinidas y Patrones

**Matrices Especiales:**

- Matriz Identidad I_n
- Matriz de unos (todos 1)
- Matriz de Pascal
- Matriz Diagonal con valores específicos
- Matriz de Hilbert
- Matriz Mágica (suma filas = suma columnas = suma diagonales)

**Patrones:**

- Patrón de cruz
- Patrón de X (diagonales)
- Patrón de marco
- Patrón de espiral
- Patrón en zigzag
- Patrón de tablero de ajedrez

**Ejemplo Patrón Espiral (4x4):**

```bash
 1  2  3  4
12 13 14  5
11 16 15  6
10  9  8  7
```

#### 10. Visualización Avanzada

**Formato Básico:**

```bash
Matriz A (3x3):
  1.0   2.0   3.0
  4.0   5.0   6.0
  7.0   8.0   9.0
```

**Formato con Bordes:**

```bash
┌─────────────┐
│ 1.0  2.0  3.0│
│ 4.0  5.0  6.0│
│ 7.0  8.0  9.0│
└─────────────┘
```

**Alineación:**

- Alinear columnas correctamente
- Padding para números de diferente longitud
- Formatear decimales (precisión configurable)

**Resaltado:**

- Resaltar diagonal principal
- Resaltar elemento específico
- Usar símbolos especiales para ceros

**Modo Compacto:**

- Para matrices grandes, mostrar solo esquinas

```bash
Matriz (100x100):
  1.0  2.0  ...  99.0 100.0
  ...
 9901 9902 ... 9999 10000
```

### Requerimientos Técnicos

#### Conceptos del Módulo a Aplicar

1. **Declaración e Inicialización:**

```java
// Matriz rectangular
double[][] matrizA = new double[3][4];

// Inicialización directa
int[][] matrizB = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Matriz irregular (jagged array)
int[][] irregular = new int[3][];
irregular[0] = new int[2];
irregular[1] = new int[4];
irregular[2] = new int[3];
```

1. **Recorrido con Bucles Anidados:**

```java
// For clásico
for (int i = 0; i < matriz.length; i++) {
    for (int j = 0; j < matriz[i].length; j++) {
        // Procesar matriz[i][j]
    }
}

// For-each
for (double[] fila : matriz) {
    for (double elemento : fila) {
        // Procesar elemento
    }
}
```

1. **Operaciones por Fila y Columna:**

```java
// Suma de fila i
double sumaFila = 0;
for (int j = 0; j < matriz[i].length; j++) {
    sumaFila += matriz[i][j];
}

// Suma de columna j
double sumaColumna = 0;
for (int i = 0; i < matriz.length; i++) {
    sumaColumna += matriz[i][j];
}
```

1. **Diagonales:**

```java
// Diagonal principal (i == j)
for (int i = 0; i < n; i++) {
    suma += matriz[i][i];
}

// Diagonal secundaria (i + j == n - 1)
for (int i = 0; i < n; i++) {
    suma += matriz[i][n - 1 - i];
}
```

1. **Transposición:**

```java
double[][] transpuesta = new double[columnas][filas];
for (int i = 0; i < filas; i++) {
    for (int j = 0; j < columnas; j++) {
        transpuesta[j][i] = original[i][j];
    }
}
```

1. **Multiplicación de Matrices:**

```java
// A(m×n) * B(n×p) = C(m×p)
for (int i = 0; i < m; i++) {
    for (int j = 0; j < p; j++) {
        C[i][j] = 0;
        for (int k = 0; k < n; k++) {
            C[i][j] += A[i][k] * B[k][j];
        }
    }
}
```

1. **Detección de Propiedades:**

```java
// Simetría
boolean esSimetrica = true;
for (int i = 0; i < n && esSimetrica; i++) {
    for (int j = 0; j < n && esSimetrica; j++) {
        if (matriz[i][j] != matriz[j][i]) {
            esSimetrica = false;
        }
    }
}
```

1. **Matrices Irregulares:**

```java
// Diferentes longitudes de fila
int[][] irregular = new int[3][];
irregular[0] = new int[2];  // Primera fila: 2 elementos
irregular[1] = new int[5];  // Segunda fila: 5 elementos
irregular[2] = new int[3];  // Tercera fila: 3 elementos
```

### Casos de Prueba

#### Caso 1: Suma de Matrices

A = [[1, 2], [3, 4]]
B = [[5, 6], [7, 8]]
Expected: C = [[6, 8], [10, 12]]

#### Caso 2: Multiplicación

A = [[1, 2], [3, 4]]
B = [[2, 0], [1, 3]]
Expected: C = [[4, 6], [10, 12]]

#### Caso 3: Transposición

A = [[1, 2, 3], [4, 5, 6]]
Expected: A^T = [[1, 4], [2, 5], [3, 6]]

#### Caso 4: Detección de Identidad

I = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]
Expected: Es matriz identidad = true

#### Caso 5: Simetría

A = [[1, 2, 3], [2, 4, 5], [3, 5, 6]]
Expected: Es simétrica = true

#### Caso 6: Búsqueda

Matriz = [[1, 5, 3], [5, 2, 5], [7, 5, 9]]
Buscar: 5
Expected: Encontrado en (0,1), (1,0), (1,2), (2,1) - 4 ocurrencias

#### Caso 7: Traza

A = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
Expected: Traza = 1 + 5 + 9 = 15

### Estructura de Código Sugerida

```java
public class CalculadoraMatrices {
    static double[][] matrizA;
    static double[][] matrizB;
    static double[][] matrizC;
    static double[][] matrizD;
    static double[][] matrizE;

    public static void main(String[] args) {
        // Menú principal
    }

    // Métodos de creación
    static double[][] crearMatrizManual() { }
    static double[][] crearMatrizAleatoria(int filas, int cols) { }
    static double[][] crearIdentidad(int n) { }

    // Operaciones aritméticas
    static double[][] sumar(double[][] a, double[][] b) { }
    static double[][] restar(double[][] a, double[][] b) { }
    static double[][] multiplicarEscalar(double[][] matriz, double escalar) { }
    static double[][] multiplicar(double[][] a, double[][] b) { }

    // Transformaciones
    static double[][] transponer(double[][] matriz) { }
    static double[][] rotar90Horario(double[][] matriz) { }
    static double[][] reflejarHorizontal(double[][] matriz) { }

    // Propiedades
    static boolean esIdentidad(double[][] matriz) { }
    static boolean esSimetrica(double[][] matriz) { }
    static boolean esTriangularSuperior(double[][] matriz) { }
    static double calcularTraza(double[][] matriz) { }

    // Búsqueda
    static void buscarElemento(double[][] matriz, double valor) { }
    static double encontrarMaximo(double[][] matriz) { }
    static double encontrarMinimo(double[][] matriz) { }

    // Análisis
    static double[] sumaFilas(double[][] matriz) { }
    static double[] sumaColumnas(double[][] matriz) { }
    static double promedioGeneral(double[][] matriz) { }

    // Visualización
    static void imprimirMatriz(double[][] matriz) { }
    static void imprimirConBordes(double[][] matriz) { }

    // Utilidades
    static boolean dimensionesIguales(double[][] a, double[][] b) { }
    static boolean esMultiplicable(double[][] a, double[][] b) { }
}
```

### Criterios de Evaluación

#### Funcionalidad (40%)

- Todas las operaciones aritméticas correctas
- Transformaciones precisas
- Detección de propiedades funcional
- Búsqueda y análisis completo

#### Uso de Conceptos (40%)

- Bucles anidados correctamente implementados
- Manejo de índices bidimensionales
- Operaciones sobre filas/columnas
- Trabajo con diagonales
- Validaciones de dimensiones

#### Calidad del Código (20%)

- Código modular (funciones separadas)
- Validaciones robustas
- Visualización clara
- Comentarios explicativos

### Entregables

1. Código fuente completo
2. Documentación de algoritmos implementados
3. Casos de prueba ejecutados
4. Manual de usuario

### Tiempo Estimado

12-16 horas de desarrollo

### Extensiones Opcionales

1. Implementar determinante de matrices (método de Sarrus o cofactores)
2. Calcular matriz inversa
3. Resolver sistemas de ecuaciones lineales (Gauss-Jordan)
4. Implementar descomposición LU
5. Calcular valores y vectores propios (método de potencias)
6. Operaciones con matrices de números complejos
7. Guardar/cargar matrices desde archivo
8. Generar gráficos ASCII de visualización de datos
