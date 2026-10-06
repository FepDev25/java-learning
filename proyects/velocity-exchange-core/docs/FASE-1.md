# FASE 1: EL LIBRO DE ÓRDENES THREAD-SAFE

## 1. The Blueprint (Diseño de Clases)

### Arquitectura de Dominio

El sistema **Velocity Exchange Core** está estructurado en capas siguiendo principios de diseño de sistemas de bajo nivel:

```bash
┌─────────────────────────────────────────┐
│         MatchingEngine                  │  ← Producer-Consumer Pattern
│  (Thread Dedicado + BlockingQueue)     │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│           OrderBook                      │  ← Thread-Safe Data Structure
│  (Bids PriorityQueue + Asks PQueue)     │
└─────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│    Order (Immutable) + Trade (POJO)     │  ← Domain Models
└─────────────────────────────────────────┘
```

### Clases Implementadas (Ya disponibles en `src/main/java/com/velocity/exchange/`)

#### 1.1 `Order.java` - La Orden Inmutable

**Características clave:**

- **Inmutabilidad Total:** `final` en todos los campos. No hay setters.
- **Thread-Safe por Diseño:** Objetos inmutables son inherentemente seguros para compartir entre hilos.
- **Comparable:** Implementa Price-Time Priority:
  - BUY: Mayor precio primero (DESC), luego FIFO
  - SELL: Menor precio primero (ASC), luego FIFO
- **UUID único:** Cada orden tiene un ID generado automáticamente.

**Ejemplo de uso:**

```java
Order buyOrder = new Order("AAPL", OrderType.BUY, 150.50, 100);
// buyOrder es inmutable, puede compartirse entre hilos sin sincronización
```

#### 1.2 `Trade.java` - El Registro de Transacción

Representa un match exitoso entre una orden de compra y venta.

- Inmutable
- Timestamp de ejecución (`Instant.now()`)
- Referencia a las órdenes padre (buyOrderId, sellOrderId)

#### 1.3 `OrderBook.java` - EL DESAFÍO PRINCIPAL

**Estado Actual:** STUB - Solo estructura, sin implementación.

**Tu misión:** Implementar esta clase de forma **THREAD-SAFE**.

**Requerimientos técnicos:**

1. **Estructuras de Datos:** Necesitas dos colas de prioridad (una para BUY, otra para SELL):

   **Opción A - Sincronización Manual:**

   ```java
   private final PriorityQueue<Order> bids;    // Requiere synchronized
   private final PriorityQueue<Order> asks;
   private final Object lock = new Object();   // Lock explícito
   ```

   **Opción B - Estructuras Concurrentes (Recomendado para aprender):**

   ```java
   private final PriorityBlockingQueue<Order> bids;
   private final PriorityBlockingQueue<Order> asks;
   // PriorityBlockingQueue es thread-safe internamente
   ```

   **Opción C - ReentrantLock (Nivel Avanzado):**

   ```java
   private final PriorityQueue<Order> bids;
   private final PriorityQueue<Order> asks;
   private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
   // Permite múltiples lectores simultáneos, un solo escritor
   ```

2. **Métodos Críticos:**
   - `addOrder(Order)`: Debe ser atómico. Si 1000 hilos llaman esto simultáneamente, las 1000 órdenes deben agregarse.
   - `getBestBid()` / `getBestAsk()`: Deben retornar valores consistentes incluso durante escrituras concurrentes.
   - `getBids()` / `getAsks()`: CUIDADO - retornar la referencia interna causa **aliasing**. Usa copia defensiva:

     ```java
     return new ArrayList<>(bids); // Copia snapshot
     ```

#### 1.4 `MatchingEngine.java` - El Patrón Producer-Consumer

**Estado Actual:** STUB completo.

**Patrón de diseño:**

```bash
[Broker Thread 1] ──┐
[Broker Thread 2] ──┼──> BlockingQueue ──> [Engine Thread] ──> Genera Trades
[Broker Thread N] ──┘
```

**Componentes necesarios:**

1. **Cola de Tareas:**

   ```java
   private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
   ```

2. **Hilo de Procesamiento:**

   ```java
   private Thread processingThread;
   ```

