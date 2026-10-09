# 15 · JDBC: conectar objetos Java con una base de datos

## Contenido

- [1. Qué hace JDBC y qué hace PostgreSQL](#1-qué-hace-jdbc-y-qué-hace-postgresql)
- [2. Infraestructura y esquema: las reglas están también en SQL](#2-infraestructura-y-esquema-las-reglas-están-también-en-sql)
- [3. Abrir y cerrar una conexión directa](#3-abrir-y-cerrar-una-conexión-directa)
- [4. Sentencias y cursor de resultados](#4-sentencias-y-cursor-de-resultados)
- [5. PreparedStatement: separar SQL de valores](#5-preparedstatement-separar-sql-de-valores)
- [6. Operaciones por lotes: acumular operaciones y decidir la unidad de confirmación](#6-operaciones-por-lotes-acumular-operaciones-y-decidir-la-unidad-de-confirmación)
- [7. Confirmación y reversión de cambios: todo o nada dentro de una conexión](#7-confirmación-y-reversión-de-cambios-todo-o-nada-dentro-de-una-conexión)
- [8. Prestar conexiones reutilizables](#8-prestar-conexiones-reutilizables)
- [9. `dao` y `modelo`: persistencia detrás de un contrato](#9-dao-y-modelo-persistencia-detrás-de-un-contrato)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Qué hace JDBC y qué hace PostgreSQL

JDBC es la API de Java para trabajar con bases de datos mediante drivers. El driver de PostgreSQL convierte las operaciones de JDBC en comunicación con ese servidor. Java administra conexiones, parámetros y resultados; PostgreSQL ejecuta SQL, comprueba restricciones y persiste los datos.

El recorrido habitual es:

```text
Programa Java → Connection → Statement / PreparedStatement
            → PostgreSQL → ResultSet → objetos Java
```

No hay JPA ni un ORM que haga el mapeo en este módulo. `Estudiante` es un objeto Java y el DAO lo llena leyendo columnas una por una. Los `pom.xml` anteriores no requieren bibliotecas externas para estas APIs del JDK; el de JDBC añade driver PostgreSQL `42.7.3` y HikariCP `5.1.0`, versiones fijadas por el repositorio, sin implicar que sean las últimas disponibles.

### Ciclo mínimo de una consulta

```java
// Reducción del ciclo de recursos usado en las consultas JDBC.
try (Connection conn = Conexion.obtener();
     Statement sentencia = conn.createStatement();
     ResultSet filas = sentencia.executeQuery("SELECT id, nombre FROM estudiantes")) {
    while (filas.next()) {
        int id = filas.getInt("id");
        String nombre = filas.getString("nombre");
        System.out.println(id + " : " + nombre);
    }
}
```

Cada recurso depende del anterior: la conexión crea una sentencia y esta genera un resultado. `next` establece la fila actual antes de leerla. El bloque cierra primero resultado, después sentencia y finalmente conexión. Una excepción puede cortar el ciclo, pero no omite el cierre normal de los recursos declarados.

## 2. Infraestructura y esquema: las reglas están también en SQL

docker-compose.yml define PostgreSQL 16 Alpine, puerto 5435 del host hacia 5432 del contenedor y volumen `jdbc_data`. Las configuraciones Java apuntan a `jdbc:postgresql://localhost:5435/notas_db`, con usuario y contraseña de la práctica definidos en el compose y en `config`.

schema.sql crea tres tablas:

| Tabla | Responsabilidad | Restricciones relevantes |
| --- | --- | --- |
| `estudiantes` | Persona, carrera, promedio, estado y fecha de alta | Nombre/carrera obligatorios; edad entre 15 y 99; promedio entre 0 y 10 |
| `materias` | Catálogo de asignaturas y créditos | Nombre único y obligatorio |
| `inscripciones` | Relación estudiante–materia por semestre | Claves foráneas y pareja por semestre única |

`SERIAL PRIMARY KEY` usa una secuencia para generar IDs. `NOT NULL` impide ausencia; `DEFAULT` aporta valores cuando no se suministran; `CHECK` exige una condición; `UNIQUE` evita duplicados según sus columnas. Una restricción `CHECK` de promedio no impide nulo sin `NOT NULL`; en este esquema promedio y nota pueden faltar.

La clave foránea de estudiante en inscripciones tiene `ON DELETE CASCADE`: eliminar al estudiante elimina sus inscripciones. Esa acción la realiza el servidor, no un bucle oculto del DAO. La relación con materias no declara esa cascada.

El esquema incluye cinco estudiantes, cinco materias y seis inscripciones iniciales. Su script de inicialización se aplica al preparar un directorio de datos vacío; volver a levantar el mismo volumen no reinicia los datos. Además, los `INSERT` de semillas no son idempotentes y las inscripciones presuponen IDs iniciales; ejecutar el script repetidamente sobre una base existente no equivale a una instalación limpia.

### Restricciones junto a las columnas

```sql
-- Extracto del esquema de estudiantes.
CREATE TABLE IF NOT EXISTS estudiantes (
    id        SERIAL PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    edad      INTEGER NOT NULL CHECK (edad BETWEEN 15 AND 99),
    pais      VARCHAR(100) NOT NULL DEFAULT 'Ecuador',
    carrera   VARCHAR(150) NOT NULL,
    promedio  NUMERIC(4,2) CHECK (promedio BETWEEN 0 AND 10),
    activo    BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT NOW()
);
```

Las reglas se aplican al ejecutar SQL independientemente de cómo se haya construido el objeto Java. `NUMERIC(4,2)` admite cuatro dígitos de precisión total y dos decimales. El `CHECK` limita el rango cuando hay valor, mientras que la ausencia sigue permitida para promedio porque no tiene `NOT NULL`.

```sql
-- Campos de relación y unicidad de la inscripción.
estudiante_id INTEGER NOT NULL REFERENCES estudiantes(id) ON DELETE CASCADE,
materia_id    INTEGER NOT NULL REFERENCES materias(id),
UNIQUE (estudiante_id, materia_id, semestre)
```

La clave foránea exige referencias existentes. La unicidad evalúa la combinación de los tres datos: una nueva inscripción del mismo estudiante en la misma materia puede existir en otro semestre, pero no repetir esa combinación exacta.

## 3. Abrir y cerrar una conexión directa

`Conexion` delega:

```java
public static Connection obtener() throws SQLException {
    return DriverManager.getConnection(URL, USUARIO, PASSWORD);
}
```

Cada llamada solicita una conexión nueva al driver. El llamador debe cerrarla, normalmente con `try-with-resources`. Obtenerla no selecciona automáticamente una tabla ni ejecuta las consultas del programa.

`verificar()` imprime metadatos del servidor y driver, URL y auto-commit. `SQLException` contiene mensaje y `SQLState`, que ayudan a distinguir conexión rechazada, autenticación u otros fallos. El método captura el error y escribe un diagnóstico; no relanza, así terminar el programa sin excepción no demuestra por sí solo que se conectó.

El ejemplo de verificación de conexiones verifica conexión directa, consulta `version()`, prueba el pool y cuenta las tres tablas. Sus conteos posteriores pueden variar según ejecuciones previas. Los nombres de tablas concatenados vienen de una lista fija; permitir que los suministre el usuario requeriría una política diferente.

## 4. Sentencias y cursor de resultados

CRUD significa crear, leer, actualizar y eliminar datos. Para `INSERT`, `UPDATE` y `DELETE`, `executeUpdate` devuelve el número de filas afectadas. Para una consulta que devuelve filas, `executeQuery` retorna `ResultSet`.

El cursor empieza **antes** de la primera fila. Primero se llama `rs.next()`: devuelve verdadero y posiciona si existe otra fila. Luego `getString`, `getInt`, `getDouble` y `getBoolean` extraen columnas. Los índices JDBC empiezan en 1, no en 0 como los arreglos.

El ejemplo de operaciones CRUD inserta Carlos y Laura, consulta estudiantes, muestra metadatos de columnas, actualiza Carlos y elimina ambos nombres. Usa `INSERT ... RETURNING id`, una forma de PostgreSQL de devolver un valor de la fila insertada. Las restricciones del servidor siguen aplicándose aunque el objeto Java haya permitido cualquier valor.

La función `selectTodos` convierte cada fila a `Estudiante` y retorna una lista. El ResultSet no es esa lista: permanece ligado a recursos y contexto de la consulta. Materializar objetos permite usarlos después de cerrar la conexión.

**Límites actuales:** algunos `conn.createStatement().executeUpdate(...)` del CRUD no cierran su statement explícitamente. Es preferible declarar cada recurso en `try-with-resources`, como hacen otras partes. La limpieza por nombre elimina todas las coincidencias, no exclusivamente el registro recién insertado; estas operaciones corresponden a la base de práctica.

### Transformar cada fila en un objeto

```java
static List<Estudiante> selectTodos(Connection conn) throws SQLException {
    List<Estudiante> lista = new ArrayList<>();
    try (Statement stmt = conn.createStatement();
         ResultSet rs   = stmt.executeQuery(
                 "SELECT * FROM estudiantes ORDER BY id")) {
        while (rs.next()) {
            Estudiante e = new Estudiante();
            e.setId      (rs.getInt      ("id"));
            e.setNombre  (rs.getString   ("nombre"));
            e.setEdad    (rs.getInt      ("edad"));
            e.setPais    (rs.getString   ("pais"));
            e.setCarrera (rs.getString   ("carrera"));
            e.setPromedio(rs.getDouble   ("promedio"));
            e.setActivo  (rs.getBoolean  ("activo"));
            lista.add(e);
        }
    }
    return lista;
}
```

El ciclo crea una instancia por fila; no reusa un solo objeto cambiándolo repetidamente. Cada setter copia una columna a un campo del modelo y la lista conserva esa referencia. El resultado materializado puede utilizarse después de cerrar el cursor y la conexión.

## 5. PreparedStatement: separar SQL de valores

El ejemplo de consultas parametrizadas define un SQL con marcadores:

```java
String sql = """
    INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio)
    VALUES (?, ?, ?, ?, ?)
    """;
```

Después asigna parámetros:

```java
ps.setString(1, (String) datos[0]);
ps.setInt   (2, (int)    datos[1]);
ps.setString(3, (String) datos[2]);
ps.setString(4, (String) datos[3]);
ps.setDouble(5, (double) datos[4]);
ps.executeUpdate();
```

El arreglo empieza en 0 y los parámetros en 1. El texto SQL conserva su estructura; el nombre enviado se interpreta como un dato, aunque contenga comillas. Esto evita que valores enlazados se conviertan en instrucciones SQL. No protege partes concatenadas de la consulta ni permite usar `?` como nombre de tabla, columna o dirección `ASC/DESC`.

Solicitar `Statement.RETURN_GENERATED_KEYS` permite consultar claves generadas y asignar ID al objeto. `getGeneratedKeys` también devuelve un cursor: requiere `next`. Los parámetros permanecen entre ejecuciones hasta que se cambian o limpian; en las transacciones, por ejemplo, la segunda inserción reutiliza país y carrera que no se reasignan.

El ejemplo reutiliza una consulta con rangos `BETWEEN ? AND ?`, inclusivos en ambos extremos. Así una nota exactamente 8 puede aparecer en los rangos 7–8 y 8–9. La preparación y caché efectiva del plan en el servidor dependen del driver y su configuración; no se debe asumir «compilado una sola vez» como garantía universal de toda llamada.

La eliminación con `nombre = ANY(?)` enlaza un array SQL creado por la conexión y lo libera con `free`; esa sintaxis es específica de PostgreSQL. Tampoco limita la eliminación a las filas recién creadas si ya existían personas con esos nombres.

### Reutilizar parámetros entre consultas

```java
double[][] rangos = {{7.0, 8.0}, {8.0, 9.0}, {9.0, 10.0}};
for (double[] rango : rangos) {
    ps.setDouble(1, rango[0]);
    ps.setDouble(2, rango[1]);
    try (ResultSet rs = ps.executeQuery()) {
        System.out.printf("  Rango [%.1f - %.1f]: ", rango[0], rango[1]);
        StringBuilder sb = new StringBuilder();
        while (rs.next())
            sb.append(rs.getString("nombre")).append("(").append(
                    String.format("%.1f", rs.getDouble("promedio"))).append(") ");
        System.out.println(sb.isEmpty() ? "ninguno" : sb.toString().trim());
    }
}
```

La sentencia tiene dos marcadores definidos previamente para los extremos de un `BETWEEN`. Cada vuelta sustituye sus valores, ejecuta y debe consumir el resultado antes de continuar. Los límites inclusivos explican que una nota exactamente 8.0 aparezca tanto en 7–8 como en 8–9.

## 6. Operaciones por lotes: acumular operaciones y decidir la unidad de confirmación

`addBatch()` agrega el conjunto actual de parámetros; `executeBatch()` ejecuta lo acumulado y devuelve resultados por operación. La implementación compara cien inserciones individuales, un batch de cien y batches de veinte con confirmación en cada lote.

Batch puede reducir intercambio y costo repetitivo, pero no garantiza exactamente un único viaje de red para cualquier driver, configuración o tamaño. Tampoco implica atomicidad por sí solo: la transacción decide qué queda confirmado.

Con `autoCommit=false`, un batch completo puede confirmarse como una unidad. Con commit cada veinte, los primeros lotes ya quedan persistidos si falla uno posterior; el rollback posterior no deshace commits anteriores. Elegir tamaño de lote es también decidir alcance de recuperación, no solo velocidad.

Los resultados pueden ser conteos no negativos o constantes como `SUCCESS_NO_INFO` y `EXECUTE_FAILED`; sumar todo el arreglo sin distinguirlas no garantiza un conteo válido de filas. `resultados.length` cuenta respuestas, no necesariamente filas insertadas. Estos códigos están definidos por [Statement de Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.sql/java/sql/Statement.html).

`BatchUpdateException.getUpdateCounts()` permite investigar ejecución parcial, pero su longitud no indica universalmente el único índice fallido: un driver puede continuar otras operaciones o detenerse. El ejemplo hace rollback en esa captura. Su tercera sección no tiene un `catch` propio que revierta y explique un fallo, y su relación de tiempos es una medición ilustrativa de variantes distintas, no un benchmark comparable garantizado.

### Añadir parámetros y ejecutar un lote

```java
// Versión reducida del batch completo, con país y carrera constantes.
conn.setAutoCommit(false);
try (PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) VALUES (?,?,?,?,?)")) {
    for (int i = 1; i <= 100; i++) {
        ps.setString(1, "Batch_" + i);
        ps.setInt(2, 18 + (i % 10));
        ps.setString(3, "Ecuador");
        ps.setString(4, "CS");
        ps.setDouble(5, 7.0 + (i % 30) * 0.1);
        ps.addBatch();
    }
    int[] resultados = ps.executeBatch();
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
    throw e;
}
```

`addBatch` conserva los valores del conjunto actual antes de que la siguiente vuelta los cambie. La ejecución ocurre una vez reunidos los cien conjuntos. El commit confirma esta unidad completa; la captura reducida muestra una política explícita de rollback frente a fallo. El arreglo de resultados necesita interpretarse según los códigos de JDBC, no sumarse sin distinguir sus constantes especiales.

## 7. Confirmación y reversión de cambios: todo o nada dentro de una conexión

Por defecto el auto-commit confirma cada operación al completarse. Para agrupar cambios, se desactiva y se decide explícitamente:

```java
conn.setAutoCommit(false);
```

La implementación agrega Transact_A y Transact_B, después llama `commit()`. En el caso fallido, inserta Rollback_A y luego intenta edad 200 para Rollback_B. El `CHECK` rechaza 200; `rollback()` deshace también la primera inserción porque pertenece a la misma transacción aún no confirmada.

Las propiedades ACID describen garantías del sistema transaccional: atomicidad agrupa cambios; consistencia mantiene restricciones y necesita reglas de dominio correctas; aislamiento controla qué observan transacciones concurrentes según el nivel configurado; durabilidad conserva lo confirmado conforme a las garantías de persistencia del servidor. «Aislamiento» no significa que nunca exista interferencia observable bajo cualquier nivel.

### Transacción con más de una escritura

```java
// Esquema reducido de una transacción sobre una conexión ya obtenida.
conn.setAutoCommit(false);
try (Statement sentencia = conn.createStatement()) {
    sentencia.executeUpdate(
            "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) "
            + "VALUES ('Rollback_A', 22, 'Ecuador', 'CS', 8.0)");
    sentencia.executeUpdate(
            "INSERT INTO estudiantes (nombre, edad, pais, carrera, promedio) "
            + "VALUES ('Rollback_B', 200, 'Ecuador', 'CS', 8.0)");
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
}
```

La segunda escritura viola la restricción de edad antes de alcanzar el commit. El rollback retira también la primera porque ambas estaban pendientes en la misma conexión. Si la primera ya hubiera sido confirmada, el rollback posterior no podría deshacer ese commit.

### Savepoint no es un commit parcial

El ejemplo inserta Save_A, crea `sp_after_A`, inserta Save_B y hace `rollback(sp1)`. Save_B se deshace; Save_A sigue pendiente y solo persiste al `commit()` final. El comentario «commit parcial» anterior al savepoint es incorrecto: el savepoint marca un punto de regreso dentro de la misma transacción.

Después de rollback no se recuperan necesariamente IDs consumidos por secuencias; pueden quedar huecos. Eso no contradice la atomicidad de las filas. La limpieza por patrones `LIKE` actúa sobre coincidencias de nombres; `_` es un comodín SQL de un carácter, no un guion bajo literal a menos que se escape.

Para una transacción de varias operaciones todas deben utilizar la **misma conexión** y el mismo contexto. Invocar métodos que abren cada uno su conexión no crea automáticamente una transacción global.

### Un punto de regreso dentro de la transacción

```java
// Secuencia reducida después de ejecutar la primera inserción.
Savepoint punto = conn.setSavepoint("sp_after_A");
ps.setString(1, "Save_B");
ps.setInt(2, 21);
ps.setDouble(5, 7.0);
ps.executeUpdate();
conn.rollback(punto);
conn.commit();
```

País y carrera se mantienen de la asignación anterior a la misma sentencia. Volver al savepoint elimina Save_B, pero conserva dentro de la transacción la inserción previa de Save_A. El último commit, y no la creación del punto, es lo que confirma Save_A.

## 8. Prestar conexiones reutilizables

`ConexionPool` crea un `HikariDataSource` estático y configura máximo 10, mínimo inactivas 2, tiempo de espera de préstamo 30 s y límites de inactividad y vida. El máximo incluye conexiones en uso e inactivas, no solo «activas» como su comentario sugiere.

`obtener()` pide una conexión al pool. `close()` sobre la conexión prestada normalmente la devuelve al pool mediante su envoltorio; no equivale a cerrar el pool entero. Por eso también es necesario usar `try-with-resources` con conexiones del pool. `ConexionPool.cerrar()` sí termina el data source y debe acompañar el cierre de la aplicación.

Los datos de `estadisticas()` son una observación momentánea, no un registro fijo de actividad. El bloque estático inicializa al usar la clase; problemas de inicialización no siempre aparecen como una simple `SQLException` capturable en la llamada. El código no vuelve a abrir automáticamente el pool después de cerrarlo.

Compartir el pool es diferente de compartir una única conexión con estado transaccional entre todas las tareas. Cada unidad de trabajo debe obtener y devolver su recurso con claridad.

### Configurar capacidad y obtener un préstamo

```java
config.setMaximumPoolSize   (10);   // máximo de conexiones activas
config.setMinimumIdle       (2);    // conexiones en espera mínimas
config.setConnectionTimeout (30_000); // ms esperando una conexión libre
```

El máximo limita el total administrado por el pool y el mínimo inactivo expresa una reserva disponible. El tiempo de espera limita cuánto puede esperar una solicitud de préstamo cuando no consigue una conexión. No limita automáticamente cuánto puede durar cualquier consulta una vez obtenido el recurso.

```java
public static Connection obtener() throws SQLException {
    return dataSource.getConnection();
}
```

Este retorno presta una conexión del data source compartido. El cliente conserva la responsabilidad de cerrarla para devolverla al pool; cerrar el préstamo y cerrar todo el data source son operaciones distintas.

## 9. `dao` y `modelo`: persistencia detrás de un contrato

`EstudianteDao` define guardar, búsquedas, listas, actualizar, eliminar y contar activos. El cliente puede depender de esa interfaz mientras `EstudianteDaoJdbc` se ocupa del SQL.

`guardar` inserta cinco campos, recupera ID y lo asigna a la misma instancia. No recarga todos los valores de la base; por ejemplo `creadoEn` no queda completo hasta mapear una consulta. `buscarPorId` retorna `Optional.empty()` si no hay fila. Un fallo SQL se transforma en `RuntimeException` con causa, no en vacío: **ausencia y error de infraestructura son situaciones diferentes**.

`buscarPorNombre` compara exactamente con `LOWER(nombre)=LOWER(?)`; ignora mayúsculas, pero no busca un fragmento como el repositorio del capítulo 9. Nombre no es único en el esquema y esa consulta no tiene `ORDER BY`: si hay varios iguales, no existe una elección ordenada garantizada. El esquema inicial ya contiene Felipe, y el ejemplo de acceso mediante DAO inserta otro, de modo que ese caso de repetición puede ocurrir.

`mapearFila` recupera columnas y convierte `Timestamp` a `LocalDateTime`. `creado_en` es `TIMESTAMP` sin zona; el modelo no representa un instante global. `getDouble("promedio")` devuelve cero para SQL NULL si no se comprueba `wasNull`, perdiendo la distinción entre ausencia y promedio cero. El campo `double` del modelo tampoco puede representar nulo por sí mismo.

`actualizar` cambia **nombre, carrera y promedio**, no edad, país o activo. `eliminar` borra físicamente por ID; no es baja lógica. `contarActivos` filtra `activo=TRUE`, mientras que las listas generales no filtran activos. Cada método abre y cierra su propia conexión, por lo que el DAO actual no permite agrupar varias llamadas en una transacción compartida sin ampliar su diseño.

El ejemplo de acceso mediante DAO usa interfaz, inserta, consulta, cambia datos y elimina sus IDs; cierra el pool en `finally`. Si una operación intermedia falla, no hay un bloque de limpieza de filas que garantice revertir todos los cambios ya confirmados. Revisar mensajes es necesario para saber hasta dónde avanzó.

### Ausencia y error siguen caminos diferentes

```java
public Optional<Estudiante> buscarPorId(int id) {
    String sql = "SELECT * FROM estudiantes WHERE id = ?";
    try (Connection conn = ConexionPool.obtener();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? Optional.of(mapearFila(rs)) : Optional.empty();
        }

    } catch (SQLException ex) {
        throw new RuntimeException("Error al buscar por id: " + ex.getMessage(), ex);
    }
}
```

Si la consulta es correcta y no hay fila, `rs.next` da falso y se retorna vacío. Una excepción SQL sale por la captura como error con causa: no se convierte en ausencia. Esta separación permite al cliente decidir qué hacer ante un ID inexistente sin ocultar una conexión fallida.

### Copiar valores con su interpretación temporal

```java
Timestamp ts = rs.getTimestamp("creado_en");
if (ts != null) e.setCreadoEn(ts.toLocalDateTime());
return e;
```

La fecha almacenada puede ser nula y se comprueba antes de convertir. `toLocalDateTime` produce campos locales, de acuerdo con la columna sin zona. El retorno es el objeto ya construido; el helper no decide una política de horario global.

## Preguntas de repaso

La conexión requiere PostgreSQL disponible y las dependencias incluidas en el classpath.

Usar `java -cp target/classes` solo no incluye driver ni HikariCP; ejecuta con Maven o con el classpath completo configurado en el IDE. Después estudia, en orden, el ejemplo de operaciones CRUD, el ejemplo de consultas parametrizadas, el ejemplo de operaciones por lotes, el ejemplo de confirmación y reversión de cambios y `dao.EjemploDAO`.

Los ejemplos escriben y eliminan datos de la base de práctica. Reiniciarlos puede alterar conteos, IDs y tiempos, y las limpiezas por nombre pueden afectar coincidencias previas. `docker compose down` detiene contenedores; eliminar volúmenes sería otra operación con pérdida de datos y no hace falta para estudiar el flujo normal.

1. ¿Por qué `rs.next` va antes de getters? **Porque el cursor comienza antes de la primera fila.**
2. ¿Un marcador `?` representa una tabla? **No; representa un valor.**
3. ¿Batch implica todo o nada? **No; eso depende de la transacción y sus commits.**
4. ¿Savepoint hace persistente Save_A? **No; solo el commit confirma esa transacción.**
5. ¿Optional vacío significa consulta fallida? **No; el DAO lo usa para ausencia y propaga por otra vía los fallos SQL.**
