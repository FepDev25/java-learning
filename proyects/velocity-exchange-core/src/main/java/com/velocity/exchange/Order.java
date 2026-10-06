package com.velocity.exchange;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa una orden en el sistema de intercambio.
 * INMUTABLE para thread-safety por diseño.
 */
public final class Order implements Comparable<Order> {
    private final String orderId;
    private final String symbol;
    private final OrderType type;
    private final double price;
    private final int quantity;
    private final Instant timestamp;

    public Order(String symbol, OrderType type, double price, int quantity) {
        this.orderId = UUID.randomUUID().toString();
        this.symbol = Objects.requireNonNull(symbol, "Symbol cannot be null");
        this.type = Objects.requireNonNull(type, "OrderType cannot be null");
        this.price = price;
        this.quantity = quantity;
        this.timestamp = Instant.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public OrderType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Comparación para ordenamiento Price-Time Priority:
     * - BUY: Mayor precio primero, luego más antiguo
     * - SELL: Menor precio primero, luego más antiguo
     */
    @Override
    public int compareTo(Order other) {
        if (this.type != other.type) {
            throw new IllegalArgumentException("Cannot compare orders of different types");
        }

        int priceComparison;
        if (type == OrderType.BUY) {
            priceComparison = Double.compare(other.price, this.price); // DESC
        } else {
            priceComparison = Double.compare(this.price, other.price); // ASC
        }

        if (priceComparison != 0) {
            return priceComparison;
        }

        return this.timestamp.compareTo(other.timestamp); // Más antiguo primero
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return String.format("Order[%s %s %d@%.2f id=%s]",
            type, symbol, quantity, price, orderId.substring(0, 8));
    }
}
