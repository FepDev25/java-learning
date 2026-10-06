# Proyecto Integrador - Módulo API Colecciones

## Sistema de Gestión de Biblioteca Digital y Comunidad de Lectores

### Descripción General

Desarrollar un sistema completo de biblioteca digital que gestione libros, autores, lectores, préstamos y recomendaciones. El sistema debe utilizar intensivamente las colecciones de Java (List, Set, Map) para almacenar y organizar información, implementar comparaciones con Comparable y Comparator, y proporcionar funcionalidades avanzadas de búsqueda y análisis.

### Objetivos de Aprendizaje

- Utilizar List (ArrayList, LinkedList) para gestión de datos ordenados
- Aplicar Set (HashSet, TreeSet) para garantizar unicidad y ordenamiento
- Implementar Map (HashMap, TreeMap) para relaciones clave-valor
- Crear clases que implementen Comparable
- Definir múltiples Comparator personalizados
- Realizar operaciones complejas con colecciones
- Iterar sobre colecciones de diferentes formas
- Combinar diferentes tipos de colecciones según necesidades

### Arquitectura del Sistema

#### Modelo de Datos

Clase: Libro implements Comparable Libro

```java
Atributos:
- isbn (String) - identificador único
- titulo (String)
- autores (Set<Autor>) - puede tener múltiples autores
- generos (Set<Genero>) - puede pertenecer a múltiples géneros
- añoPublicacion (int)
- editorial (String)
- numeroPaginas (int)
- idioma (String)
- copias Disponibles (int)
- copiasTotal (int)
- calificacionPromedio (double)
- calificaciones (List<Calificacion>)
- tags (Set<String>) - etiquetas descriptivas

Métodos:
- compareTo(Libro otro) - ordenar por título naturalmente
- equals y hashCode - basados en ISBN
- toString() - representación completa
```

Clase: Autor implements Comparable Autor

```java
Atributos:
- id (int)
- nombre (String)
- apellido (String)
- nacionalidad (String)
- fechaNacimiento (LocalDate)
- biografia (String)
- librosEscritos (Set<String>) - ISBNs de libros
- premios (List<String>)

Métodos:
- compareTo(Autor otro) - ordenar por apellido, luego nombre
- getNombreCompleto()
- equals y hashCode - basados en id
```

Clase: Lector implements Comparable Lector

```java
Atributos:
- numeroLector (int) - único
- nombre (String)
- apellido (String)
- email (String) - único
- fechaRegistro (LocalDate)
- preferenciaGeneros (Set<Genero>) - géneros favoritos
- historialLecturas (List<HistorialLectura>) - libros leídos con fecha
- librosPrestados (Map<String, Prestamo>) - ISBN -> Préstamo activo
- calificacionesDadas (List<Calificacion>)
- listaDeseos (Set<String>) - ISBNs

Métodos:
- compareTo(Lector otro) - por número de lector
- cantidadLibrosLeidos()
- calcularGeneroFavorito()
- equals y hashCode - basados en numeroLector
```

Clase: Prestamo

```java
Atributos:
- numeroPrestamo (int)
- isbn (String)
- numeroLector (int)
- fechaPrestamo (LocalDate)
- fechaDevolucionEsperada (LocalDate)
- fechaDevolucionReal (LocalDate)
- estado (EstadoPrestamo) - ACTIVO, DEVUELTO, VENCIDO
- multaPendiente (double)

Métodos:
- calcularDiasRetraso()
- calcularMulta()
- estaVencido()
```

Clase: Calificacion

```java
Atributos:
- isbn (String)
- numeroLector (int)
- puntuacion (int) - 1 a 5 estrellas
- comentario (String)
- fecha (LocalDate)
- votosUtiles (int) - otros lectores que marcaron útil

Métodos:
- esPositiva() - >= 4
```

Clase: HistorialLectura

```java
Atributos:
- isbn (String)
- fechaInicio (LocalDate)
- fechaFin (LocalDate)
- calificacion (Calificacion)

Métodos:
- calcularDiasLectura()
```

Enum: Genero

- FICCION, FANTASIA, CIENCIA_FICCION, ROMANCE, THRILLER, TERROR, HISTORIA,
- BIOGRAFIA, AUTOAYUDA, NEGOCIOS, TECNOLOGIA, ARTE, POESIA, INFANTIL

