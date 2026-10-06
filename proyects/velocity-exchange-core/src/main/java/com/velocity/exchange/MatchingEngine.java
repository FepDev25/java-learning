package com.velocity.exchange;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * STUB: Motor de emparejamiento de órdenes.
 *
 * TAREA DEL ESTUDIANTE:
 * Implementar el patrón Producer-Consumer usando:
 * - BlockingQueue para recibir órdenes
 * - Un hilo dedicado que procesa órdenes y ejecuta matches
 * - Sincronización apropiada con el OrderBook
 *
 * REQUERIMIENTOS:
 * 1. Debe procesar órdenes en orden FIFO
 * 2. Al recibir una orden, intentar emparejarla con el lado opuesto
 * 3. Generar objetos Trade cuando hay match
 * 4. Soportar inicio/parada limpia del engine (shutdown graceful)
 */
public class MatchingEngine {
    private final OrderBook orderBook;

    public MatchingEngine(String symbol) {
        this.orderBook = new OrderBook(symbol);
        // TODO: Inicializar BlockingQueue y thread de procesamiento
    }

    /**
     * Inicia el motor de procesamiento (arranca el hilo consumidor).
     */
    public void start() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Detiene el motor de forma ordenada.
     */
    public void shutdown() throws InterruptedException {
        // TODO: Implementar shutdown graceful
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Envía una orden al motor para procesamiento asíncrono.
     * Múltiples hilos (brokers) pueden llamar esto simultáneamente.
     */
    public void submitOrder(Order order) throws InterruptedException {
        // TODO: Agregar a la cola de procesamiento
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Retorna todas las transacciones ejecutadas.
     */
    public List<Trade> getTrades() {
        // TODO: Implementar con thread-safety
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Retorna el volumen total transaccionado.
     */
    public int getTotalVolume() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
}
