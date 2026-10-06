# Proyecto Integrador 02 - Módulo Arreglos

## Sistema de Gestión y Análisis de Calificaciones Académicas

### Descripción General

Desarrollar un sistema completo para gestionar calificaciones de estudiantes en múltiples materias. El sistema debe realizar análisis estadístico avanzado, detectar patrones, generar rankings, y proporcionar reportes detallados utilizando técnicas de manipulación y procesamiento de arreglos.

### Objetivos de Aprendizaje

- Trabajar con arreglos de diferentes tipos de datos
- Implementar algoritmos de ordenamiento personalizados
- Realizar análisis estadístico complejo
- Detectar y procesar patrones en datos
- Combinar múltiples arreglos para análisis relacionado
- Aplicar búsqueda y filtrado con múltiples criterios

### Requerimientos Funcionales

#### 1. Estructura de Datos

Utilizar arreglos paralelos para almacenar información:

- `int[] matriculas` - Número de matrícula (1000-9999)
- `String[] nombres` - Nombre completo del estudiante
- `int[] edades` - Edad del estudiante (15-25)
- `String[] carreras` - Carrera (SISTEMAS, CIVIL, INDUSTRIAL, MECATRONICA, GESTION)
- `double[] calif1` - Calificación Parcial 1 (0-100)
- `double[] calif2` - Calificación Parcial 2 (0-100)
- `double[] calif3` - Calificación Parcial 3 (0-100)
- `double[] promedios` - Promedio calculado
- `String[] estatus` - APROBADO, REPROBADO, EXTRAORDINARIO

Capacidad: 100 estudiantes

#### 2. Menú Principal

- Registrar estudiante
- Ingresar/Modificar calificaciones
- Buscar estudiante
- Calcular promedios y estatus
- Generar rankings
- Análisis estadístico general
- Análisis por carrera
- Análisis de tendencias
- Reportes especializados
- Operaciones masivas
- Simulación de curva
- Salir

#### 3. Registro de Estudiantes

Validaciones:

- Matrícula única (4 dígitos)
- Nombre no vacío (mínimo 5 caracteres, solo letras y espacios)
- Edad en rango válido
- Carrera debe estar en el catálogo

Inicialización:

- Calificaciones en 0 al registrar
- Promedio en 0
- Estatus: "PENDIENTE"

#### 4. Ingreso de Calificaciones

**Por Estudiante Individual:**

- Buscar por matrícula
- Mostrar datos del estudiante
- Ingresar las 3 calificaciones parciales
- Validar rango 0-100
- Calcular promedio automáticamente
- Determinar estatus:
  - Promedio >= 70: APROBADO
  - Promedio >= 50 y < 70: EXTRAORDINARIO
  - Promedio < 50: REPROBADO

**Ingreso Masivo:**

- Cargar calificaciones de todos los estudiantes
- Mostrar progreso
- Validar cada calificación

#### 5. Búsqueda de Estudiantes

**Por Matrícula:**

- Búsqueda exacta
- Si está ordenado por matrícula, usar búsqueda binaria

**Por Nombre:**

- Búsqueda parcial, case-insensitive
- Listar todos los que coincidan

**Por Carrera:**

- Listar todos los estudiantes de una carrera
- Mostrar estadísticas de esa carrera

**Por Rango de Promedio:**

- Min y max
- Listar estudiantes en ese rango
- Ejemplo: buscar estudiantes entre 80 y 90

**Por Estatus:**

- APROBADO, REPROBADO, EXTRAORDINARIO
- Contar y listar

#### 6. Cálculo de Promedios

**Individual:**

- Promedio simple: (calif1 + calif2 + calif3) / 3
- Promedio ponderado opcional:
  - Parcial 1: 30%
  - Parcial 2: 30%
  - Parcial 3: 40%

**Masivo:**

- Calcular promedios de todos los estudiantes registrados
- Actualizar arreglo de promedios
- Actualizar estatus según promedio

**Redondeo:**

- Opción para redondear promedios al entero más cercano
- Mostrar impacto del redondeo en estatus

#### 7. Rankings y Clasificaciones

**Top 10 Mejores Promedios:**

- Ordenar por promedio descendente
- Mostrar primeros 10
- Incluir nombre, matrícula, promedio, carrera

**Top 5 por Carrera:**

- Para cada carrera, mostrar los 5 mejores

**Estudiantes de Honor:**

- Promedio >= 90
- Listar en orden descendente

**Estudiantes en Riesgo:**

- Promedio < 60
- Ordenar por promedio ascendente (mostrar los más críticos primero)

**Distribución de Calificaciones:**

```bash
Excelente (90-100): 15 estudiantes (15%)
Muy Bien (80-89):   25 estudiantes (25%)
Bien (70-79):       30 estudiantes (30%)
Regular (60-69):    20 estudiantes (20%)
Deficiente (<60):   10 estudiantes (10%)
```

