# TUTORIAL: CONCURRENCIA EN JAVA - DEL HARDWARE A LA JVM

> **Requisitos previos:** Entender POO, conocer estructuras de datos básicas (ArrayList, Queue).
>
> **Objetivo:** Comprender cómo funcionan los hilos a nivel de CPU y memoria, y dominar las herramientas de sincronización en Java.

---

## PARTE 1: ¿QUÉ ES UN HILO? (ARQUITECTURA DE HARDWARE)

### 1.1 CPU, Cores y Threads

Tu procesador moderno (Intel/AMD) tiene:

```bash
CPU (Die Físico)
├── Core 1
│   ├── Thread 1 (Hardware Thread / Hyper-Threading)
│   └── Thread 2
├── Core 2
│   ├── Thread 1
│   └── Thread 2
└── ...
```

**Ejemplo:** Intel Core i7-12700K tiene:

- 12 cores (8 P-cores + 4 E-cores)
- 20 threads (16 + 4)

**En Arch Linux, verifica tu CPU:**

```bash
lscpu | grep -E "^CPU\(s\)|Core|Thread"
```

### 1.2 Memoria RAM y Cachés

```bash
┌─────────────┐
│     CPU     │
│  ┌───────┐  │
│  │ L1 $  │  │ ← 32 KB, latencia ~4 ciclos (1 ns)
│  ├───────┤  │
│  │ L2 $  │  │ ← 256 KB, latencia ~12 ciclos (3 ns)
│  └───────┘  │
└──────┬──────┘
       │
   ┌───┴───┐
   │ L3 $  │      ← 8-32 MB, latencia ~40 ciclos (12 ns)
   └───┬───┘
       │
   ┌───┴────┐
   │  RAM   │     ← GB, latencia ~200 ciclos (60 ns)
   └────────┘
```

**El problema:** Cada core tiene su propia caché L1/L2. Si dos hilos (en cores diferentes) modifican la misma variable:

```java
// Thread 1 (Core 1)         // Thread 2 (Core 2)
int balance = 1000;          int balance = 1000;  // Copia en L1$
balance += 500;  // 1500     balance -= 200;      // 800 (PROBLEMA!)
```

Sin sincronización:

- Ambos leen `balance = 1000` desde RAM
- Cada uno trabaja con su copia en caché
- Ambos escriben a RAM → **Última escritura gana** → Dato perdido

---

## PARTE 2: RACE CONDITIONS (EL ENEMIGO #1)

### 2.1 Anatomía de un Race Condition

**Código aparentemente simple:**

```java
public class BankAccount {
    private int balance = 1000;

    public void withdraw(int amount) {
        balance = balance - amount;  // ← PELIGRO
    }
}
```

**¿Qué pasa en bytecode?** (Usa `javap -c BankAccount.class` para verlo)

```assembly
; balance = balance - amount
 0: aload_0           // Cargar 'this'
 1: getfield #2       // Leer balance → Stack
 4: iload_1           // Leer amount → Stack
 5: isub              // Restar
 6: putfield #2       // Escribir resultado a balance
```

**Interleaving catastrófico:**

| Tiempo | Thread 1 (withdraw 500)      | Thread 2 (withdraw 300)      | Balance RAM |
|--------|------------------------------|------------------------------|-------------|
| T1     | getfield → lee 1000          |                              | 1000        |
| T2     |                              | getfield → lee 1000          | 1000        |
| T3     | isub → 1000-500=500          |                              | 1000        |
| T4     |                              | isub → 1000-300=700          | 1000        |
| T5     | putfield → escribe 500       |                              | **500**     |
| T6     |                              | putfield → escribe 700       | **700**     |

**Resultado:** ¡Balance final es 700 en lugar de 200! Perdimos $500.

### 2.2 El Test que Lo Demuestra

