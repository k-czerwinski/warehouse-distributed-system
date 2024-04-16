package com.bd2.warehousedistributedsystem.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private final Map<Product, Integer> products = new LinkedHashMap<>();
    public void addToCart(Product product, int quantity) {
        if (quantity <= 0) {
            return;
        }
        products.put(product, products.getOrDefault(product, 0) + quantity);
    }

    public Map<Product, Integer> getProductsInCart() {
        return products;
    }

    public void clearCart() {
        products.clear();
    }
}