#### 8. Análisis Estadístico General

Calcular y mostrar:

**Medidas de Tendencia Central:**

- Promedio general de todos los estudiantes
- Mediana (valor central al ordenar)
- Moda (promedio más frecuente)

**Medidas de Dispersión:**

- Rango (promedio máximo - promedio mínimo)
- Varianza
- Desviación estándar

**Análisis por Parcial:**

- Para cada parcial (1, 2, 3):
  - Promedio del parcial
  - Calificación más alta
  - Calificación más baja
  - Cuántos aprobaron ese parcial (>= 70)

**Comparación entre Parciales:**

- ¿En cuál parcial hubo mejor desempeño?
- ¿Tendencia ascendente o descendente?

**Tasas de Aprobación:**

- Porcentaje de aprobados
- Porcentaje de reprobados
- Porcentaje en extraordinario

#### 9. Análisis por Carrera

Para cada carrera mostrar:

- Total de estudiantes inscritos
- Promedio general de la carrera
- Mejor y peor estudiante
- Tasa de aprobación
- Distribución de calificaciones
- Comparación con promedio general

Ranking de Carreras:

- Ordenar carreras por promedio general
- Identificar carrera con mejor desempeño

#### 10. Análisis de Tendencias

**Evolución de Calificaciones:**

- Para cada estudiante, detectar tendencia:
  - MEJORANDO: calif1 < calif2 < calif3
  - EMPEORANDO: calif1 > calif2 > calif3
  - ESTABLE: variación mínima (<5 puntos)
  - IRREGULAR: sin patrón claro

**Identificación de Patrones:**

- Estudiantes con calificación perfecta en algún parcial
- Estudiantes con calificaciones idénticas en todos los parciales
- Estudiantes con variación alta (>30 puntos entre parciales)

**Predicción:**

- Si la tendencia continúa, ¿cuál sería la calificación en un parcial 4?
- Usar regresión lineal simple

#### 11. Reportes Especializados

**Reporte por Estudiante:**

```bash
=================================================
           REPORTE INDIVIDUAL
=================================================
Matrícula: 1234
Nombre: Juan Pérez García
Carrera: SISTEMAS
Edad: 20 años

CALIFICACIONES:
  Parcial 1: 85.0
  Parcial 2: 90.0
  Parcial 3: 95.0

PROMEDIO: 90.0
ESTATUS: APROBADO
TENDENCIA: MEJORANDO (+10 puntos)

POSICIÓN: 3 de 100 estudiantes
POSICIÓN EN CARRERA: 1 de 30

OBSERVACIONES:
- Estudiante de honor (>= 90)
- Mejor de su carrera
- Tendencia positiva
=================================================
```

**Reporte Grupal por Carrera:**

- Listado completo de estudiantes
- Estadísticas consolidadas
- Gráfico de distribución (ASCII art)

**Reporte Comparativo:**

- Comparar desempeño entre carreras
- Tabla comparativa

#### 12. Operaciones Masivas

**Ajuste de Curva (Curving):**

- Agregar puntos a todos (ej: +5 puntos)
- Multiplicar por factor (ej: * 1.05)
- Validar que no exceda 100
- Recalcular promedios y estatus

**Normalización:**

- Convertir calificaciones a escala diferente
- Ejemplo: convertir escala 0-100 a escala 0-10
- O convertir a escala 0-5

**Eliminación de Calificación Más Baja:**

- Para cada estudiante, eliminar la calificación parcial más baja
- Recalcular promedio solo con las 2 mejores
- Mostrar impacto en promedios

**Bonificación por Asistencia:**

- Simular agregar puntos extra
- Solicitar matrícula y puntos a agregar
- Aplicar a un parcial específico

#### 13. Simulación de Curva

Aplicar diferentes métodos de curva:

**Curva Lineal:**

- Todos reciben los mismos puntos extra

**Curva Proporcional:**

- Puntos extra proporcionales al esfuerzo
- Quien tiene 50 recibe más ayuda que quien tiene 90

**Curva por Desviación Estándar:**

- Basado en qué tan lejos está del promedio
- Ajustar para que el promedio general sea 75

**Curva de Raíz Cuadrada:**

- Nueva calificación = sqrt(calif_original * 100)
- Beneficia más a calificaciones bajas

Mostrar antes y después de aplicar curva.

### Operaciones de Manipulación de Arreglos

1. **Ordenamiento Multinivel:**

```java
// Ordenar por carrera, y dentro de cada carrera por promedio
// Usar ordenamiento estable
```

1. **Filtrado y Copia:**

```java
// Crear arreglo temporal con estudiantes que cumplen criterio
double[] aprobados = filtrarPorEstatus("APROBADO");
```

1. **Detección de Orden:**

```java
// Detectar si calificaciones están en orden ascendente/descendente
boolean esMejorando = detectarTendencia(calif1[i], calif2[i], calif3[i]);
```