```java
@Test
void demonstrateLostUpdates() {
    class UnsafeCounter {
        private int count = 0;
        public void increment() { count++; }  // NO atómico
        public int getCount() { return count; }
    }

    UnsafeCounter counter = new UnsafeCounter();
    ExecutorService executor = Executors.newFixedThreadPool(100);

    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> counter.increment());
    }

    executor.shutdown();
    executor.awaitTermination(10, TimeUnit.SECONDS);

    System.out.println("Esperado: 100000");
    System.out.println("Obtenido: " + counter.getCount());  // ~94783 (varía)
}
```

**Output típico:**

```bash
Esperado: 100000
Obtenido: 94823  ← Perdimos ~5177 incrementos
```

---

## PARTE 3: EL JAVA MEMORY MODEL (JMM)

### 3.1 Heap vs Stack (Por Hilo)

```java
public class Example {
    private int sharedVar = 0;  // Heap (compartido)

    public void method() {
        int localVar = 5;       // Stack (privado del hilo)
        sharedVar += localVar;  // Acceso a Heap → SINCRONIZAR
    }
}
```

**Memoria en ejecución:**

```bash
Thread 1                      Thread 2
┌──────────────┐             ┌──────────────┐
│    Stack     │             │    Stack     │
│ ┌──────────┐ │             │ ┌──────────┐ │
│ │localVar=5│ │             │ │localVar=5│ │
│ └──────────┘ │             │ └──────────┘ │
└──────┬───────┘             └──────┬───────┘
       │                             │
       └──────────┬──────────────────┘
                  ▼
           ┌──────────────┐
           │     Heap     │
           │  sharedVar=0 │ ← ZONA PELIGROSA
           └──────────────┘
```

**Regla de oro:**

- Variables locales → Stack → Thread-safe automáticamente
- Variables de instancia/clase → Heap → Requieren sincronización

### 3.2 Reordenamiento de Instrucciones

El compilador JIT y la CPU pueden **reordenar** operaciones para optimizar:

```java
// Código escrito
public void writer() {
    data = 42;
    ready = true;
}

// Posible ejecución real (REORDENADO)
public void writer() {
    ready = true;   // ← Movido primero
    data = 42;
}

// Thread lector puede ver:
public void reader() {
    if (ready) {
        System.out.println(data);  // ¡Podría imprimir 0!
    }
}
```

**Solución:** Establecer **happens-before** relationships (ver sección volatile).

---

## PARTE 4: SINCRONIZACIÓN EN JAVA

### 4.1 `synchronized` - El Intrinsic Lock

#### Sintaxis 1: Método sincronizado

```java
public class OrderBook {
    private final List<Order> bids = new ArrayList<>();

    public synchronized void addOrder(Order order) {
        bids.add(order);
    }
}
```

**Equivalente a:**

```java
public void addOrder(Order order) {
    synchronized(this) {  // Lock en el objeto actual
        bids.add(order);
    }
}
```

#### Sintaxis 2: Bloque sincronizado (más granular)

```java
public class OrderBook {
    private final List<Order> bids = new ArrayList<>();
    private final List<Order> asks = new ArrayList<>();
    private final Object bidsLock = new Object();
    private final Object asksLock = new Object();

    public void addBid(Order order) {
        synchronized(bidsLock) {  // Solo bloquea bids
            bids.add(order);
        }
    }

    public void addAsk(Order order) {
        synchronized(asksLock) {  // Diferente lock → Concurrencia!
            asks.add(order);
        }
    }
}
```

**Ventaja:** Dos hilos pueden agregar bid y ask simultáneamente (no compiten por el mismo lock).

#### Cómo funciona `synchronized` (Monitor)

```bash
Thread 1 llega a synchronized(obj)
    ↓
¿Alguien tiene el lock de 'obj'?
    ↓
   NO → Adquiere lock, ejecuta bloque, libera lock
    ↓
   SÍ → Se bloquea (WAITING), entra a cola de espera
```

**Bajo el capó (Linux):**

```bash
# Ver syscalls al ejecutar código sincronizado
strace -e futex java MyProgram

# Output:
futex(0x7f8c4c001234, FUTEX_WAIT_PRIVATE, 2, NULL) = 0
futex(0x7f8c4c001234, FUTEX_WAKE_PRIVATE, 1)      = 1
```

