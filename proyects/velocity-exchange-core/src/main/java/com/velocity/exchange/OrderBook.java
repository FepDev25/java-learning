package com.velocity.exchange;

import java.util.List;

/**
 * STUB: Libro de órdenes para un símbolo específico.
 *
 * TAREA DEL ESTUDIANTE:
 * Implementar esta clase de forma THREAD-SAFE usando:
 * - synchronized blocks
 * - ReentrantLock
 * - Estructuras concurrentes (ConcurrentHashMap, PriorityBlockingQueue, etc.)
 *
 * REQUERIMIENTOS:
 * 1. Mantener órdenes BUY ordenadas por precio DESC, tiempo ASC
 * 2. Mantener órdenes SELL ordenadas por precio ASC, tiempo ASC
 * 3. Soportar múltiples hilos llamando addOrder() simultáneamente
 * 4. Retornar estadísticas consistentes sin race conditions
 */
public class OrderBook {
    private final String symbol;

    public OrderBook(String symbol) {
        this.symbol = symbol;
        // TODO: Inicializar estructuras de datos thread-safe
    }

    /**
     * Agrega una orden al libro.
     * Debe ser thread-safe: múltiples hilos pueden llamar esto simultáneamente.
     */
    public void addOrder(Order order) {
        // TODO: Implementar con sincronización apropiada
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Retorna el número total de órdenes BUY activas.
     */
    public int getBidCount() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Retorna el número total de órdenes SELL activas.
     */
    public int getAskCount() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Retorna el mejor precio de compra (más alto).
     */
    public Double getBestBid() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Retorna el mejor precio de venta (más bajo).
     */
    public Double getBestAsk() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Retorna todas las órdenes BUY (copia defensiva).
     */
    public List<Order> getBids() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    /**
     * Retorna todas las órdenes SELL (copia defensiva).
     */
    public List<Order> getAsks() {
        // TODO: Implementar
        throw new UnsupportedOperationException("Implementación pendiente");
    }

    public String getSymbol() {
        return symbol;
    }
}