Enum: EstadoPrestamo

- ACTIVO, DEVUELTO, VENCIDO, RENOVADO

### Colecciones Principales del Sistema

#### Almacenamiento Principal

Map<String, Libro> catalogoLibros

- Clave: ISBN
- Valor: Libro
- Tipo: HashMap (búsqueda O(1) por ISBN)

Map<Integer, Autor> catalogoAutores

- Clave: ID del autor
- Valor: Autor
- Tipo: HashMap

Map<Integer, Lector> catalogoLectores

- Clave: Número de lector
- Valor: Lector
- Tipo: HashMap

List Prestamo prestamosActivos

- Lista de préstamos no devueltos
- Tipo: ArrayList (acceso por índice)

List Prestamo historialPrestamos

- Todos los préstamos (históricos)
- Tipo: ArrayList

#### Índices Secundarios (para búsquedas eficientes)

Map<Genero, Set String librosPorGenero

- Clave: Género
- Valor: Set de ISBNs (HashSet)
- Permite obtener libros de un género rápidamente

Map<Integer, Set String librosPorAutor

- Clave: ID del autor
- Valor: Set de ISBNs (HashSet)
- Relaciona autores con sus libros

Map<Integer, List Prestamo prestamosPorLector

- Clave: Número de lector
- Valor: Lista de préstamos
- Historial completo por lector

TreeSet Libro rankingMejoresLibros

- Libros ordenados por calificación (Comparator personalizado)
- Tipo: TreeSet con Comparator

Set String emailsRegistrados

- Control de unicidad de emails
- Tipo: HashSet

### Requerimientos Funcionales

#### 1. Gestión de Libros

**Agregar Libro:**

- Validar ISBN único
- Asignar autores (seleccionar de catálogo o crear nuevos)
- Asignar géneros (múltiples)
- Agregar tags
- Actualizar índices (librosPorGenero, librosPorAutor)

**Buscar Libros:**

- Por ISBN (exacto) - usar HashMap O(1)
- Por título (parcial, case-insensitive) - iterar catálogo
- Por autor - usar índice librosPorAutor
- Por género - usar índice librosPorGenero
- Por rango de años - filtrar catálogo
- Por tags - intersección de sets
- Por disponibilidad (copias > 0)

**Listar Libros:**

- Todos los libros ordenados alfabéticamente (TreeSet temporal o sort)
- Por fecha de publicación (Comparator)
- Por calificación (usar rankingMejoresLibros)
- Por popularidad (número de préstamos)

**Actualizar Libro:**

- Modificar información
- Agregar/eliminar autores
- Agregar/eliminar géneros
- Actualizar número de copias

**Eliminar Libro:**

- Validar que no tenga préstamos activos
- Eliminar de índices
- Eliminar del catálogo principal

#### 2. Gestión de Autores

**Registrar Autor:**

- Generar ID único
- Validar datos
- Agregar a catálogo

**Buscar Autor:**

- Por ID
- Por nombre (parcial)
- Por nacionalidad

**Listar Autores:**

- Ordenados alfabéticamente (orden natural con Comparable)
- Por nacionalidad
- Por cantidad de libros escritos (Comparator)
- Autores con premios

**Asociar Libro a Autor:**

- Agregar ISBN a librosEscritos del autor
- Actualizar índice librosPorAutor

#### 3. Gestión de Lectores

**Registrar Lector:**

- Generar número de lector único
- Validar email único (usar Set emailsRegistrados)
- Registrar preferencias de género
- Agregar a catálogo

**Buscar Lector:**

- Por número de lector
- Por email
- Por nombre (parcial)

**Perfil del Lector:**

- Mostrar información personal
- Libros prestados actualmente (usar Map librosPrestados)
- Historial de lecturas (ordenado por fecha)
- Género favorito (calcular de historial)
- Estadísticas (total leído, promedio calificaciones, etc.)

**Listar Lectores:**

- Todos ordenados por número
- Lectores más activos (más libros leídos)
- Lectores con préstamos vencidos
- Por género de preferencia

#### 4. Gestión de Préstamos

**Realizar Préstamo:**