3. **Control de Ciclo de Vida:**

   ```java
   private volatile boolean running = false;  // volatile para visibilidad
   ```

**Algoritmo de Matching Básico (para cuando implementes):**

```pseudocode
MIENTRAS running:
    order = orderQueue.take()  // Bloquea si está vacía

    SI order.type == BUY:
        MIENTRAS haya asks Y bestAsk <= order.price:
            askOrder = asks.poll()
            trade = ejecutarMatch(order, askOrder)
            trades.add(trade)
    SINO:
        MIENTRAS haya bids Y bestBid >= order.price:
            bidOrder = bids.poll()
            trade = ejecutarMatch(bidOrder, order)
            trades.add(trade)
```

---

## 2. The Setup (Arch JVM)

### 2.1 Verificar Instalación de Java

```bash
# Verificar versión (debe ser 21+)
java -version

# Si no tienes Java 21, instalar en Arch:
sudo pacman -S jdk21-openjdk

# Configurar JAVA_HOME (agregar a ~/.bashrc)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

### 2.2 Estructura del Proyecto

```bash
cd ~/Learning/Udemy_Master_Java/proyects/velocity-exchange-core

# Estructura Maven:
velocity-exchange-core/
├── pom.xml
├── src/
│   ├── main/java/com/velocity/exchange/
│   │   ├── Order.java
│   │   ├── OrderBook.java         ← IMPLEMENTAR
│   │   ├── MatchingEngine.java    ← IMPLEMENTAR
│   │   ├── Trade.java
│   │   └── OrderType.java
│   └── test/java/com/velocity/exchange/
│       └── ConcurrencyTest.java   ← Tests listos
└── TUTORIAL.md
```

### 2.3 Compilar y Ejecutar Tests

#### Opción 1 - Usando Maven (Recomendado)

```bash
# Compilar todo el proyecto
mvn clean compile

# Ejecutar SOLO los tests de concurrencia
mvn test -Dtest=ConcurrencyTest