1. **Intercambio Sincronizado:**

```java
// Al ordenar, mantener todos los arreglos sincronizados
void intercambiar(int i, int j) {
    // Intercambiar en todos los arreglos paralelos
}
```

### Algoritmos Requeridos

1. **Búsqueda Binaria:**
   - Para búsqueda por matrícula en arreglo ordenado

2. **Ordenamiento Burbuja Optimizado:**
   - Para ordenar por promedio
   - Detectar si ya está ordenado (early termination)

3. **Ordenamiento por Selección:**
   - Para ordenar por nombre alfabéticamente

4. **Encontrar Máximo/Mínimo:**
   - Una pasada por el arreglo

5. **Cálculo de Mediana:**
   - Ordenar arreglo de promedios
   - Tomar valor central

6. **Cálculo de Moda:**
   - Contar frecuencia de cada promedio
   - Encontrar el más frecuente

7. **Cálculo de Varianza:**

```java
suma = 0;
for (promedio en promedios) {
    suma += (promedio - media) * (promedio - media);
}
varianza = suma / n;
```

1. **Detección de Patrones:**
   - Comparar elementos consecutivos
   - Identificar secuencias

### Casos de Prueba

#### Caso 1: Cálculo de Promedio

Calificaciones: 80, 85, 90
Expected: Promedio = 85.0, Estatus = APROBADO

#### Caso 2: Aplicar Curva

Calificación original: 65, Curva: +10
Expected: 75, cambio de EXTRAORDINARIO a APROBADO

#### Caso 3: Encontrar Mediana

Promedios: [60, 65, 70, 75, 80, 85, 90]
Expected: Mediana = 75

#### Caso 4: Tendencia

Parciales: 60, 70, 80
Expected: MEJORANDO

#### Caso 5: Ordenamiento

Antes: [(1001, 75), (1002, 90), (1003, 60)]
Después (por promedio desc): [(1002, 90), (1001, 75), (1003, 60)]

### Estructura de Código Sugerida

```java
public class SistemaCalificaciones {
    static int[] matriculas = new int[100];
    static String[] nombres = new String[100];
    static int[] edades = new int[100];
    static String[] carreras = new String[100];
    static double[] calif1 = new double[100];
    static double[] calif2 = new double[100];
    static double[] calif3 = new double[100];
    static double[] promedios = new double[100];
    static String[] estatus = new String[100];
    static int totalEstudiantes = 0;

    static final String[] CARRERAS_VALIDAS = {
        "SISTEMAS", "CIVIL", "INDUSTRIAL", "MECATRONICA", "GESTION"
    };

    public static void main(String[] args) { }

    // Métodos de gestión
    static void registrarEstudiante() { }
    static void ingresarCalificaciones() { }
    static void calcularPromedios() { }

    // Métodos de búsqueda
    static int buscarPorMatricula(int mat) { }
    static void buscarPorNombre(String nombre) { }

    // Métodos de análisis
    static double calcularMedia() { }
    static double calcularMediana() { }
    static double calcularDesviacionEstandar() { }
    static void analizarTendencias() { }

    // Métodos de ordenamiento
    static void ordenarPorPromedio() { }
    static void ordenarPorCarrera() { }

    // Métodos de reportes
    static void generarTopEstudiantes() { }
    static void reportePorCarrera(String carrera) { }
    static void distribucionCalificaciones() { }

    // Métodos de manipulación
    static void aplicarCurva(double puntos) { }
    static void eliminarCalifMasBaja() { }
    static void intercambiar(int i, int j) { }
}
```

### Criterios de Evaluación

#### Funcionalidad (40%)

- Todas las operaciones funcionan correctamente
- Cálculos estadísticos precisos
- Rankings generados correctamente
- Curvas aplicadas apropiadamente

#### Uso de Conceptos (40%)

- Implementación de algoritmos de búsqueda (lineal y binaria)
- Al menos 2 algoritmos de ordenamiento
- Manipulación correcta de arreglos paralelos
- Análisis estadístico completo
- Detección de patrones implementada

#### Calidad del Código (20%)

- Código modular y organizado
- Validaciones robustas
- Interfaz clara y usable
- Comentarios en secciones clave

### Entregables

1. Código fuente completo
2. Documento con análisis de complejidad de algoritmos
3. Casos de prueba con resultados
4. Manual de usuario

### Tiempo Estimado

14-18 horas de desarrollo

### Extensiones Opcionales

1. Implementar MergeSort para ordenamiento eficiente
2. Agregar calificación final (examen final) con peso 40%
3. Sistema de asistencias que afecte la calificación
4. Generar gráficos ASCII de distribución
5. Exportar reportes a formato texto estructurado
6. Implementar sistema de créditos por materia
7. Calcular GPA (Grade Point Average)
8. Historial de modificaciones de calificaciones
