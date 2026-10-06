package com.velocity.exchange;

import java.time.Instant;
import java.util.Objects;

/**
 * Representa una transacción ejecutada (match entre una orden de compra y venta).
 * INMUTABLE.
 */
public final class Trade {
    private final String buyOrderId;
    private final String sellOrderId;
    private final String symbol;
    private final double price;
    private final int quantity;
    private final Instant executedAt;

    public Trade(String buyOrderId, String sellOrderId, String symbol, double price, int quantity) {
        this.buyOrderId = Objects.requireNonNull(buyOrderId);
        this.sellOrderId = Objects.requireNonNull(sellOrderId);
        this.symbol = Objects.requireNonNull(symbol);
        this.price = price;
        this.quantity = quantity;
        this.executedAt = Instant.now();
    }

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    @Override
    public String toString() {
        return String.format("Trade[%s %d@%.2f buy=%s sell=%s]",
            symbol, quantity, price,
            buyOrderId.substring(0, 8),
            sellOrderId.substring(0, 8));
    }
}
