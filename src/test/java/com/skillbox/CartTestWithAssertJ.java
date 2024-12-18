package com.skillbox;

import com.skillbox.shop.model.Cart;
import com.skillbox.shop.model.CartItem;
import com.skillbox.shop.model.Product;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTestWithAssertJ {

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
    void testAddProduct1() {
        cart.addProduct(product1, 5);

        Map<String, CartItem> cartItems = cart.getCartItems();
        assertThat(cartItems)
                .hasSize(1)
                .containsKey(product1.getId());

        CartItem item = cartItems.get(product1.getId());
        assertThat(item)
                .isNotNull()
                .extracting(CartItem::getQuantity)
                .isEqualTo(5);
    }

    @Test
    void testRemoveProduct1() {
        cart.addProduct(product1, 5);
        cart.addProduct(product2, 3);
        cart.removeProduct(product1.getId());

        Map<String, CartItem> cartItems = cart.getCartItems();
        assertThat(cartItems)
                .hasSize(1)
                .doesNotContainKey(product1.getId())
                .containsKey(product2.getId());
    }

    @Test
    void testCalculateTotal1() {
        cart.addProduct(product1, 2);
        cart.addProduct(product2, 3);

        double total = cart.calculateTotal();
        assertThat(total)
                .isPositive()
                .isNotZero()
                .isGreaterThan(79)
                .isLessThan(81)
                .isEqualTo(80.0);
    }

    @Test
    void testEmptyCartCalculateZeroTotal1() {
        double total = cart.calculateTotal();
        assertThat(total).isZero();
    }

    @Test
    void testImmutableCartItems1() {
        cart.addProduct(product1, 2);

        Map<String, CartItem> cartItems = cart.getCartItems();
        assertThatThrownBy(cartItems::clear)
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testCartProductIds1() {
        cart.addProduct(product2, 1);
        cart.addProduct(product1, 2);

        Map<String, CartItem> actualCartItems = cart.getCartItems();

        assertThat(actualCartItems.values())
                .extracting(cartItem -> cartItem.getProduct().getName())
                .containsExactlyInAnyOrder("Widget", "Gadget");
    }
}
