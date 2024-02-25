package com.store.watch.dto;

public record Watch(String id, String name, int unitPrice, int discountQuantity, int discountPrice) {
    public Watch(String id, String name, int unitPrice) {
        this(id, name, unitPrice, 0, 0);
    }
}
