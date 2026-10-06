# Proyecto Integrador 01 - Módulo Arreglos

## Sistema de Gestión de Inventario de Productos

### Descripción General

Desarrollar un sistema de gestión de inventario para una tienda que permita administrar productos mediante arreglos. El sistema debe manejar múltiples arreglos paralelos para almacenar diferentes características de los productos y realizar operaciones de búsqueda, ordenamiento, análisis estadístico y manipulación de datos.

### Objetivos de Aprendizaje

- Declarar e inicializar arreglos de diferentes tipos
- Implementar búsqueda lineal y binaria
- Aplicar algoritmos de ordenamiento (burbuja, selección, inserción)
- Manipular arreglos (insertar, eliminar, desplazar elementos)
- Realizar análisis estadístico sobre arreglos
- Combinar múltiples arreglos para gestión de datos relacionados
- Detectar y manejar arreglos ordenados

### Requerimientos Funcionales

#### 1. Estructura de Datos

Utilizar arreglos paralelos para almacenar información de productos:

- `int[] codigos` - Código único del producto (100-999)
- `String[] nombres` - Nombre del producto
- `String[] categorias` - Categoría (ELECTRONICA, ROPA, ALIMENTOS, HOGAR, DEPORTES)
- `double[] precios` - Precio unitario
- `int[] stock` - Cantidad en inventario
- `double[] descuentos` - Porcentaje de descuento (0-50)

Capacidad máxima del inventario: 50 productos

#### 2. Menú Principal

- Agregar producto nuevo
- Buscar producto
- Modificar producto existente
- Eliminar producto
- Listar productos
- Ordenar productos
- Análisis estadístico
- Reporte de productos
- Gestión de stock
- Aplicar descuentos masivos
- Salir

#### 3. Agregar Producto

Validaciones:

- Código único (no puede repetirse)
- Código en rango 100-999
- Nombre no vacío (mínimo 3 caracteres)
- Categoría debe ser una de las permitidas
- Precio mayor a 0
- Stock no negativo
- Descuento entre 0 y 50

Inserción:

- Agregar al final del arreglo si hay espacio
- Mostrar mensaje si el inventario está lleno
- Incrementar contador de productos

#### 4. Búsqueda de Productos

Implementar búsqueda por:

**Por Código (búsqueda exacta):**

- Búsqueda lineal
- Si el arreglo está ordenado por código, usar búsqueda binaria
- Mostrar todos los datos del producto

**Por Nombre (búsqueda parcial):**

- Case-insensitive
- Mostrar todos los productos que contengan el texto
- Ejemplo: buscar "lap" encuentra "Laptop", "Lámpara"

**Por Categoría:**

- Listar todos los productos de una categoría
- Mostrar cantidad total encontrada

**Por Rango de Precio:**

- Solicitar precio mínimo y máximo
- Mostrar productos en ese rango

**Productos con Stock Bajo:**

- Definir umbral (ej: stock < 10)
- Listar productos que necesitan reabastecimiento

#### 5. Modificar Producto

- Buscar producto por código
- Mostrar datos actuales
- Permitir modificar:
  - Nombre
  - Categoría
  - Precio
  - Stock (agregar o reducir)
  - Descuento
- Validar nuevos valores
- Confirmar cambios

#### 6. Eliminar Producto

- Buscar por código
- Mostrar información del producto
- Solicitar confirmación
- Eliminar usando desplazamiento de elementos
- Actualizar contador de productos
- Mostrar mensaje de éxito

#### 7. Listar Productos

Implementar diferentes vistas:

**Listado Completo:**

```bash
=================================================================
COD  | NOMBRE              | CATEGORÍA    | PRECIO  | STOCK | DESC
=================================================================
101  | Laptop HP           | ELECTRONICA  | 15000.0 | 25    | 10%
102  | Camisa Polo         | ROPA         | 450.00  | 100   | 5%
103  | Arroz 5kg           | ALIMENTOS    | 125.50  | 200   | 0%
...
=================================================================
Total de productos: 25
=================================================================
```

**Listado por Categoría:**

- Agrupar y mostrar productos de cada categoría
- Subtotales por categoría

**Listado con Descuento:**

- Mostrar solo productos que tienen descuento activo
- Precio original vs precio con descuento