`futex` = Fast Userspace Mutex (kernel de Linux).

### 4.2 `volatile` - Visibility sin Locks

#### El problema de visibilidad

```java
public class StopFlag {
    private boolean stop = false;  // No volatile

    public void writer() {
        stop = true;  // Thread 1
    }

    public void reader() {
        while (!stop) {  // Thread 2 podría NO ver el cambio nunca
            // Trabajo...
        }
        System.out.println("Detenido!");
    }
}
```

**¿Por qué puede fallar?**

1. El compilador puede cachear `stop` en un registro (optimización).
2. La CPU puede mantener `stop = false` en L1 cache sin actualizar desde RAM.

#### La solución: `volatile`

```java
private volatile boolean stop = false;
```

**Garantías de `volatile`:**

1. **Visibility:** Toda escritura es visible inmediatamente en todos los hilos.
2. **Happens-before:** Escritura de `volatile` ocurre antes de cualquier lectura posterior.
3. **No reordenamiento:** Las instrucciones antes/después de `volatile` no se reordenan cruzando la barrera.

**Implementación (x86 Assembly):**

```assembly
; Escritura a variable volatile
mov    [rax], 1         ; Escribir valor
mfence                  ; Memory fence (barrera de memoria)
```

`mfence` = CPU instruction que fuerza sincronización de cachés.

#### ¿Cuándo NO es suficiente `volatile`?

```java
private volatile int counter = 0;

public void increment() {
    counter++;  // ← AÚN NO ES ATÓMICO (read-modify-write)
}
```

`volatile` garantiza visibilidad, pero **NO atomicidad** de operaciones compuestas.

**Solución:** Usar `AtomicInteger` (ver sección 4.4).

### 4.3 `ReentrantLock` - Control Explícito

#### Ventajas sobre `synchronized`

1. **Interruptibilidad:**

   ```java
   try {
       lock.lockInterruptibly();  // Puede ser interrumpido
       // ...
   } catch (InterruptedException e) {
       // Manejar interrupción
   } finally {
       lock.unlock();
   }
   ```

2. **Trylock con timeout:**

   ```java
   if (lock.tryLock(1, TimeUnit.SECONDS)) {
       try {
           // Zona crítica
       } finally {
           lock.unlock();
       }
   } else {
       System.out.println("No pude adquirir el lock, abortando");
   }
   ```

3. **Fairness (justicia):**

   ```java
   ReentrantLock lock = new ReentrantLock(true);  // FIFO
   ```

   Con `fair=true`, los hilos adquieren el lock en orden de llegada.

4. **Condiciones múltiples:**

   ```java
   ReentrantLock lock = new ReentrantLock();
   Condition notEmpty = lock.newCondition();
   Condition notFull = lock.newCondition();

   public void put(Order order) throws InterruptedException {
       lock.lock();
       try {
           while (queue.size() == MAX_SIZE) {
               notFull.await();  // Esperar a que haya espacio
           }
           queue.add(order);
           notEmpty.signal();    // Notificar a consumidores
       } finally {
           lock.unlock();
       }
   }
   ```

#### ReentrantReadWriteLock (Optimización Lecturas)

```java
public class OrderBook {
    private final List<Order> bids = new ArrayList<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public void addOrder(Order order) {
        writeLock.lock();  // Escritura exclusiva
        try {
            bids.add(order);
        } finally {
            writeLock.unlock();
        }
    }

    public List<Order> getBids() {
        readLock.lock();   // Múltiples lectores OK
        try {
            return new ArrayList<>(bids);
        } finally {
            readLock.unlock();
        }
    }
}
```

**Beneficio:** 100 hilos pueden llamar `getBids()` simultáneamente (compartiendo `readLock`), pero `addOrder()` requiere exclusividad.

### 4.4 `AtomicInteger` y Familia Atomic

#### Operaciones atómicas sin locks

