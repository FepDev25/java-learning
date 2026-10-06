# P10 - Java 8: Fecha y Hora

## Descripción General

Estudio de la API de fecha y hora de Java 8 (`java.time`), inmutable y thread-safe:
`LocalDate`, `LocalTime`, `LocalDateTime`, `ZonedDateTime`, `Duration` y `Period`.

## Información del Proyecto

- **Artifact ID:** p10_java8_date_time
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

Todas las clases viven en el paquete `com.cultodeportivo.datetime`.

- `EjemploLocalDate.java` - Solo fecha (año-mes-día), sin hora ni zona. Inmutable:
  `plus`/`minus`/`with` devuelven un nuevo objeto.
- `EjemploLocalTime.java` - Solo hora (hora:minuto:segundo:nanosegundo).
- `EjemploLocalDateTime.java` - Fecha + hora sin zona; parseo desde ISO-8601.
- `EjemploZonedDateTime.java` - Fecha/hora con `ZoneId` y reglas de horario (DST);
  diferencia entre `ZoneOffset` (fijo) y `ZoneId` (con reglas).
- `EjemploDuration.java` - Diferencia entre instantes en segundos/nanosegundos
  (para `LocalDateTime`/`LocalTime`).
- `EjemploPeriod.java` - Diferencia entre fechas en años/meses/días (para `LocalDate`).

## Conceptos Clave Aprendidos

| Clase | Representa | Uso |
|-------|------------|-----|
| `LocalDate` | Fecha | Cumpleaños, vencimientos |
| `LocalTime` | Hora | Horarios de clase |
| `LocalDateTime` | Fecha + hora | Eventos locales |
| `ZonedDateTime` | Fecha + hora + zona | Distintas zonas horarias |
| `Duration` | Tiempo (segundos) | Diferencias entre instantes |
| `Period` | Fecha (años/meses/días) | Edades, duraciones de semestre |

- La API `java.time` es **inmutable** y **thread-safe**.
- Reemplaza a `java.util.Date` y `java.util.Calendar` (obsoletos).
- `Period.between(inicio, fin)` en orden cronológico da un resultado positivo.
- `ZoneId` respeta el horario de verano; `ZoneOffset` es un offset fijo.

## Ejecución de Ejemplos

```bash
cd p10_java8_date_time
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.datetime.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.datetime.EjemploLocalDate"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.datetime.EjemploZonedDateTime"
```

## Estructura de Paquetes

```text
com.cultodeportivo
└── datetime/
```

## Notas Técnicas

- Usar `Period` con `LocalDate` y `Duration` con `LocalDateTime`/`Instant`.
- El separador `T` (por ejemplo `2025-06-30T23:59`) es el estándar ISO-8601.
- Ecuador (`America/Guayaquil`) usa UTC-5 sin horario de verano.

## Referencias

- [Date-Time API - Oracle](https://docs.oracle.com/javase/tutorial/datetime/)
- [java.time](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/package-summary.html)