**Productos Agotados:**

- Listar productos con stock = 0

#### 8. Ordenamiento de Productos

Implementar ordenamiento por diferentes criterios:

**Por Código:**

- Algoritmo: Burbuja optimizado
- Orden ascendente o descendente

**Por Nombre:**

- Algoritmo: Selección
- Orden alfabético (A-Z o Z-A)

**Por Precio:**

- Algoritmo: Inserción
- Menor a mayor o mayor a menor

**Por Stock:**

- Algoritmo: Burbuja
- Menor a mayor (identificar productos críticos)

**Por Categoría y luego por Precio:**

- Ordenamiento multinivel
- Agrupar por categoría, dentro de cada grupo ordenar por precio

Consideraciones:

- Mantener sincronizados todos los arreglos paralelos
- Mostrar arreglo antes y después de ordenar
- Indicar el algoritmo utilizado

#### 9. Análisis Estadístico

Calcular y mostrar:

**Estadísticas Generales:**

- Total de productos en inventario
- Valor total del inventario (suma de precio * stock)
- Producto más caro y más barato
- Producto con mayor y menor stock
- Promedio de precios
- Promedio de stock

**Por Categoría:**

- Cantidad de productos por categoría
- Valor total por categoría
- Categoría con mayor valor en inventario
- Porcentaje que representa cada categoría del total

**Análisis de Precios:**

- Distribución de precios por rangos:
  - 0-100: X productos
  - 101-500: Y productos
  - 501-1000: Z productos
  - Más de 1000: W productos

**Análisis de Descuentos:**

- Productos con descuento vs sin descuento
- Promedio de descuento aplicado
- Ahorro total si se compra todo con descuento

#### 10. Reporte de Productos

Generar reportes especializados:

**Top 10 Productos Más Caros:**

- Ordenar por precio (descendente)
- Mostrar primeros 10

**Top 10 Productos con Mayor Stock:**

- Útil para identificar sobrestock

**Productos Críticos:**

- Stock por debajo del mínimo
- Sugerencia de cantidad a ordenar

**Reporte de Ventas Potenciales:**

- Si se vendiera todo el stock actual
- Calcular ganancia total
- Aplicar descuentos si existen

#### 11. Gestión de Stock

**Actualización Masiva de Stock:**

- Incrementar/decrementar stock de todos los productos de una categoría
- Ejemplo: "Agregar 50 unidades a todos los ALIMENTOS"

**Reabastecimiento:**

- Ingresar código de producto
- Agregar cantidad al stock existente
- Registrar el movimiento

**Venta de Producto:**

- Ingresar código y cantidad
- Validar stock suficiente
- Reducir stock
- Mostrar total a pagar (precio * cantidad - descuento)

**Transferencia entre Productos:**

- Reducir stock de un producto
- Incrementar stock de otro (si aplica)

#### 12. Descuentos Masivos

**Aplicar Descuento por Categoría:**

- Seleccionar categoría
- Aplicar porcentaje de descuento a todos los productos
- Validar que no exceda 50%

**Descuento por Stock Alto:**

- Productos con stock > umbral reciben descuento automático
- Ejemplo: stock > 100 -> 15% descuento

**Eliminar Descuentos:**

- Opción para resetear todos los descuentos a 0
- O eliminar descuentos de una categoría específica

### Requerimientos Técnicos

#### Conceptos del Módulo a Aplicar

1. **Declaración e Inicialización:**

```java
int[] codigos = new int[50];
String[] nombres = new String[50];
double[] precios = new double[50];
// Inicialización con valores predeterminados
Arrays.fill(descuentos, 0.0);
```

1. **Iteración:**

- For clásico para índices
- For-each para lectura (cuando no se necesita índice)
- While para búsquedas

1. **Búsqueda:**

- Búsqueda lineal (arreglo no ordenado)
- Búsqueda binaria (arreglo ordenado)
- Búsqueda con condiciones múltiples

1. **Ordenamiento:**

- Burbuja (Bubble Sort)
- Selección (Selection Sort)
- Inserción (Insertion Sort)
- Arrays.sort() como comparación

1. **Manipulación:**

- Insertar al final
- Eliminar con desplazamiento a la izquierda
- Desplazar elementos a la derecha para inserción
- Intercambiar elementos (swap)