```java
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();  // Atómico: read-modify-write
    }

    public int get() {
        return count.get();
    }
}
```

**Bajo el capó (CPU x86):**

```assembly
; incrementAndGet() usa instrucción LOCK CMPXCHG
retry:
  mov eax, [rcx]           ; Leer valor actual
  lea edx, [eax + 1]       ; Calcular nuevo valor
  lock cmpxchg [rcx], edx  ; Compare-And-Swap atómico
  jnz retry                ; Si falló (otra thread cambió), reintentar
```

`LOCK CMPXCHG` = Compare-And-Swap (CAS) a nivel hardware.

#### Operaciones disponibles

```java
AtomicInteger ai = new AtomicInteger(10);

ai.get();                      // 10
ai.incrementAndGet();          // 11 (++count)
ai.getAndIncrement();          // 11, retorna 11 (count++)
ai.addAndGet(5);               // 16
ai.compareAndSet(16, 20);      // true, ahora vale 20
ai.compareAndSet(16, 25);      // false (ya no vale 16)
ai.updateAndGet(x -> x * 2);   // 40 (lambda: operación custom)
```

#### Otros tipos atómicos

```java
AtomicLong counter = new AtomicLong();
AtomicBoolean flag = new AtomicBoolean(false);
AtomicReference<Order> lastOrder = new AtomicReference<>();

// Para arrays
AtomicIntegerArray arr = new AtomicIntegerArray(100);
arr.incrementAndGet(5);  // Incrementa índice 5 atómicamente
```

---

## PARTE 5: PATRONES DE CONCURRENCIA

### 5.1 Producer-Consumer con `BlockingQueue`

#### El patrón clásico

```java
public class MatchingEngine {
    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final Thread processingThread;
    private volatile boolean running = true;

    public MatchingEngine() {
        this.processingThread = new Thread(this::processOrders);
        this.processingThread.setName("MatchingEngine-Worker");
        this.processingThread.setDaemon(false);  // No daemon → espera shutdown
    }

    public void start() {
        running = true;
        processingThread.start();
    }

    public void shutdown() throws InterruptedException {
        running = false;
        processingThread.interrupt();  // Despertar si está bloqueado
        processingThread.join(5000);   // Esperar máximo 5 segundos
    }

    // PRODUCER: Múltiples hilos pueden llamar esto
    public void submitOrder(Order order) throws InterruptedException {
        orderQueue.put(order);  // Bloquea si la cola está llena (bounded)
    }

    // CONSUMER: Solo este thread ejecuta esto
    private void processOrders() {
        while (running || !orderQueue.isEmpty()) {  // Procesar órdenes pendientes
            try {
                Order order = orderQueue.poll(100, TimeUnit.MILLISECONDS);
                if (order != null) {
                    matchOrder(order);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restaurar flag
                break;
            }
        }
    }

    private void matchOrder(Order order) {
        // Lógica de matching...
    }
}
```

#### Tipos de `BlockingQueue`

| Tipo                     | Capacidad  | Ordenamiento         | Uso                          |
|--------------------------|------------|----------------------|------------------------------|
| `LinkedBlockingQueue`    | Ilimitada* | FIFO                 | General purpose              |
| `ArrayBlockingQueue`     | Bounded    | FIFO                 | Control de memoria estricto  |
| `PriorityBlockingQueue`  | Ilimitada  | Comparator/Comparable| Órdenes por prioridad        |
| `SynchronousQueue`       | 0 (handoff)| N/A                  | Transferencia directa        |
| `DelayQueue`             | Ilimitada  | Delay vencido        | Tareas programadas           |

*Ilimitada = Crece dinámicamente (limitada por memoria).

### 5.2 Wait/Notify (Bajo Nivel)

#### Implementación manual de Producer-Consumer

```java
public class BoundedBuffer<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();  // Libera el lock y espera
        }
        queue.add(item);
        notifyAll();  // Despertar a threads esperando en take()
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();  // Esperar a que haya elementos
        }
        T item = queue.poll();
        notifyAll();  // Despertar a threads esperando en put()
        return item;
    }
}
```

