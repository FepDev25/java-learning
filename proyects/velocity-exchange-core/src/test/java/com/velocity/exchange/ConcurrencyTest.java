package com.velocity.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TESTS ASESINOS: Diseñados para exponer Race Conditions.
 *
 * Si implementas OrderBook sin sincronización, estos tests DEBEN FALLAR.
 * Si ves NullPointerException, ArrayIndexOutOfBounds, o números incorrectos: ¡PERFECTO!
 * Ese es el comportamiento esperado antes de implementar thread-safety.
 */
class ConcurrencyTest {

    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        orderBook = new OrderBook("AAPL");
    }

    /**
     * TEST ASESINO #1: El Estrés Básico
     *
     * 1000 hilos agregan órdenes simultáneamente al OrderBook.
     * Sin sincronización correcta, verás:
     * - Pérdida de órdenes (lost updates)
     * - Contadores inconsistentes
     * - Posibles excepciones (ConcurrentModificationException)
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    @DisplayName("1000 threads adding orders concurrently - debe ser thread-safe")
    void shouldHandleConcurrentOrdersSafely() throws InterruptedException {
        final int NUM_THREADS = 1000;
        final int BUY_THREADS = 500;
        final int SELL_THREADS = 500;

        ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(NUM_THREADS);

        AtomicInteger buyOrdersSubmitted = new AtomicInteger(0);
        AtomicInteger sellOrdersSubmitted = new AtomicInteger(0);

        // 500 hilos comprando
        for (int i = 0; i < BUY_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Todos empiezan al mismo tiempo
                    Order buyOrder = new Order("AAPL", OrderType.BUY, 150.0 + threadId, 1);
                    orderBook.addOrder(buyOrder);
                    buyOrdersSubmitted.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("ERROR en BUY thread " + threadId + ": " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 500 hilos vendiendo
        for (int i = 0; i < SELL_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    Order sellOrder = new Order("AAPL", OrderType.SELL, 150.0 + threadId, 1);
                    orderBook.addOrder(sellOrder);
                    sellOrdersSubmitted.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("ERROR en SELL thread " + threadId + ": " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // ¡EMPEZAR LA GUERRA!
        startLatch.countDown();

        // Esperar que terminen todos
        boolean finished = endLatch.await(20, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertTrue(finished, "Todos los hilos deben terminar en tiempo razonable");

        // ASERCIONES CRÍTICAS - Si fallan, hay race conditions
        assertEquals(BUY_THREADS, buyOrdersSubmitted.get(),
            "Todos los hilos BUY deben haber ejecutado");
        assertEquals(SELL_THREADS, sellOrdersSubmitted.get(),
            "Todos los hilos SELL deben haber ejecutado");

        // LA PRUEBA DE FUEGO: ¿El OrderBook tiene EXACTAMENTE las órdenes correctas?
        assertEquals(BUY_THREADS, orderBook.getBidCount(),
            "El OrderBook debe tener exactamente 500 órdenes BUY (sin pérdidas)");
        assertEquals(SELL_THREADS, orderBook.getAskCount(),
            "El OrderBook debe tener exactamente 500 órdenes SELL (sin pérdidas)");

        // Verificar integridad del ordenamiento
        List<Order> bids = orderBook.getBids();
        List<Order> asks = orderBook.getAsks();

        assertNotNull(bids, "getBids() no debe retornar null");
        assertNotNull(asks, "getAsks() no debe retornar null");

        assertEquals(BUY_THREADS, bids.size(), "Lista de bids debe tener tamaño correcto");
        assertEquals(SELL_THREADS, asks.size(), "Lista de asks debe tener tamaño correcto");
    }

    /**
     * TEST ASESINO #2: El Doble Check
     *
     * Verifica que getBestBid() y getBestAsk() retornen valores consistentes
     * incluso cuando otros hilos están modificando el OrderBook.
     */
    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @DisplayName("Best bid/ask queries durante escrituras concurrentes")
    void shouldReturnConsistentBestPrices() throws InterruptedException {
        final int NUM_WRITERS = 50;
        final int NUM_READERS = 50;
        final int OPERATIONS_PER_THREAD = 100;

        ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(NUM_WRITERS + NUM_READERS);

        Random random = new Random(42);

        // Escritores: agregan órdenes constantemente
        for (int i = 0; i < NUM_WRITERS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    Random localRandom = new Random(threadId);
                    for (int op = 0; op < OPERATIONS_PER_THREAD; op++) {
                        OrderType type = localRandom.nextBoolean() ? OrderType.BUY : OrderType.SELL;
                        double price = 100.0 + localRandom.nextDouble() * 100.0;
                        orderBook.addOrder(new Order("AAPL", type, price, 1));
                    }
                } catch (Exception e) {
                    System.err.println("ERROR en writer " + threadId + ": " + e.getMessage());
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // Lectores: consultan best prices constantemente
        AtomicInteger inconsistencyCount = new AtomicInteger(0);
        for (int i = 0; i < NUM_READERS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int op = 0; op < OPERATIONS_PER_THREAD; op++) {
                        Double bestBid = orderBook.getBestBid();
                        Double bestAsk = orderBook.getBestAsk();

                        // Si ambos existen, bid debe ser <= ask
                        if (bestBid != null && bestAsk != null) {
                            if (bestBid > bestAsk) {
                                inconsistencyCount.incrementAndGet();
                                System.err.println("INCONSISTENCIA: bestBid=" + bestBid + " > bestAsk=" + bestAsk);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("ERROR en reader " + threadId + ": " + e.getMessage());
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        endLatch.await(15, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(0, inconsistencyCount.get(),
            "NO debe haber inconsistencias donde bestBid > bestAsk");
    }

    /**
     * TEST ASESINO #3: El Producer-Consumer Extremo
     *
     * Simula el sistema real: múltiples brokers enviando órdenes y un engine procesándolas.
     * Verifica que NO se pierdan órdenes y que el volumen transaccionado sea correcto.
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    @DisplayName("MatchingEngine debe procesar órdenes sin pérdidas")
    void shouldProcessOrdersWithoutLoss() throws InterruptedException {
        MatchingEngine engine = new MatchingEngine("AAPL");
        engine.start();

        final int NUM_BROKERS = 100;
        final int ORDERS_PER_BROKER = 10;
        final int TOTAL_ORDERS = NUM_BROKERS * ORDERS_PER_BROKER;

        ExecutorService brokers = Executors.newFixedThreadPool(NUM_BROKERS);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(NUM_BROKERS);

        AtomicInteger ordersSubmitted = new AtomicInteger(0);

        // Cada broker envía 10 órdenes
        for (int i = 0; i < NUM_BROKERS; i++) {
            final int brokerId = i;
            brokers.submit(() -> {
                try {
                    startLatch.await();
                    Random localRandom = new Random(brokerId);
                    for (int j = 0; j < ORDERS_PER_BROKER; j++) {
                        OrderType type = (brokerId % 2 == 0) ? OrderType.BUY : OrderType.SELL;
                        double price = 150.0 + localRandom.nextDouble() * 10.0;
                        Order order = new Order("AAPL", type, price, 1);
                        engine.submitOrder(order);
                        ordersSubmitted.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.err.println("ERROR en broker " + brokerId + ": " + e.getMessage());
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        endLatch.await(20, TimeUnit.SECONDS);
        brokers.shutdown();
        brokers.awaitTermination(5, TimeUnit.SECONDS);

        // Dar tiempo al engine para procesar
        Thread.sleep(2000);
        engine.shutdown();

        assertEquals(TOTAL_ORDERS, ordersSubmitted.get(),
            "Todos los brokers deben haber enviado sus órdenes");

        // Verificar que el engine procesó todas las órdenes
        OrderBook book = engine.getOrderBook();
        int totalInBook = book.getBidCount() + book.getAskCount();
        int totalTraded = engine.getTotalVolume();

        assertEquals(TOTAL_ORDERS, totalInBook + totalTraded,
            "Total de órdenes en book + transaccionadas debe igualar órdenes enviadas");
    }

    /**
     * TEST ASESINO #4: El Lost Update
     *
     * Simula el clásico problema de lost update:
     * Múltiples hilos incrementan un contador compartido.
     * Sin sincronización, el resultado final será MENOR que el esperado.
     */
    @Test
    @Timeout(value = 15, unit = TimeUnit.SECONDS)
    @DisplayName("Contador compartido sin sincronización - demuestra lost updates")
    void demonstrateLostUpdates() throws InterruptedException {
        // Este test usa un contador NO sincronizado para demostrar el problema
        class UnsafeCounter {
            private int count = 0;

            public void increment() {
                // Esta operación NO es atómica:
                // 1. Leer count
                // 2. Sumar 1
                // 3. Escribir count
                count++;
            }

            public int getCount() {
                return count;
            }
        }

        UnsafeCounter unsafeCounter = new UnsafeCounter();
        final int NUM_THREADS = 100;
        final int INCREMENTS_PER_THREAD = 1000;
        final int EXPECTED = NUM_THREADS * INCREMENTS_PER_THREAD;

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeCounter.increment();
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        int actualCount = unsafeCounter.getCount();

        System.out.println("\n=== DEMOSTRACIÓN DE LOST UPDATES ===");
        System.out.println("Esperado: " + EXPECTED);
        System.out.println("Obtenido: " + actualCount);
        System.out.println("Pérdidas: " + (EXPECTED - actualCount));
        System.out.println("====================================\n");

        // Este test está diseñado para FALLAR y mostrar el problema
        // Comentar esta línea después de ver el fallo
        assertTrue(actualCount < EXPECTED,
            "SIN sincronización, debe haber lost updates (actualCount < expected)");

        // Para arreglar: usar AtomicInteger o synchronized
    }

    /**
     * TEST ASESINO #5: El Deadlock Potential
     *
     * Simula un escenario donde múltiples hilos adquieren locks en diferente orden,
     * potencialmente causando deadlock.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("Múltiples OrderBooks - potencial deadlock si mal implementado")
    void shouldNotDeadlockWithMultipleBooks() throws InterruptedException {
        OrderBook book1 = new OrderBook("AAPL");
        OrderBook book2 = new OrderBook("GOOGL");

        final int NUM_THREADS = 50;
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        // La mitad de hilos: book1 -> book2
        for (int i = 0; i < NUM_THREADS / 2; i++) {
            executor.submit(() -> {
                try {
                    book1.addOrder(new Order("AAPL", OrderType.BUY, 150.0, 1));
                    book2.addOrder(new Order("GOOGL", OrderType.BUY, 2800.0, 1));
                } catch (Exception e) {
                    System.err.println("ERROR: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        // La otra mitad: book2 -> book1 (orden inverso)
        for (int i = 0; i < NUM_THREADS / 2; i++) {
            executor.submit(() -> {
                try {
                    book2.addOrder(new Order("GOOGL", OrderType.SELL, 2800.0, 1));
                    book1.addOrder(new Order("AAPL", OrderType.SELL, 150.0, 1));
                } catch (Exception e) {
                    System.err.println("ERROR: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean finished = latch.await(8, TimeUnit.SECONDS);
        executor.shutdownNow();

        assertTrue(finished, "NO debe haber deadlock (todos los hilos deben terminar)");
    }
}
