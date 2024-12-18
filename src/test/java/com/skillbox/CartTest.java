package com.skillbox;

import com.skillbox.shop.model.Cart;
import com.skillbox.shop.model.CartItem;
import com.skillbox.shop.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        product1 = new Product("1", "Widget", 10.0);
        product2 = new Product("2", "Gadget", 20.0);
    }

    @Test
    void testNegativeAmount()   {
        assertThrows(IllegalArgumentException.class, () -> cart.addProduct(product1, -1));
    }

    @Test
    void testAddProduct() {
        cart.addProduct(product1, 5);

        Map<String, CartItem> cartItems = cart.getCartItems();
        assertEquals(1, cartItems.size());
        assertTrue(cartItems.containsKey(product1.getId()));
    }

    @Test
    void testGetProduct() {
        cart.addProduct(product1, 5);
        Map<String, CartItem> cartItems = cart.getCartItems();
        CartItem item = cartItems.get(product1.getId());
        assertNotNull(item);
        assertEquals(50.0, item.getProduct().getPrice() * item.getQuantity(), "Should calculate product totals correctly");
    }


    @Test
    void testRemoveProduct() {
        cart.addProduct(product1, 5);
        cart.addProduct(product2, 3);

        cart.removeProduct(product1.getId());

        Map<String, CartItem> cartItems = cart.getCartItems();
        assertEquals(1, cartItems.size());
        assertFalse(cartItems.containsKey(product1.getId()));
    }

    @Test
    void testAddExistingProductIncreasesQuantity() {
        cart.addProduct(product1, 2);
        cart.addProduct(product1, 3);

        CartItem item = cart.getCartItems().get(product1.getId());
        assertNotNull(item);
        assertEquals(50.0, item.getProduct().getPrice() * item.getQuantity(), "Should calculate product totals correctly");
    }

    @Test
    void testEmptyCartCalculateZeroTotal() {
        double total = cart.calculateTotal();
        assertEquals(0.0, total, "Empty cart should have a total of zero");
    }

    @Test
    void testImmutableCartItems() {
        cart.addProduct(product1, 2);

        Map<String, CartItem> cartItems = cart.getCartItems();
        assertThrows(UnsupportedOperationException.class, cartItems::clear, "Cart items map should be immutable");
    }

    @Test
    void testCartProductIds() {
        cart.addProduct(product2, 1);
        cart.addProduct(product1, 2);

        List<String> expectedProductIds = List.of("1", "2");
        List<String> actualProductIds = cart.getCartItems()
                .values().stream()
                .map(item -> item.getProduct().getId())
                .sorted()
                .collect(Collectors.toList());

        assertIterableEquals(expectedProductIds, actualProductIds, "Product IDs should match the expected order");
    }

    @Test
    void testCalculateTotal() {
        cart.addProduct(product1, 2);
        cart.addProduct(product2, 3);

        double total = cart.calculateTotal();

        assertAll("cart items state",
                () -> assertNotEquals(0, total, "Total should not be zero"),
                () -> assertTrue(total > 0, "Total should be positive"),
                () -> assertEquals(80.0, total, 0.01, "Total amount should be 80.0")
        );
    }

}