1. **Arreglos Paralelos:**

- Mantener sincronización al ordenar
- Método auxiliar para intercambiar en todos los arreglos

```java
private static void intercambiar(int i, int j) {
    // Intercambiar en todos los arreglos
    int tempCod = codigos[i];
    codigos[i] = codigos[j];
    codigos[j] = tempCod;
    // Repetir para cada arreglo...
}
```

1. **Análisis de Datos:**

- Encontrar máximo y mínimo
- Calcular promedio
- Sumatoria
- Conteo condicional
- Detección de patrones

1. **Combinación de Arreglos:**

- Crear arreglos temporales para filtrado
- Copiar subarreglos
- Unir información de múltiples arreglos

### Casos de Prueba

#### Caso 1: Agregar Producto

Input:

- Código: 150
- Nombre: "Laptop Dell"
- Categoría: "ELECTRONICA"
- Precio: 18500.00
- Stock: 15
- Descuento: 10

Expected: Producto agregado exitosamente

#### Caso 2: Búsqueda Binaria

Precondición: Arreglo ordenado por código: [101, 105, 110, 115, 120]
Buscar: 110
Expected: Encontrado en índice 2 (usando búsqueda binaria)

#### Caso 3: Eliminar Producto

Arreglo: [101, 102, 103, 104, 105]
Eliminar: 103
Expected: [101, 102, 104, 105, 0]

#### Caso 4: Ordenamiento Burbuja

Input: precios = [150.0, 320.0, 80.0, 500.0, 45.0]
Expected (ascendente): [45.0, 80.0, 150.0, 320.0, 500.0]

#### Caso 5: Análisis Estadístico

Productos: 5
Precios: [100, 200, 300, 400, 500]
Expected:

- Promedio: 300.0
- Máximo: 500.0
- Mínimo: 100.0

#### Caso 6: Venta con Descuento

Producto: Precio=1000, Descuento=20%, Cantidad=3
Expected: Total = (1000 x 0.8) x 3 = 2400.0

### Estructura de Código Sugerida

```java
public class SistemaInventario {
    // Arreglos paralelos
    static int[] codigos = new int[50];
    static String[] nombres = new String[50];
    static String[] categorias = new String[50];
    static double[] precios = new double[50];
    static int[] stock = new int[50];
    static double[] descuentos = new double[50];

    static int totalProductos = 0;
    static final String[] CATEGORIAS_VALIDAS = {
        "ELECTRONICA", "ROPA", "ALIMENTOS", "HOGAR", "DEPORTES"
    };

    // Métodos principales
    public static void main(String[] args) { }
    static void mostrarMenu() { }
    static void agregarProducto() { }
    static int buscarPorCodigo(int codigo) { }
    static void buscarPorNombre(String nombre) { }
    static void eliminarProducto() { }
    static void ordenarPorPrecio() { }
    static void ordenamientoBurbuja() { }
    static void mostrarEstadisticas() { }
    static void intercambiar(int i, int j) { }
    // ... más métodos
}
```

### Criterios de Evaluación

#### Funcionalidad (40%)

- Todas las operaciones implementadas correctamente
- Búsqueda y ordenamiento funcionan
- Manipulación de arreglos sin errores
- Validaciones efectivas

#### Uso de Conceptos (40%)

- Implementación correcta de algoritmos de búsqueda
- Al menos 2 algoritmos de ordenamiento implementados
- Manejo apropiado de arreglos paralelos
- Uso de for, for-each, while según corresponda
- Operaciones de inserción y eliminación correctas

#### Calidad del Código (20%)

- Código organizado en métodos
- Nombres descriptivos
- Comentarios en algoritmos
- Manejo de errores (índices fuera de rango, etc.)

### Entregables

1. Código fuente completo
2. Documento con casos de prueba
3. Capturas de pantalla de ejecución

### Tiempo Estimado

12-16 horas de desarrollo

### Extensiones Opcionales

1. Implementar QuickSort o MergeSort
2. Agregar historial de movimientos (ventas/reabastecimientos)
3. Implementar sistema de proveedores
4. Agregar fecha de vencimiento para productos perecederos
5. Crear sistema de alertas automáticas
6. Implementar búsqueda con múltiples criterios simultáneos
