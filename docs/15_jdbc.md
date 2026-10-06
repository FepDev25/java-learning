# P15 - JDBC y Bases de Datos

## Descripción General

Acceso a bases de datos con **JDBC** y PostgreSQL: conexión directa con `DriverManager`,
pool de conexiones con **HikariCP**, CRUD, `PreparedStatement` (prevención de SQL Injection),
operaciones por lotes (batch), transacciones y el patrón **DAO**.

## Información del Proyecto

- **Artifact ID:** p15_jdbc
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven
- **Dependencias:** `org.postgresql:postgresql:42.7.3`, `com.zaxxer:HikariCP:5.1.0`

## Requisitos previos

El módulo necesita una base de datos PostgreSQL. Se incluye un `docker-compose.yml`
que levanta PostgreSQL 16 e inicializa el esquema (`src/main/resources/schema.sql`):

```bash
cd p15_jdbc
docker compose up -d
docker compose ps        # esperar a que el healthcheck pase
```

| Parámetro | Valor |
|-----------|-------|
| Host/puerto | `localhost:5435` (mapeado al 5432 del contenedor) |
| Base de datos | `notas_db` |
| Usuario | `felipe` |
| Contraseña | `ecuador2024` |

## Contenido del Módulo

### 1. `config` — Conexión

- `Conexion.java` - Conexión con `DriverManager` (una conexión nueva por llamada).
- `ConexionPool.java` - Pool con HikariCP (reutiliza conexiones; uso como singleton).

### 2. `jdbc` — Ejemplos de JDBC

- `EjemploConexion.java` - Verifica la conexión con `DriverManager` y con HikariCP.
- `EjemploCRUD.java` - CRUD básico con `Statement` y `ResultSet`.
- `EjemploPreparedStatement.java` - La forma correcta: parámetros con `?`, previene SQL Injection.
- `EjemploBatch.java` - Inserción masiva con `addBatch()` / `executeBatch()`.
- `EjemploTransacciones.java` - `setAutoCommit(false)`, `commit()` y `rollback()` (ACID).

### 3. `dao` — Patrón DAO

- `EstudianteDao.java` - Interfaz de acceso a datos (aplica DIP de SOLID).
- `EstudianteDaoJdbc.java` - Implementación JDBC: `PreparedStatement`, pool y
  mapeo manual `ResultSet → Estudiante`.
- `EjemploDAO.java` - El cliente depende de la interfaz, no de la implementación.

### 4. `modelo`

- `Estudiante.java` - POJO que mapea la tabla `estudiantes`.

## Conceptos Clave Aprendidos

- **JDBC:** `Connection` → `Statement`/`PreparedStatement` → `ResultSet`.
- **PreparedStatement:** seguridad (SQL Injection), rendimiento y legibilidad.
  **Nunca** concatenar entrada de usuario en un `Statement`.
- **Batch:** múltiples sentencias en un solo viaje de red (N operaciones → 1 round-trip).
- **Transacciones (ACID):** atomicidad, consistencia, aislamiento y durabilidad;
  auto-commit desactivado para control manual.
- **Pool de conexiones:** abrir/cerrar conexiones es costoso; HikariCP las reutiliza.
- **Patrón DAO:** aísla la lógica de acceso a datos del resto de la aplicación.
- **try-with-resources:** cierra conexión, statement y resultset automáticamente.

## Ejecución de Ejemplos

```bash
cd p15_jdbc
docker compose up -d      # levantar PostgreSQL
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.jdbc.EjemploConexion"
```

Otros ejemplos: `com.cultodeportivo.jdbc.EjemploCRUD`,
`com.cultodeportivo.jdbc.EjemploPreparedStatement`,
`com.cultodeportivo.jdbc.EjemploBatch`,
`com.cultodeportivo.jdbc.EjemploTransacciones`,
`com.cultodeportivo.dao.EjemploDAO`.

## Estructura de Paquetes

```text
p15_jdbc/
├── docker-compose.yml
├── pom.xml
└── src/main/
    ├── java/com/cultodeportivo/
    │   ├── config/    (Conexion, ConexionPool)
    │   ├── dao/       (EstudianteDao, EstudianteDaoJdbc, EjemploDAO)
    │   ├── jdbc/      (EjemploConexion, EjemploCRUD, EjemploPreparedStatement, EjemploBatch, EjemploTransacciones)
    │   └── modelo/    (Estudiante)
    └── resources/
        └── schema.sql
```

## Notas Técnicas

- El esquema `schema.sql` define las tablas `estudiantes` y `materias`.
- Para producción, preferir un pool (HikariCP) sobre `DriverManager` directo.
- El puerto 5435 se usa para evitar conflictos con instancias locales en 5432.

## Referencias

- [JDBC - Oracle](https://docs.oracle.com/javase/tutorial/jdbc/)
- [HikariCP](https://github.com/brettwooldridge/HikariCP)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)
