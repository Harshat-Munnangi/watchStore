package com.store.watch.dto;

public record Watch(String id, String name, int unitPrice, int discountQuantity, int discountPrice) {
    public Watch(String id, String name, int unitPrice) {
        this(id, name, unitPrice, 0, 0);
    }

    public int calculateCost(int quantity) {
        if (discountQuantity > 0) {
            int discountedSets = quantity / discountQuantity;
            int remainingWatches = quantity % discountQuantity;
            return discountedSets * discountPrice + remainingWatches * unitPrice;
        } else {
            return quantity * unitPrice;
        }
    }
}