#### Reglas de `wait()`/`notify()`

1. **SIEMPRE dentro de `synchronized`:**

   ```java
   synchronized(obj) {
       obj.wait();  // OK
   }
   obj.wait();      // IllegalMonitorStateException
   ```

2. **Usar `while` en lugar de `if`:**

   ```java
   // INCORRECTO
   if (queue.isEmpty()) {
       wait();  // Vulnerable a spurious wakeups
   }

   // CORRECTO
   while (queue.isEmpty()) {
       wait();  // Revisa condición después de despertar
   }
   ```

3. **`notify()` vs `notifyAll()`:**
   - `notify()`: Despierta UN hilo aleatorio → Más eficiente, pero riesgoso
   - `notifyAll()`: Despierta TODOS → Más seguro, garantiza progreso

**Recomendación:** Usa `BlockingQueue` en lugar de `wait/notify` manual (menos propenso a errores).

### 5.3 Immutability (Thread-Safety sin Locks)

#### El patrón más seguro

```java
public final class Order implements Comparable<Order> {
    private final String orderId;
    private final OrderType type;
    private final double price;
    private final int quantity;
    private final Instant timestamp;

    public Order(String symbol, OrderType type, double price, int quantity) {
        this.orderId = UUID.randomUUID().toString();
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = Instant.now();
    }

    // Solo getters, NO setters
    public String getOrderId() { return orderId; }
    public double getPrice() { return price; }
    // ...
}
```

**Ventajas:**

- Thread-safe automáticamente (no requiere sincronización)
- Cacheable sin preocupaciones
- Seguro para usar como key en `HashMap`

**Desventajas:**

- Si necesitas modificar: crear nuevo objeto (overhead)

#### Records en Java 16+ (Inmutabilidad automática)

```java
public record Order(
    String orderId,
    OrderType type,
    double price,
    int quantity,
    Instant timestamp
) implements Comparable<Order> {
    // Constructor compacto (validación)
    public Order {
        Objects.requireNonNull(type);
        if (price <= 0) throw new IllegalArgumentException();
    }

    @Override
    public int compareTo(Order other) {
        return Double.compare(this.price, other.price);
    }
}
```

`record` = `final class` con campos `final`, `equals`, `hashCode`, `toString` automáticos.

---

## PARTE 6: APLICACIÓN AL PROYECTO

### 6.1 Implementar `OrderBook` Thread-Safe

#### Estrategia 1: Synchronized (Simple)

```java
public class OrderBook {
    private final String symbol;
    private final PriorityQueue<Order> bids;    // NO thread-safe
    private final PriorityQueue<Order> asks;
    private final Object lock = new Object();   // Lock compartido

    public OrderBook(String symbol) {
        this.symbol = symbol;
        this.bids = new PriorityQueue<>();
        this.asks = new PriorityQueue<>();
    }

    public void addOrder(Order order) {
        synchronized(lock) {
            if (order.getType() == OrderType.BUY) {
                bids.add(order);
            } else {
                asks.add(order);
            }
        }
    }

    public int getBidCount() {
        synchronized(lock) {
            return bids.size();
        }
    }

    public List<Order> getBids() {
        synchronized(lock) {
            return new ArrayList<>(bids);  // Copia defensiva
        }
    }
}
```

**Pros:** Simple, difícil de equivocarse.
**Contras:** Lecturas y escrituras compiten por el mismo lock (menor concurrencia).

#### Estrategia 2: ReentrantReadWriteLock (Optimizado)

```java
public class OrderBook {
    private final PriorityQueue<Order> bids = new PriorityQueue<>();
    private final PriorityQueue<Order> asks = new PriorityQueue<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void addOrder(Order order) {
        rwLock.writeLock().lock();
        try {
            (order.getType() == OrderType.BUY ? bids : asks).add(order);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public List<Order> getBids() {
        rwLock.readLock().lock();
        try {
            return new ArrayList<>(bids);
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
```