- Validar disponibilidad del libro (copias > 0)
- Validar que lector no tenga préstamos vencidos
- Crear objeto Prestamo
- Reducir copias disponibles
- Agregar a prestamosActivos
- Agregar a historialPrestamos
- Actualizar Map librosPrestados del lector
- Actualizar índice prestamosPorLector

**Devolver Libro:**

- Buscar préstamo activo
- Registrar fecha de devolución
- Calcular multa si hay retraso
- Incrementar copias disponibles
- Eliminar de prestamosActivos
- Cambiar estado a DEVUELTO
- Permitir calificar el libro

**Renovar Préstamo:**

- Validar que no esté vencido
- Extender fecha de devolución (15 días más)
- Cambiar estado a RENOVADO
- Máximo 2 renovaciones

**Listar Préstamos:**

- Préstamos activos
- Préstamos vencidos (con multas)
- Historial de préstamos de un lector
- Préstamos de un libro específico
- Préstamos por rango de fechas

#### 5. Sistema de Calificaciones y Reseñas

**Calificar Libro:**

- Validar que el lector haya leído el libro
- Crear objeto Calificacion
- Agregar a lista de calificaciones del libro
- Agregar a calificacionesDadas del lector
- Actualizar calificación promedio del libro
- Actualizar rankingMejoresLibros (TreeSet se auto-ordena)

**Ver Calificaciones:**

- Todas las calificaciones de un libro (ordenadas por fecha)
- Calificaciones de un lector
- Promedio de calificaciones por género
- Distribución de calificaciones (1 estrella: X, 2 estrellas: Y, etc.)

**Marcar Calificación como Útil:**

- Incrementar votosUtiles
- Reordenar al mostrar (más útiles primero)

#### 6. Sistema de Recomendaciones

**Recomendar Libros a un Lector:**
Algoritmo basado en:

1. Género favorito del lector
2. Libros mejor calificados de ese género
3. Libros que no ha leído
4. Excluir libros en lista de deseos (ya los conoce)

Retornar: List Libro ordenada por relevancia

**Recomendar por Similitud:**

- Lectores que leyeron un libro también leyeron...
- Encontrar lectores que leyeron el mismo libro
- Ver qué otros libros leyeron
- Contar frecuencias (Map<String, Integer>)
- Ordenar por frecuencia
- Retornar top 10

**Lectores Similares:**

- Comparar preferencias de género (intersección de Sets)
- Comparar historial de lecturas (intersección)
- Calcular porcentaje de similitud
- Usar para recomendar lo que leen lectores similares

#### 7. Reportes y Estadísticas

**Top 10:**

- Libros más prestados (usar Map<String, Integer> para contar)
- Libros mejor calificados (usar TreeSet rankingMejoresLibros)
- Autores más leídos
- Lectores más activos
- Géneros más populares

**Estadísticas Generales:**

- Total de libros en catálogo
- Total de copias disponibles vs prestadas
- Promedio de calificaciones global
- Porcentaje de préstamos a tiempo vs vencidos
- Libros nunca prestados (diferencia de Sets)

**Estadísticas por Género:**

- Para cada género:
  - Total de libros
  - Promedio de calificaciones
  - Total de préstamos
  - Lectores interesados (cuenta preferenciaGeneros)

**Estadísticas por Autor:**

- Total de libros
- Total de préstamos de sus libros
- Calificación promedio de sus obras
- Género más frecuente en sus libros

**Tendencias:**

- Libros más prestados del mes
- Nuevos registros de lectores por período
- Géneros en tendencia (más préstamos recientes)

#### 8. Búsquedas Avanzadas

**Búsqueda Combinada:**
Permitir filtrar por múltiples criterios simultáneos:

- Género AND autor AND rango de años
- Usar intersección de Sets para eficiencia

Ejemplo:

```java
Set<String> librosFiccion = librosPorGenero.get(FICCION);
Set<String> librosAutor = librosPorAutor.get(autorId);
Set<String> resultado = new HashSet<>(librosFiccion);
resultado.retainAll(librosAutor); // Intersección
// Filtrar por año...
```

**Búsqueda por Tags:**

- Ingresar múltiples tags
- Encontrar libros que tengan TODOS los tags (AND)
- O que tengan AL MENOS UNO (OR)
- Usar operaciones de Set (retainAll, addAll)

**Búsqueda Textual:**

- Buscar en título, descripción, tags
- Case-insensitive
- Retornar resultados ordenados por relevancia

