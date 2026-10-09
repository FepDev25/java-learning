# 06 · Hilos y concurrencia: ejecutar y coordinar tareas

## Contenido

- [1. Una tarea puede avanzar mientras otra espera](#1-una-tarea-puede-avanzar-mientras-otra-espera)
- [2. Tres formas de definir trabajo](#2-tres-formas-de-definir-trabajo)
- [3. Proteger una secuencia compartida](#3-proteger-una-secuencia-compartida)
- [4. Un buzón con una posición](#4-un-buzón-con-una-posición)
- [5. Programar trabajo para después](#5-programar-trabajo-para-después)
- [6. Separar la tarea del pool](#6-separar-la-tarea-del-pool)
- [Preguntas de repaso](#preguntas-de-repaso)

## 1. Una tarea puede avanzar mientras otra espera

Un hilo tiene su propio recorrido de ejecución. Varios hilos dentro del mismo proceso pueden compartir objetos y alternarse al usar el procesador; si hay recursos suficientes, también pueden trabajar en paralelo. **Concurrencia** trata de tareas que progresan en períodos superpuestos; **paralelismo**, de ejecución simultánea. Crear tres hilos no garantiza tres procesadores ni un orden de mensajes.

El hilo principal ejecuta `main`. Los ejemplos del módulo crean otros para simular estudio, viajes, descarga, impresión y recordatorios. Sus pausas y mensajes representan trabajo: el texto «42 tests passed» del pool es una cadena simulada, no una ejecución de tests reales.

## 2. Tres formas de definir trabajo

`TareaFelipe extends Thread` redefine `run` y recibe el nombre del hilo. El ejemplo de hilos mediante herencia crea tres instancias, las inicia y espera a todas con `join`. Extender `Thread` mezcla tarea y mecanismo de ejecución, y consume la única herencia de clase disponible.

`TareaEstudio implements Runnable` solo define la tarea. El ejemplo de tareas ejecutables la entrega a un hilo:

```java
Thread t1 = new Thread(new TareaEstudio("Cálculo I"), "Hilo-Calculo");
```

La materia es estado del objeto tarea; `"Hilo-Calculo"` es el nombre del hilo que la ejecutará. Una clase que ya herede otra también puede implementar `Runnable`. El ejemplo de tareas mediante lambdas escribe el mismo contrato con una lambda y reutiliza una tarea para viajes a distintas ciudades, obteniendo el nombre desde `Thread.currentThread()`.

### Trabajo declarado como una tarea

```java
public void run() {
    System.out.println("INICIO Felipe estudia: " + materia + " | hilo: " + Thread.currentThread().getName());

    for (int i = 1; i <= 4; i++) {
        System.out.println("  " + materia + " → ejercicio " + i);
        try {
            Thread.sleep((long) (Math.random() * 700));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    System.out.println("FIN Felipe terminó de estudiar: " + materia + " | hilo: " + Thread.currentThread().getName());
}
```

El trabajo pertenece a `run`; el hilo se ocupa de ejecutarlo. Cada tarea conserva la materia en su propio estado. El nombre del hilo se consulta durante la ejecución, no al construir la tarea. Las pausas hacen observable la alternancia de tareas, pero no imponen un orden global.

```java
// Inicialización y espera reducidas del ejemplo de tareas.
Thread t1 = new Thread(new TareaEstudio("Cálculo I"), "Hilo-Calculo");
Thread t2 = new Thread(new TareaEstudio("Programación OOP"), "Hilo-OOP");
t1.start();
t2.start();
t1.join();
t2.join();
```

Ambos hilos se inician antes de las esperas. El principal queda suspendido hasta la terminación de cada uno, mientras que los trabajadores pueden avanzar concurrentemente. Llamar a `run` en lugar de `start` cambiaría esa relación por dos llamadas ordinarias.

### `start`, `run`, `join` y `sleep`

`start()` pide iniciar un nuevo hilo que ejecutará `run`. Llamar `run()` directamente sería una llamada ordinaria en el hilo actual. Un mismo objeto `Thread` se inicia una sola vez; repetir `start` produce error.

`join()` hace esperar al hilo que llama hasta que el otro termine. Si primero se inician todos y luego se llama a `join` sobre cada uno, las tareas ya pueden avanzar concurrentemente; no se vuelven secuenciales por esperar sus resultados en un orden.

`Thread.sleep` pausa el hilo actual por un tiempo aproximado. No prueba que otro hilo haya terminado y **no libera un monitor** que el hilo ya posea. Una pausa no garantiza el orden lógico entre tareas.

`getState` observa un estado que puede cambiar enseguida: `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING` o `TERMINATED`. El estado impreso desde el final de `run` de `TareaFelipe` no tiene por qué ser `TERMINATED`: el hilo todavía está ejecutando esa impresión.

### Interrumpir requiere una política

Una interrupción es una solicitud, no una terminación forzada. `sleep`, `wait` y otras esperas pueden lanzar `InterruptedException`. Varios ejemplos restablecen la bandera mediante `Thread.currentThread().interrupt()`, pero luego continúan el bucle; `TareaFelipe` solo imprime el error, y `Lector` lo convierte en `RuntimeException`. Esas decisiones no equivalen a una cancelación coordinada.

Para una tarea cancelable, una adaptación habitual es restaurar la bandera y **salir** de `run`, o propagar la interrupción al nivel que controle el ciclo de vida. La política debe establecer qué ocurre con la otra tarea que esté esperando; restaurar la bandera por sí solo no resuelve el protocolo.

### Salida cooperativa ante interrupción

```java
// Adaptación que añade una política de terminación.
Runnable tarea = () -> {
    try {
        Thread.sleep(500);
        System.out.println("Paso completado");
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
    }
};
```

La restauración de la bandera comunica la solicitud recibida y el retorno evita continuar con el trabajo normal. Este comportamiento difiere de restaurar y volver a intentar indefinidamente la misma espera. En una colaboración de varias tareas también hace falta decidir cómo se informa a las que dependían de esta.

## 3. Proteger una secuencia compartida

Una condición de carrera aparece cuando el resultado depende de cómo se intercalan operaciones sobre estado compartido. Incluso si una instrucción parece breve, una actualización puede implicar leer, calcular y escribir.

El ejemplo de impresión sincronizada protege dos impresiones y una pausa:

```java
public synchronized static void imprimirApunte(String titulo, String contenido) {
    System.out.print("Felipe anota: " + titulo);
    try {
        Thread.sleep(600);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    System.out.println(contenido);
}
```

Al ser `static synchronized`, usa el monitor de `EjemploSincronizacion.class`. Todas las tareas `ImprimirApunte` compiten por el mismo monitor. Un hilo entra y mantiene el bloqueo durante la pausa; los otros no entran hasta que salga. Así el título y el contenido quedan juntos. No se garantiza quién entra primero.

Un método `synchronized` de instancia usaría `this`. Si cada hilo tuviera un objeto distinto, esos monitores distintos no protegerían una operación compartida entre ellos. El bloqueo también establece visibilidad de cambios entre quienes usan el mismo monitor; no se limita a impedir dos entradas simultáneas.

## 4. Un buzón con una posición

`ColaApuntes` tiene un texto y una bandera `disponible`. Aunque se llama cola, su capacidad real es **un apunte**. El escritor no debe sobrescribir uno sin leer; el lector no debe consumir cuando está vacío.

Fragmento del productor:

```java
while (disponible) {
    try {
        wait();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
this.apunte = tema;
System.out.println("E -> Felipe escribió apunte: " + apunte);
this.disponible = true;
notify();
```

Los métodos son sincronizados sobre el **mismo buzón**. `wait()` libera ese monitor y suspende al hilo; antes de retornar debe volver a adquirirlo. `notify()` despierta a uno de quienes esperan, pero no le entrega el monitor inmediatamente. Lo obtiene cuando el notificante salga y pueda competir por él.

El lector hace lo complementario: espera mientras no esté disponible, obtiene el apunte, pone la bandera en falso y notifica. El ciclo normal es:

```text
vacío → escritor guarda → lleno → lector consume → vacío
```

El `while` reevalúa la condición después de despertar. Es necesario porque puede haber despertares espurios o cambios antes de adquirir de nuevo el monitor. Sustituirlo por `if` podría permitir continuar sin que se cumpla la condición.

`Escritor` publica diez temas; `Lector` lee diez veces. Iniciar primero al lector funciona: esperará al primer dato. Si los conteos no coincidieran, alguno podría quedar esperando indefinidamente. El ejemplo usa un productor y un consumidor; con varios participantes, un único `notify` puede despertar a quien no puede avanzar. `notifyAll` o una `BlockingQueue` con un protocolo de finalización facilitan una generalización, pero no están implementados aquí.

**Problema de interrupción actual:** en el `catch` de `wait` se restaura la bandera y se vuelve a esperar si la condición sigue igual. La siguiente espera puede lanzar inmediatamente otra interrupción, formando un bucle que impide progreso. Además, si `Lector` termina por su excepción, el escritor puede quedar bloqueado con el buzón lleno. El ejemplo explica el intercambio normal; no tiene una estrategia completa de cancelación.

### La operación complementaria de consumo

```java
public synchronized String leer() {
    while (!disponible) {
        try {
            wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    System.out.println("L -> Felipe lee apunte:    " + apunte);
    this.disponible = false;
    notify(); // avisa al Escritor
    return apunte;
}
```

La comprobación y el cambio de bandera ocurren bajo el mismo monitor. Si el buzón está vacío, el consumidor libera el monitor mediante `wait` para permitir que el productor lo llene. Al obtener un apunte, marca vacío y notifica antes de retornar el texto consumido. El uso de `while` mantiene la condición como requisito aun después de un despertar.

## 5. Programar trabajo para después

El ejemplo de recordatorio único crea un `TimerTask` para ejecutarlo aproximadamente tres segundos después y luego llama a `timer.cancel`. El hilo principal sigue mientras espera. El ejemplo de recordatorios periódicos usa `scheduleAtFixedRate`, retardo inicial de un segundo y período de dos; cancela al cuarto recordatorio.

El contador es `AtomicInteger`: `incrementAndGet` incrementa y devuelve el resultado como una operación atómica. Eso evita separar incremento y lectura en tareas concurrentes. Un `Timer` ejecuta sus tareas con un solo hilo: una tarea lenta retrasa a otras, y una excepción no controlada puede terminar ese hilo. Cancelarlo libera su actividad.

### Recordatorio periódico con condición de finalización

```java
timer.scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        int vez = contador.incrementAndGet();
        System.out.println("💧 Recordatorio #" + vez + " → Toma agua, Felipe! | " + new Date());
        if (vez >= 4) {
            System.out.println("Ya tomó suficiente agua. Cancelando timer.");
            timer.cancel();
        }
    }
}, 1000, 2000); // delay inicial 1s, luego cada 2s
```

El primer argumento es una tarea; los siguientes indican retardo inicial y período en milisegundos. El contador incrementa antes de compararse, y la cuarta ejecución cancela el timer. Las impresiones muestran las ejecuciones reales observadas, que pueden retrasarse respecto del calendario ideal.

## 6. Separar la tarea del pool

`ExecutorService` recibe tareas y gestiona hilos reutilizables. El ejemplo de ejecución con un trabajador usa `newSingleThreadExecutor`, envía una descarga y llama a `shutdown`. **Shutdown deja de aceptar tareas nuevas y permite terminar las ya aceptadas**; no las termina inmediatamente. `awaitTermination(3, SECONDS)` espera como máximo ese intervalo y devuelve si finalizó. Como la tarea duerme tres segundos y hay otros costos, el booleano no es un resultado fijo.

El ejemplo de pool de tamaño fijo configura tres trabajadores. El pool no necesariamente crea los tres al construirlo: puede iniciarlos según llegan tareas. Un `Runnable` no devuelve un resultado; `Callable<T>` sí y puede lanzar excepciones comprobadas.

```java
Future<String> f1 = executor.submit(compilarProyecto);
Future<String> f2 = executor.submit(ejecutarTests);
Future<Integer> f3 = executor.submit(contarLineas);
```

`Future` representa un resultado pendiente. `get()` espera y lo obtiene; si la tarea falla, puede lanzar `ExecutionException`. `isDone` también es verdadero al finalizar con error o cancelación, así no significa éxito. Un `Future<?>` de `submit(Runnable)` normalmente devuelve nulo cuando acaba correctamente.

El ejemplo de productor y consumidor con un pool envía escritor y lector a un pool de **dos** trabajadores y espera ambos futures. Con uno, la primera tarea podría ocupar el único hilo y esperar a una segunda que siga en cola: separar tareas no garantiza que haya capacidad para ejecutarlas.

El ejemplo de planificación con un pool programa una tarea única y un recordatorio con retardo 500 ms y período 1500 ms. Aunque el comentario dice «máximo 4 veces», el código no comprueba cuatro: cancela después de dormir siete segundos y normalmente permite **cinco** comienzos (0.5, 2, 3.5, 5 y 6.5 s), sujeto a planificación. `cancel(false)` no interrumpe una ejecución ya iniciada. Una excepción en una tarea periódica también puede suprimir futuras ejecuciones de esa tarea.

### Resultado pendiente de un cálculo

```java
Callable<Integer> contarLineas = () -> {
    System.out.println("Contando líneas de código... | " + Thread.currentThread().getName());
    TimeUnit.SECONDS.sleep(1);
    return 1_340;
};
```

La tarea devuelve un entero en vez de limitarse a imprimir. Su resultado se obtiene después de enviarla al executor mediante un future. La pausa simula trabajo y el número 1340 es un dato preparado, no un conteo efectivo del repositorio.

```java
// Secuencia reducida de envío, cierre y obtención del resultado.
ExecutorService executor = Executors.newFixedThreadPool(3);
Future<Integer> resultado = executor.submit(contarLineas);
executor.shutdown();
System.out.println(resultado.get());
```

`shutdown` establece que no se aceptarán tareas nuevas. `get` espera si el valor todavía no está disponible y puede propagar fallo o interrupción. Esperar el resultado no cancela ni vuelve a ejecutar la tarea.

### Programación temporal con un pool

```java
ScheduledFuture<?> futuro = scheduler.scheduleAtFixedRate(() -> {
    int vez = contador.incrementAndGet();
    System.out.println("[" + LocalTime.now() + "] 💡 Repasa tema " + vez + " de Algoritmos");
}, 500, 1500, TimeUnit.MILLISECONDS);
```

La repetición la controla el planificador, no el contador por sí solo. Este solo etiqueta ejecuciones; en el ejemplo la cancelación se realiza desde otra parte después de siete segundos. Una condición basada en cantidad tendría que ejecutar una cancelación explícita al alcanzar su límite.

## Preguntas de repaso

El orden global de los mensajes puede variar; cada mensaje pertenece a la tarea que lo emitió.

1. ¿`run()` crea otro hilo? **No; `start()` inicia la ejecución en otro hilo.**
2. ¿`sleep` libera el monitor? **No; `wait` sí lo libera mientras espera.**
3. ¿`notify` garantiza que el otro ya consumió? **No; solo avisa a un hilo en espera.**
4. ¿`shutdown` cancela lo enviado? **No; permite completar lo aceptado.**
5. ¿Compartir un `Runnable` siempre es seguro? **Depende de si su estado compartido se modifica y de cómo se coordina.**
