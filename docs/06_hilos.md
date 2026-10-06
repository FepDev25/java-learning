# P06 - Hilos y Concurrencia

## Descripción General

Introducción a la programación concurrente en Java: creación de hilos, sincronización,
el patrón productor-consumidor, `ExecutorService` y `Timer`.

## Información del Proyecto

- **Artifact ID:** p06_hilos
- **Group ID:** com.cultodeportivo
- **Versión:** 1.0-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven

## Contenido del Módulo

### 1. `hilos` — Creación de hilos

- `hilos/EjemploExtenderThread.java` - Opción 1: extender `Thread`.
- `hilos/EjemploRunnable.java` - Opción 2: implementar `Runnable`.
- `hilos/EjemploRunnableLambda.java` - Opción 3: `Runnable` como lambda (Java 8+).
- `hilos/threads/TareaFelipe.java` - Tarea que extiende `Thread`.
- `hilos/runnable/TareaEstudio.java` - Tarea que implementa `Runnable`.

### 2. `sincronizacion` — Condiciones de carrera

- `sincronizacion/EjemploSincronizacion.java` - Uso de `synchronized` para proteger
  una sección crítica (salida de líneas sin mezclarse entre hilos).
- `sincronizacion/runnable/ImprimirApunte.java` - Tarea que invoca el método sincronizado.

### 3. `productorconsumidor` — Patrón Productor-Consumidor

- `productorconsumidor/ColaApuntes.java` - Monitor compartido con `wait()` / `notify()`.
- `productorconsumidor/EjemploProductorConsumidor.java` - `Escritor` produce, `Lector` consume.
- `productorconsumidor/runnable/Escritor.java`, `runnable/Lector.java`.

### 4. `executors` — Gestión de pools de hilos

- `executors/EjemploExecutorService.java` - `newSingleThreadExecutor()` y `shutdown()`.
- `executors/EjemploFixedThreadPool.java` - `newFixedThreadPool(n)` con `Callable` y `Future`.
- `executors/EjemploScheduledExecutor.java` - `schedule()` y `scheduleAtFixedRate()`.
- `executors/EjemploExecutorProductorConsumidor.java` - Productor-consumidor con pool.

### 5. `timer` — Temporizadores

- `timer/EjemploTimer.java` - `Timer` ejecuta una tarea una vez tras un delay.
- `timer/EjemploTimerPeriodo.java` - Tarea periódica con `AtomicInteger` thread-safe.

## Conceptos Clave Aprendidos

- **Hilo:** unidad de ejecución; `start()` lanza el hilo, `run()` contiene el código.
- **Runnable vs Thread:** implementar `Runnable` es más flexible (permite heredar otra clase).
- **join():** bloquea el hilo actual hasta que otro termine.
- **Race condition:** dos hilos modifican un recurso compartido sin control.
- **synchronized:** garantiza exclusión mutua sobre el monitor del objeto/clase.
- **wait()/notify():** comunicación entre hilos; `wait` libera el lock y suspende.
- **ExecutorService:** pool de hilos reutilizables; evita crear hilos manualmente.
- **Future/Callable:** resultado asíncrono con valor de retorno y excepciones.
- **AtomicInteger:** contador thread-safe sin `synchronized`.
- **Timer vs ScheduledExecutorService:** se recomienda el segundo (mejor manejo de excepciones).

## Ejecución de Ejemplos

```bash
cd p06_hilos
mvn clean compile
mvn exec:java -Dexec.mainClass="com.cultodeportivo.<paquete>.<Clase>"
```

Por ejemplo:

```bash
mvn exec:java -Dexec.mainClass="com.cultodeportivo.hilos.EjemploRunnableLambda"
mvn exec:java -Dexec.mainClass="com.cultodeportivo.productorconsumidor.EjemploProductorConsumidor"
```

## Estructura de Paquetes

```text
com.cultodeportivo
├── executors/
├── hilos/
│   ├── runnable/
│   └── threads/
├── productorconsumidor/
│   └── runnable/
├── sincronizacion/
│   └── runnable/
└── timer/
```

## Notas Técnicas

- Los ejemplos usan `Thread.sleep()` para simular trabajo; declaran `InterruptedException`.
- Se recomienda `Runnable` sobre extender `Thread` para no limitar la herencia.
- En aplicaciones reales preferir `java.util.concurrent` sobre `Timer` y `Thread` crudo.

## Referencias

- [Concurrency - Oracle](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [java.util.concurrent](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html)
