# 10 · Fecha y hora: representar información temporal

## Contenido

- [1. No todo dato temporal es un instante](#1-no-todo-dato-temporal-es-un-instante)
- [2. Fechas locales: crear, consultar y cambiar una fecha](#2-fechas-locales-crear-consultar-y-cambiar-una-fecha)
- [3. Horas locales: una hora sin día](#3-horas-locales-una-hora-sin-día)
- [4. Fecha y hora local: combinar sin inventar una zona](#4-fecha-y-hora-local-combinar-sin-inventar-una-zona)
- [5. Fechas con zona: cambiar representación manteniendo el instante](#5-fechas-con-zona-cambiar-representación-manteniendo-el-instante)
- [6. Duraciones: totales y componentes](#6-duraciones-totales-y-componentes)
- [7. Períodos de calendario: componentes de calendario](#7-períodos-de-calendario-componentes-de-calendario)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. No todo dato temporal es un instante

«25 de enero», «08:00» y «15 de junio a las 08:00 en Guayaquil» contienen información diferente. La API `java.time` ofrece tipos que expresan esas diferencias. Una fecha sin zona no es automáticamente un instante global; asignarle la zona de la máquina puede alterar su significado.

| Tipo del módulo | Información | Ejemplo de uso |
| --- | --- | --- |
| `LocalDate` | Año, mes y día | Nacimiento, vencimiento |
| `LocalTime` | Hora del día | Inicio de una clase |
| `LocalDateTime` | Fecha y hora, sin zona | Cita local cuyo contexto conoce la zona |
| `ZonedDateTime` | Fecha, hora y zona con reglas | Evento en distintas regiones |
| `Period` | Años, meses y días de calendario | Edad o intervalo entre fechas |
| `Duration` | Cantidad temporal en segundos y nanosegundos | Duración de una sesión |

Las clases de valor temporal usadas aquí son inmutables. Operaciones como `plusDays`, `minusHours` y `withMonth` producen otros valores. La variable puede reasignarse, pero el objeto anterior no se modifica.

## 2. Fechas locales: crear, consultar y cambiar una fecha

En el ejemplo de fechas locales:

```java
LocalDate nacimientoFelipe = LocalDate.of(2005, Month.JANUARY, 25);
LocalDate inicioUniversidad = LocalDate.parse("2022-08-01");
LocalDate proximoExamen = hoy.plusDays(7);
```

`of` recibe campos; `parse` interpreta texto ISO. Los meses numéricos de `LocalDate` van de 1 a 12, a diferencia de `Calendar`. El enum `Month` evita confundir la numeración. Una fecha imposible como 30 de febrero no se normaliza silenciosamente mediante `LocalDate.of`: falla.

`getDayOfMonth`, `getDayOfYear` y `getDayOfWeek` preguntan cosas diferentes. `DayOfWeek.getValue` numera lunes como 1 y domingo como 7. `getDisplayName` con `Locale` obtiene el nombre legible del mes o día; el locale afecta texto, no cambia la fecha.

`plusDays(7)` cambia en siete días; `withDayOfMonth(1)` reemplaza el día. `isBefore`, `isAfter` e `isLeapYear` permiten preguntar sin convertir a texto. `now()` depende del reloj y de la zona predeterminada; sus resultados y edades cambian con cada fecha de ejecución.

**Comentario atrasado:** la implementación termina con la etiqueta «El 15/07/2003 fue», pero consulta `nacimientoFelipe`, que en el código actual es 25/01/2005. El comentario del inicio universitario también menciona marzo, mientras que el literal parseado es agosto. El resultado corresponde al valor realmente consultado.

### Extraer campos y consultar calendario

```java
// Consultas reducidas de campos sobre una fecha concreta.
LocalDate fecha = LocalDate.of(2005, Month.JANUARY, 25);
int mes = fecha.getMonth().getValue();
int diaDelMes = fecha.getDayOfMonth();
DayOfWeek diaDeSemana = fecha.getDayOfWeek();
boolean bisiesto = fecha.isLeapYear();
```

El mes vale 1 y el día del mes, 25. El día de semana es `TUESDAY`; 2005 no es bisiesto. Las consultas parten del valor de fecha, no de un mensaje de presentación que mencione otra fecha. Ninguna modifica el objeto.

## 3. Horas locales: una hora sin día

`LocalTime.of(8,0)` representa ocho de la mañana y `LocalTime.of(14,30,0)`, dos y media de la tarde. `plus(1,HOURS).plusMinutes(40)` convierte 08:00 en 09:40, dejando intacta la hora inicial. Una suma que cruza medianoche vuelve al comienzo del día; `LocalTime` no registra que pasó al día siguiente.

`DateTimeFormatter.ofPattern("hh:mm:ss a")` usa reloj de 12 horas con indicador AM/PM; `"HH:mm"` usa 24 horas. `MM` significa mes en patrones de fecha, mientras que `mm` significa minuto. `LocalTime.MIN` es medianoche y `MAX` es el último nanosegundo del día, no una hora «24:00».

### Aritmética y presentación separadas

```java
LocalTime finClaseManiana = claseManiana.plus(1, ChronoUnit.HOURS).plusMinutes(40); // 1h40m
System.out.println("Fin clase mañana:    " + finClaseManiana);
```

La clase empieza a las 08:00 y se añaden una hora y cuarenta minutos, obteniendo 09:40. La variable inicial conserva 08:00 porque se ha calculado otro valor. La consulta y el formato posterior no cambian esa inmutabilidad.

```java
DateTimeFormatter fmt12h = DateTimeFormatter.ofPattern("hh:mm:ss a");
DateTimeFormatter fmt24h = DateTimeFormatter.ofPattern("HH:mm");
```

Los formatos describen dos presentaciones del mismo dato. `hh` necesita un marcador de período para distinguir mañana y tarde; `HH` utiliza la numeración de 24 horas. Minutos y meses usan letras distintas en la sintaxis del patrón.

## 4. Fecha y hora local: combinar sin inventar una zona

Se puede crear con campos, juntar `LocalDate` y `LocalTime`, o interpretar `"2025-07-15T09:00:00"`. La `T` separa fecha y hora en el formato ISO. El ejemplo de entrega de esta clase sigue usando 2025; el de `Duration` usa 2027. Son datos independientes, no la misma entrega.

`entrega.minusHours(2)` calcula recordatorio y `entrega.plusDays(3).withHour(10).withMinute(0)` calcula defensa. Un patrón personalizado puede utilizarse tanto para formatear como para interpretar texto. Dar formato no cambia el valor temporal; solo produce texto.

Un `LocalDateTime` no permite saber qué ocurrió primero entre eventos de zonas diferentes sin información adicional. «09:00 en Quito» y «09:00 en Madrid» no son el mismo instante aunque sus campos locales coincidan.

### Interpretación ISO y reemplazo de campos

```java
LocalDateTime examen = LocalDateTime.parse("2025-07-15T09:00:00");
System.out.println("Examen final:" + examen);
```

El texto representa el 15 de julio de 2025 a las 09:00. La separación con T contiene fecha y hora, pero no desplazamiento UTC ni región. Sin zona, no se puede convertir a un momento global sin información adicional.

```java
LocalDateTime recordatorio = entrega.minusHours(2);   // 2h antes de la entrega
LocalDateTime defensa      = entrega.plusDays(3).withHour(10).withMinute(0);
```

Restar horas conserva el propósito de un aviso previo; reemplazar campos después de sumar días fija la hora de una actividad posterior. El orden de operaciones es parte de la regla y puede ser relevante cerca de cambios de día o mes.

## 5. Fechas con zona: cambiar representación manteniendo el instante

En el ejemplo de fechas con zona:

```java
LocalDateTime fechaLocal = LocalDateTime.of(2025, Month.JUNE, 15, 8, 0);
ZonedDateTime enEcuador = fechaLocal.atZone(zonaEcuador);
ZonedDateTime enMadrid = enEcuador.withZoneSameInstant(zonaMadrid);
```

`atZone` interpreta los campos locales en `America/Guayaquil`. `withZoneSameInstant` cambia zona y hora visible **conservando el momento**. Para esa fecha del ejemplo, las 08:00 en Guayaquil corresponden a las 15:00 en Madrid. El vuelo añade doce horas a ese momento y llega con representación de Madrid al día siguiente.

`ZoneOffset.of("-05:00")` es un desplazamiento fijo respecto de UTC. `ZoneId.of("Europe/Madrid")` identifica una región cuyas reglas pueden cambiar el desplazamiento según la fecha. Un offset fijo no aplica por sí mismo las reglas regionales. Horas locales ambiguas o inexistentes durante cambios horarios requieren prestar atención a cómo se resuelve la zona.

Para medir tiempo realmente transcurrido entre eventos con zonas, se pueden comparar sus instantes (`toInstant`), como extensión conceptual. `Instant` no tiene una clase de ejemplo propia en este módulo. Las reglas concretas de zonas las aporta la base temporal del JDK.

### Convertir un horario y añadir tiempo de viaje

```java
ZonedDateTime llegadaMadrid = salidaQuito
        .withZoneSameInstant(zonaMadrid)
        .plusHours(12);
```

La conversión a Madrid conserva el instante de salida. Después las doce horas avanzan sobre la línea temporal; no representan una simple sustitución del número visible de hora. La llegada se presenta con la zona de destino y puede corresponder a otra fecha local.

## 6. Duraciones: totales y componentes

El ejemplo de duraciones mide una sesión:

```java
LocalTime inicioEstudio = LocalTime.of(9, 0);
LocalTime finEstudio = LocalTime.of(13, 30);
Duration sesion = Duration.between(inicioEstudio, finEstudio);
```

La sesión tiene 270 minutos. `toHours()` devuelve 4 horas completas; `toMinutes()` devuelve el total 270; `toMinutesPart()` devuelve el resto 30. `toHoursPart()` da las horas restantes después de días completos, lo que importa en duraciones superiores a 24 horas.

`plusMinutes(15)` da 285 minutos; `dividedBy(2)` da 135. `between(fin,inicio)` da una cantidad negativa y `abs` su magnitud positiva. Las conversiones a días u horas no redondean hacia el próximo entero: devuelven unidades completas.

Medir entre `LocalDateTime` calcula diferencia de campos locales sin reglas de zona; no garantiza medir tiempo físico durante un cambio horario. Tampoco se puede deducir si 01:00 es «mañana» respecto de 23:00 usando solo `LocalTime`: faltan las fechas. `Duration` no sirve con `LocalDate` directamente porque esta no contiene segundos.

### Interpretar las unidades de una duración

```java
Duration sesionConDescanso = sesion.plusMinutes(15);  // + descanso
Duration mitad             = sesion.dividedBy(2);
System.out.println("Con descanso: " + sesionConDescanso.toMinutes() + " min");
System.out.println("Mitad sesión: " + mitad.toMinutes() + " min");
```

La duración inicial de 270 minutos pasa a 285 al incluir quince de descanso. Dividir por dos obtiene 135 minutos. Son cantidades temporales, no horas del reloj; media sesión no significa automáticamente una cita a las 02:15.

```java
Duration negativo = Duration.between(finEstudio, inicioEstudio);
System.out.println("Duration negativa: " + negativo.toMinutes() + " min");
System.out.println("¿Es negativa?      " + negativo.isNegative());
System.out.println("Absoluta (abs):    " + negativo.abs().toMinutes() + " min");
```

Invertir los extremos cambia el signo a −270 minutos. `abs` permite obtener magnitud cuando se necesita una distancia temporal, pero perder el signo también pierde la información de dirección.

## 7. Períodos de calendario: componentes de calendario

`Period.between(nacimiento,hoy)` devuelve años, meses y días. El ejemplo usa nacimiento 15/07/2003, diferente del del ejemplo de fechas locales. `getDays` no significa «total de días vividos»: es la parte restante tras años y meses. Para total de días entre fechas se puede usar `ChronoUnit.DAYS.between`, como adaptación al objetivo.

El semestre entre 01/03/2025 y 31/07/2025 tiene período `P4M30D`: cuatro meses y treinta días. `Period.ofWeeks(2)` representa catorce días. `hoy.plus(Period.of(2,6,0))` aplica dos años y seis meses de calendario, que no tienen una duración fija en segundos.

Un período de un día avanza la fecha de calendario; una duración de 24 horas avanza una cantidad temporal fija. Al operar sobre una fecha zonificada durante cambios horarios, esas dos intenciones pueden dar horas visibles diferentes.

### Edad como componentes de calendario

```java
Period edad = Period.between(nacimiento, hoy);
System.out.printf("Felipe tiene %d años, %d meses y %d días%n",
        edad.getYears(), edad.getMonths(), edad.getDays());
```

Años, meses y días son partes de un período entre dos fechas. Los meses restantes no son el número total de meses vividos y los días restantes no son el total de días. El cálculo depende de la fecha final; el instante exacto de la hora no participa.

```java
Period personalizado = Period.of(2, 3, 10);  // 2 años, 3 meses, 10 días
```

Aplicar ese período suma dos años, tres meses y diez días de calendario. Su equivalencia en segundos depende de la fecha y, si se aplica a una fecha zonificada, de las reglas temporales correspondientes.

## Preguntas de repaso

1. ¿`plusDays` cambia el objeto original? **No; devuelve otro valor.**
2. ¿`LocalDateTime` incluye zona? **No.**
3. ¿`Period.getDays()` es el total de días? **No; es un componente.**
4. ¿Qué conserva `withZoneSameInstant`? **El instante; puede cambiar fecha y hora local.**

Los comentarios que llaman obsoletas a todas las APIs clásicas no deben interpretarse como que `Date` y `Calendar` completos estén deprecados. Aquí se estudia una alternativa más precisa para representar distintas necesidades temporales.