### Comparadores Requeridos

**Libro:**

1. Natural (Comparable): por título
2. Comparator por año de publicación
3. Comparator por calificación (descendente)
4. Comparator por número de páginas
5. Comparator por popularidad (préstamos)
6. Comparator multinivel: género -> calificación -> título

**Autor:**

1. Natural (Comparable): por apellido, luego nombre
2. Comparator por cantidad de libros
3. Comparator por nacionalidad

**Lector:**

1. Natural (Comparable): por número de lector
2. Comparator por nombre completo
3. Comparator por fecha de registro
4. Comparator por cantidad de libros leídos
5. Comparator por multas acumuladas

**Prestamo:**

1. Comparator por fecha de préstamo
2. Comparator por días de retraso

**Calificacion:**

1. Comparator por puntuación
2. Comparator por fecha
3. Comparator por votos útiles

### Operaciones Específicas con Colecciones

#### ArrayList vs LinkedList

**Usar ArrayList para:**

- historialPrestamos (acceso aleatorio)
- calificaciones del libro (acceso por índice)
- resultados de búsqueda (iterar frecuentemente)

**Usar LinkedList para:**

- Cola de espera para libros agotados (FIFO)
- Historial de modificaciones (inserción frecuente al inicio)

#### HashSet vs TreeSet

**Usar HashSet para:**

- emails registrados (solo unicidad, no orden)
- tags de libros (búsqueda rápida, no orden)
- ISBNs en librosPorGenero (orden no importa)

**Usar TreeSet para:**

- rankingMejoresLibros (mantener orden automático)
- Mostrar autores alfabéticamente
- Libros ordenados por cualquier criterio con Comparator

#### HashMap vs TreeMap

**Usar HashMap para:**

- catalogoLibros (búsqueda rápida por ISBN)
- catalogoAutores (búsqueda rápida por ID)
- catalogoLectores (búsqueda rápida por número)

**Usar TreeMap para:**

- Mostrar libros en orden de ISBN
- Estadísticas por año (TreeMap<Integer, List Libro)
- Ranking ordenado (clave: puntuación, valor: libros)

### Estructura de Código Sugerida

```java
public class SistemaBiblioteca {
    // Almacenamiento principal
    private Map<String, Libro> catalogoLibros;
    private Map<Integer, Autor> catalogoAutores;
    private Map<Integer, Lector> catalogoLectores;

    // Índices
    private Map<Genero, Set<String>> librosPorGenero;
    private Map<Integer, Set<String>> librosPorAutor;
    private TreeSet<Libro> rankingLibros;

    // Préstamos
    private List<Prestamo> prestamosActivos;
    private List<Prestamo> historialPrestamos;
    private Map<Integer, List<Prestamo>> prestamosPorLector;

    // Control de unicidad
    private Set<String> emailsRegistrados;

    // Comparadores
    private static final Comparator<Libro> POR_CALIFICACION =
        (l1, l2) -> Double.compare(l2.getCalificacion(), l1.getCalificacion());

    private static final Comparator<Lector> POR_ACTIVIDAD =
        (r1, r2) -> Integer.compare(r2.cantidadLibrosLeidos(), r1.cantidadLibrosLeidos());

    // ... más comparadores

    public SistemaBiblioteca() {
        catalogoLibros = new HashMap<>();
        catalogoAutores = new HashMap<>();
        catalogoLectores = new HashMap<>();
        librosPorGenero = new HashMap<>();
        librosPorAutor = new HashMap<>();
        rankingLibros = new TreeSet<>(POR_CALIFICACION);
        prestamosActivos = new ArrayList<>();
        historialPrestamos = new ArrayList<>();
        prestamosPorLector = new HashMap<>();
        emailsRegistrados = new HashSet<>();

        inicializarGeneros();
    }

    private void inicializarGeneros() {
        for (Genero g : Genero.values()) {
            librosPorGenero.put(g, new HashSet<>());
        }
    }

    // Métodos CRUD para Libros
    public void agregarLibro(Libro libro) { }
    public Libro buscarLibroPorISBN(String isbn) { }
    public List<Libro> buscarLibrosPorTitulo(String titulo) { }
    public List<Libro> buscarLibrosPorAutor(int autorId) { }
    public Set<Libro> buscarLibrosPorGenero(Genero genero) { }

    // Métodos para Autores
    public void agregarAutor(Autor autor) { }
    public Autor buscarAutor(int id) { }
    public List<Autor> listarAutoresOrdenados() { }

    // Métodos para Lectores
    public void registrarLector(Lector lector) { }
    public Lector buscarLector(int numero) { }
    public List<Lector> listarLectoresMasActivos(int top) { }

    // Métodos para Préstamos
    public Prestamo realizarPrestamo(int numeroLector, String isbn) { }
    public void devolverLibro(int numeroPrestamo, Calificacion calif) { }
    public List<Prestamo> listarPrestamosVencidos() { }

    // Métodos de Recomendación
    public List<Libro> recomendarLibros(int numeroLector, int cantidad) { }
    public List<Libro> librosRelacionados(String isbn, int cantidad) { }

    // Métodos de Reportes
    public Map<String, Integer> top10LibrosMasPrestados() { }
    public List<Libro> top10MejorCalificados() { }
    public Map<Genero, Double> promedioCalificacionesPorGenero() { }

    // Métodos auxiliares
    private void actualizarIndices(Libro libro) { }
    private void actualizarRanking(Libro libro) { }
}
```