# Ejecutar con output verbose
mvn test -Dtest=ConcurrencyTest -Dsurefire.printSummary=true
```

#### Opción 2 - Manual con javac (Sin Maven)

```bash
# 1. Compilar clases de producción
javac -d target/classes \
  src/main/java/com/velocity/exchange/*.java

# 2. Descargar JUnit 5 manualmente (si no usas Maven)
# Ver: https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher

# 3. Compilar tests
javac -cp target/classes:junit-platform-console-standalone.jar \
  -d target/test-classes \
  src/test/java/com/velocity/exchange/*.java

# 4. Ejecutar tests
java -jar junit-platform-console-standalone.jar \
  --class-path target/test-classes:target/classes \
  --scan-classpath
```

### 2.4 Flags de la JVM para Debugging de Concurrencia

```bash
# Activar assertions (útil para debug)
mvn test -DargLine="-ea"

# Habilitar advertencias de sincronización (warnings de locks)
mvn test -DargLine="-XX:+PrintGCDetails -XX:+PrintConcurrentLocks"

# Reducir tamaño de heap para forzar stress (testing extremo)
mvn test -DargLine="-Xmx256m -Xms256m"
```

---

## 3. The Challenge (TDD Reverse - Stress Test)

### 3.1 El Manifiesto del Test

El archivo `src/test/java/com/velocity/exchange/ConcurrencyTest.java` contiene **5 tests asesinos** diseñados para exponer race conditions:

#### Test #1: `shouldHandleConcurrentOrdersSafely()`

**Objetivo:** Demostrar **Lost Updates**.

- Lanza 1000 hilos (500 BUY, 500 SELL)
- Todos agregan órdenes simultáneamente al `OrderBook`
- **Fallo esperado sin sincronización:**

  ```bash
  Expected: 500
  Actual: 487  ← Órdenes perdidas
  ```

**Por qué falla:**

```java
// Si addOrder() está implementado así (INCORRECTO):
public void addOrder(Order order) {
    if (order.getType() == OrderType.BUY) {
        bids.add(order);  // PriorityQueue NO es thread-safe
    }
}
// Múltiples hilos pueden leer/escribir el array interno al mismo tiempo
// → ArrayIndexOutOfBoundsException o pérdida de datos
```

#### Test #2: `shouldReturnConsistentBestPrices()`

**Objetivo:** Demostrar **Visibility Problems**.

- 50 escritores agregan órdenes
- 50 lectores consultan `getBestBid()` y `getBestAsk()` simultáneamente
- **Fallo esperado:** `bestBid > bestAsk` (IMPOSIBLE en un mercado real)

**Por qué falla:**

```java
// Sin sincronización, un lector puede ver:
// - bestBid actualizado (150.0)
// - bestAsk desactualizado (cache) (140.0)
// → Inconsistencia temporal
```

#### Test #3: `shouldProcessOrdersWithoutLoss()`

**Objetivo:** Validar el patrón **Producer-Consumer**.

- 100 brokers envían 10 órdenes cada uno (1000 total)
- El `MatchingEngine` debe procesar todas sin pérdidas
- **Fallo esperado:**

  ```bash
  Expected: 1000
  Actual: 1000 (en OrderBook + Trades)
  ```

  Si el resultado es < 1000, hay pérdidas en la cola o el procesamiento.

#### Test #4: `demonstrateLostUpdates()`

**Objetivo educativo:** Mostrar visualmente el problema de lost updates con un contador simple.

- **Este test está diseñado para FALLAR intencionalmente.**
- Ejecuta 100,000 incrementos concurrentes sin sincronización
- Output esperado:

  ```bash
  Esperado: 100000
  Obtenido: 94783  ← Demuestra el problema
  Pérdidas: 5217
  ```

#### Test #5: `shouldNotDeadlockWithMultipleBooks()`

**Objetivo:** Detectar **Deadlocks** potenciales.

- Crea 2 OrderBooks (`AAPL`, `GOOGL`)
- La mitad de hilos accede en orden: `book1` → `book2`
- La otra mitad en orden inverso: `book2` → `book1`
- **Fallo esperado:** El test se cuelga (timeout) si hay deadlock

**Cómo evitar deadlock:**

- Adquirir locks en orden consistente (ej: siempre por símbolo alfabético)
- Usar `tryLock()` con timeout en lugar de `lock()` bloqueante

### 3.2 Ejecutar los Tests

```bash
# Primera ejecución (debe fallar todo)
mvn test -Dtest=ConcurrencyTest

# Resultado esperado:
# [ERROR] Tests run: 5, Failures: 4, Errors: 1, Skipped: 0
```

### 3.3 Estrategia de Implementación

**NO implementes todo de golpe.** Sigue este orden:

1. **Implementa `OrderBook.addOrder()` SIN sincronización** → Ver el test #1 fallar
2. **Agrega `synchronized` a `addOrder()`** → Ver el test #1 pasar
3. **Implementa `getBestBid/Ask()` sin sincronización** → Ver test #2 fallar
4. **Sincroniza lecturas** → Ver test #2 pasar
5. **Implementa `MatchingEngine`** → Ver test #3 funcionar
6. **Reflexiona sobre test #4** → Entiende el problema de fondo
7. **Prueba test #5** → Confirma que no hay deadlocks

---

## 4. TUTORIAL.md (La Teoría de Hilos)

**Ver archivo `TUTORIAL.md` en la raíz del proyecto.**

Este archivo contiene teoría profunda sobre:

- Race Conditions a nivel de CPU y memoria
- Java Memory Model (JMM)
- Sincronización: `synchronized`, `volatile`, `ReentrantLock`, `AtomicInteger`
- Patrones de concurrencia: Producer-Consumer, Read-Write Locks

**Lectura obligatoria antes de implementar.**

---

## 5. Bonus Arch Linux (Profiling)

### 5.1 Monitorear Hilos en Tiempo Real

#### Paso 1: Ejecutar los tests en background

```bash
# Terminal 1: Ejecutar tests con delay (para tener tiempo de inspeccionar)
mvn test -Dtest=ConcurrencyTest &

# Obtener el PID del proceso Java
PID=$(pgrep -f ConcurrencyTest)
echo "PID: $PID"
```

#### Paso 2: Ver hilos con `top`

```bash
# Terminal 2: Modo hilos (muestra cada thread como una línea)
top -H -p $PID

# Columnas importantes:
# - PID: Thread ID (cada hilo tiene su propio TID)
# - %CPU: Uso de CPU por hilo
# - COMMAND: Nombre del hilo (puede ser "java" o nombres custom)
```

**Output esperado:**

```bash
  PID USER      PR  NI    VIRT    RES  %CPU %MEM     TIME+ COMMAND
15234 user      20   0 4234212 245612  25.3  1.5   0:02.45 java
15235 user      20   0 4234212 245612  24.1  1.5   0:02.38 java
15236 user      20   0 4234212 245612  23.8  1.5   0:02.41 java
...
15334 user      20   0 4234212 245612   0.0  1.5   0:00.01 java  ← Idle
```

### 5.2 Generar un Thread Dump (Análisis Forense)

```bash
# Enviar señal QUIT al proceso (NO lo mata, solo imprime stack traces)
kill -3 $PID

# El thread dump se imprime en STDOUT del proceso original
# Si ejecutaste con Maven, revisa:
cat target/surefire-reports/*.txt
```

**Ejemplo de Thread Dump:**

```bash
"pool-1-thread-23" #35 prio=5 os_prio=0 tid=0x00007f8c4c123456 nid=0x3c42 waiting for monitor entry
   java.lang.Thread.State: BLOCKED (on object monitor)
        at com.velocity.exchange.OrderBook.addOrder(OrderBook.java:45)
        - waiting to lock <0x00000006c1234567> (a java.lang.Object)
        at com.velocity.exchange.ConcurrencyTest.lambda$shouldHandleConcurrentOrdersSafely$1

"pool-1-thread-47" #59 prio=5 os_prio=0 tid=0x00007f8c4c789abc nid=0x3c56 runnable
   java.lang.Thread.State: RUNNABLE
        at com.velocity.exchange.OrderBook.addOrder(OrderBook.java:46)
        - locked <0x00000006c1234567> (a java.lang.Object)
```

**Interpretación:**

- `BLOCKED`: El hilo está esperando un lock (monitor)
- `RUNNABLE`: El hilo está ejecutando código
- `WAITING`: El hilo está en `wait()` o `park()`
- `locked <0x...>`: El hilo tiene el lock de ese objeto

### 5.3 Usar `jstack` (Herramienta JDK)

```bash
# Generar thread dump con jstack (más detallado)
jstack $PID > thread_dump.txt

# Ver solo hilos bloqueados
jstack $PID | grep -A 10 "BLOCKED"

# Detectar deadlocks automáticamente
jstack $PID | grep -A 20 "deadlock"
```

### 5.4 Profiling con VisualVM (GUI)

```bash
# Instalar VisualVM en Arch
yay -S visualvm

# Ejecutar tests y abrir VisualVM
mvn test -Dtest=ConcurrencyTest &
visualvm
```

**En VisualVM:**

1. Seleccionar el proceso Java en el panel izquierdo
2. Ir a la pestaña "Threads"
3. Ver timeline de cada hilo (verde=running, rojo=blocked, naranja=waiting)
4. Tomar snapshots para análisis offline

### 5.5 Flags JVM para Debugging Avanzado

```bash
# Detectar race conditions con ThreadSanitizer (experimental)
mvn test -DargLine="-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints"

# Habilitar logging de sincronización
mvn test -DargLine="-XX:+PrintConcurrentLocks -XX:+PrintSafepointStatistics"

# Ver garbage collection (ayuda a entender pausas)
mvn test -DargLine="-XX:+PrintGCDetails -Xloggc:gc.log"
```

---

## Próximos Pasos

1. **Lee `TUTORIAL.md` completo** (teoría de concurrencia)
2. **Ejecuta `mvn test` y observa los fallos**
3. **Implementa `OrderBook` sin sincronización** → Confirma el caos
4. **Agrega sincronización progresivamente** → Observa cómo se arreglan los tests
5. **Implementa `MatchingEngine`** (patrón Producer-Consumer)
6. **Experimenta con profiling** en Arch Linux

**Recuerda:** El objetivo es **aprender viendo los errores primero**. No busques la solución perfecta de inmediato. Rompe cosas, observa, entiende, repara.

---

**¿Preguntas?** Revisa `TUTORIAL.md` o experimenta modificando los tests para explorar diferentes escenarios de concurrencia.
