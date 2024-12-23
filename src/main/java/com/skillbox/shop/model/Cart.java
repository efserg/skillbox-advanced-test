package com.skillbox.shop.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private final Map<String, CartItem> cartItems;

    public Cart() {
        cartItems = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        CartItem item = cartItems.get(product.getId());
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            cartItems.put(product.getId(), new CartItem(product, quantity));
        }
    }

    public void removeProduct(String productId) {
        cartItems.remove(productId);
    }

    public double calculateTotal() {
        return cartItems.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public Map<String, CartItem> getCartItems() {
        return Map.copyOf(cartItems);
    }
}