**Pros:** Múltiples lectores simultáneos.
**Contras:** Más código, más complejidad.

#### Estrategia 3: PriorityBlockingQueue (Concurrente Nativo)

```java
public class OrderBook {
    private final PriorityBlockingQueue<Order> bids = new PriorityBlockingQueue<>();
    private final PriorityBlockingQueue<Order> asks = new PriorityBlockingQueue<>();

    public void addOrder(Order order) {
        (order.getType() == OrderType.BUY ? bids : asks).offer(order);
        // offer() es thread-safe internamente (usa locks + CAS)
    }

    public int getBidCount() {
        return bids.size();  // size() es weakly consistent
    }
}
```

**Pros:** Menos código, thread-safe garantizado.
**Contras:** `size()` puede ser inexacto (weakly consistent), consumo de memoria mayor.

### 6.2 Implementar `MatchingEngine`

```java
public class MatchingEngine {
    private final OrderBook orderBook;
    private final BlockingQueue<Order> orderQueue;
    private final List<Trade> trades;
    private final Thread processingThread;
    private volatile boolean running;

    public MatchingEngine(String symbol) {
        this.orderBook = new OrderBook(symbol);
        this.orderQueue = new LinkedBlockingQueue<>();
        this.trades = new CopyOnWriteArrayList<>();  // Thread-safe
        this.processingThread = new Thread(this::processOrders, "MatchingEngine");
        this.running = false;
    }

    public void start() {
        if (!running) {
            running = true;
            processingThread.start();
        }
    }

    public void shutdown() throws InterruptedException {
        running = false;
        processingThread.interrupt();
        processingThread.join(5000);
    }

    public void submitOrder(Order order) throws InterruptedException {
        orderQueue.put(order);
    }

    private void processOrders() {
        while (running || !orderQueue.isEmpty()) {
            try {
                Order order = orderQueue.poll(100, TimeUnit.MILLISECONDS);
                if (order != null) {
                    matchAndExecute(order);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void matchAndExecute(Order incomingOrder) {
        // TODO: Implementar lógica de matching
        // 1. Si es BUY, buscar en asks (SELL) con precio <= buyPrice
        // 2. Si es SELL, buscar en bids (BUY) con precio >= sellPrice
        // 3. Ejecutar trade, agregar a lista, remover órdenes matched
    }

    public List<Trade> getTrades() {
        return new ArrayList<>(trades);  // Copia defensiva
    }
}
```

---

## PARTE 7: DEBUGGING DE CONCURRENCIA

### 7.1 Herramientas en Arch Linux

#### Ver hilos en ejecución

```bash
# Ejecutar tests
mvn test -Dtest=ConcurrencyTest &
PID=$!

# Monitor de hilos en tiempo real
top -H -p $PID

# Thread dump completo
jstack $PID > threads.txt
```

#### Detectar deadlocks

```bash
# Thread dump muestra deadlocks automáticamente
jstack $PID | grep -A 30 "deadlock"
```

**Ejemplo de deadlock detectado:**

```bash
Found one Java-level deadlock:
=============================
"Thread-2":
  waiting to lock monitor 0x00007f8c4c001234 (object 0x0000000787654321, a OrderBook),
  which is held by "Thread-3"
"Thread-3":
  waiting to lock monitor 0x00007f8c4c005678 (object 0x0000000787abcdef, a OrderBook),
  which is held by "Thread-2"
```

### 7.2 Assertions para Thread-Safety

```java
// Verificar que NO estás en synchronized block
assert !Thread.holdsLock(this) : "No debe tener lock aquí";

// Verificar que SÍ estás en synchronized block
assert Thread.holdsLock(lock) : "Debe tener lock antes de modificar";
```

Ejecutar con `-ea` (enable assertions):

```bash
mvn test -DargLine="-ea"
```

---

## PARTE 8: ANTIPATRONES Y ERRORES COMUNES

### 8.1 Double-Checked Locking Roto