### Casos de Uso y Pruebas

#### Caso 1: Registro y Préstamo

1. Registrar 3 autores
2. Registrar 10 libros de diferentes géneros
3. Registrar 5 lectores
4. Realizar 8 préstamos
5. Verificar que copias disponibles se reduzcan
6. Listar préstamos activos

#### Caso 2: Devolución y Calificación

1. Devolver 3 libros a tiempo
2. Devolver 2 libros con retraso (calcular multas)
3. Calificar los libros devueltos
4. Verificar actualización de calificación promedio
5. Verificar que aparezcan en ranking

#### Caso 3: Búsquedas

1. Buscar libros de género FICCION
2. Buscar libros de un autor específico
3. Buscar libros con tags "aventura" AND "magia"
4. Buscar libros publicados entre 2000-2020
5. Verificar que usen los índices correctos

#### Caso 4: Recomendaciones

1. Registrar historial de lectura para 3 lectores
2. Solicitar recomendaciones para lector 1
3. Verificar que sean del género favorito
4. Verificar que no incluya libros ya leídos

#### Caso 5: Estadísticas

1. Generar top 10 libros más prestados
2. Calcular promedio de calificaciones por género
3. Listar autores por cantidad de libros
4. Mostrar tendencias del mes

### Criterios de Evaluación

#### Uso de List (20%)

- ArrayList para almacenamiento general
- LinkedList donde sea apropiado
- Operaciones: add, remove, get, indexOf, sort
- Iteración con for-each y Iterator

#### Uso de Set (20%)

- HashSet para unicidad sin orden
- TreeSet para unicidad con orden
- Operaciones: add, contains, remove, retainAll, addAll
- Detección y eliminación de duplicados

#### Uso de Map (25%)

- HashMap para búsqueda rápida
- TreeMap para orden
- Operaciones: put, get, containsKey, keySet, values, entrySet
- Uso de getOrDefault y computeIfAbsent

#### Comparable y Comparator (20%)

- Implementación de Comparable en al menos 3 clases
- Definición de mínimo 5 Comparator diferentes
- Uso de Comparator.comparing()
- Comparadores multinivel

#### Integración y Funcionalidad (15%)

- Todas las funcionalidades implementadas
- Uso apropiado de cada tipo de colección
- Eficiencia en búsquedas (O(1) vs O(n))
- Código limpio y organizado

### Entregables

1. Código fuente completo
2. Diagrama de clases
3. Documento explicando elección de colecciones
4. Casos de prueba con resultados
5. Análisis de complejidad temporal

### Tiempo Estimado

20-28 horas de desarrollo

### Extensiones Opcionales

1. Implementar sistema de reservaciones (Queue)
2. Agregar caché de búsquedas recientes (LinkedHashMap con LRU)
3. Implementar sistema de notificaciones (Observer pattern)
4. Agregar persistencia con serialización
5. Crear sistema de multas y pagos
6. Implementar búsqueda con algoritmos de similitud (Levenshtein)
7. Agregar sistema de eventos y clubs de lectura
8. Crear dashboard con estadísticas en tiempo real