```java
// INCORRECTO (pre-Java 5)
public class Singleton {
    private static Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {         // Check 1 (sin lock)
            synchronized(Singleton.class) {
                if (instance == null) { // Check 2 (con lock)
                    instance = new Singleton();  // ← PROBLEMA
                }
            }
        }
        return instance;
    }
}
```

**Problema:** `new Singleton()` NO es atómico:

1. Allocar memoria
2. Llamar constructor
3. Asignar referencia

CPU puede reordenar → Otro thread ve `instance != null` pero el objeto no está inicializado.

**Solución (Java 5+):**

```java
private static volatile Singleton instance;  // volatile previene reordenamiento
```

**Mejor solución (Initialization-on-demand holder):**

```java
public class Singleton {
    private Singleton() {}

    private static class Holder {
        static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE;  // Thread-safe por garantías de classloader
    }
}
```

### 8.2 Locks en Diferente Orden (Deadlock)

```java
// Thread 1
synchronized(lockA) {
    synchronized(lockB) {
        // ...
    }
}

// Thread 2
synchronized(lockB) {  // ← Orden diferente → DEADLOCK
    synchronized(lockA) {
        // ...
    }
}
```

**Solución:** Ordenar locks consistentemente (ej: por `hashCode()` o por nombre).

### 8.3 ConcurrentModificationException

```java
List<Order> orders = new ArrayList<>();

// Thread 1: Itera
for (Order order : orders) {
    System.out.println(order);
}

// Thread 2: Modifica simultáneamente
orders.add(newOrder);  // → ConcurrentModificationException
```

**Soluciones:**

1. Usar `CopyOnWriteArrayList` (itera sobre snapshot)
2. Sincronizar iteración:

   ```java
   synchronized(orders) {
       for (Order order : orders) {
           System.out.println(order);
       }
   }
   ```

---

## PARTE 9: BENCHMARKING

### 9.1 JMH (Java Microbenchmark Harness)

```java
import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class OrderBookBenchmark {
    private OrderBook orderBook;

    @Setup
    public void setup() {
        orderBook = new OrderBook("AAPL");
    }

    @Benchmark
    @Threads(10)
    public void benchmarkConcurrentAdds() {
        orderBook.addOrder(new Order("AAPL", OrderType.BUY, 150.0, 1));
    }
}
```

```bash
mvn jmh:run
```

### 9.2 Medir Throughput de Tests

```java
long start = System.nanoTime();
// ... ejecutar operaciones ...
long end = System.nanoTime();
double throughput = (numOperations / (end - start)) * 1_000_000_000;
System.out.printf("Throughput: %.2f ops/seg\n", throughput);
```

---

## RESUMEN: CHECKLIST DE THREAD-SAFETY

Al implementar una clase concurrente, verifica:

- [ ] Identificar estado compartido (variables de instancia accesibles por múltiples hilos)
- [ ] Hacer objetos inmutables donde sea posible (`final` fields, no setters)
- [ ] Sincronizar acceso a estado mutable (`synchronized`, `Lock`, estructuras concurrentes)
- [ ] Evitar publicar referencias a objetos internos (copia defensiva en getters)
- [ ] Usar `volatile` para flags booleanos de control
- [ ] Evitar locks anidados (riesgo de deadlock) o adquirir en orden consistente
- [ ] Usar `AtomicInteger`/`AtomicReference` para contadores/referencias simples
- [ ] Probar con tests de stress (como `ConcurrencyTest.java`)
- [ ] Documentar invariantes de concurrencia (qué lock protege qué dato)

---

## REFERENCIAS

- **Java Concurrency in Practice** (Brian Goetz) - La biblia de concurrencia en Java
- **JVM Specification: Chapter 17 - Threads and Locks** - Especificación oficial del JMM
- **Arch Wiki: Java** - `https://wiki.archlinux.org/title/Java`
- **OpenJDK Source Code** - `src/java.base/share/classes/java/util/concurrent/`

---

**¡Ahora implementa `OrderBook` y `MatchingEngine` aplicando estos conceptos!**
