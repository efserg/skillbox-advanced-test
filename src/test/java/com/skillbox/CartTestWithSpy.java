package com.skillbox;

import com.skillbox.shop.model.Cart;
import com.skillbox.shop.model.CartItem;
import com.skillbox.shop.model.Product;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CartTestWithSpy {

    private Cart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        cart = spy(new Cart());
        product1 = new Product("1", "Widget", 10.0);
        product2 = new Product("2", "Gadget", 20.0);
    }

    @Test
    void testAddProductWithSpy() {
        cart.addProduct(product1, 5);
        cart.addProduct(product2, 3);

        // Проверяем, что метод addProduct был вызван дважды
        verify(cart, times(2)).addProduct(any(Product.class), anyInt());

        assertThat(cart.calculateTotal()).isEqualTo(110.0);
    }

    @Test
    void testCalculateTotalWithSpy() {
        cart.addProduct(product1, 1);
        doReturn(15.0).when(cart).calculateTotal();
//        when(cart.calculateTotal()).thenReturn(15.0);
        double total = cart.calculateTotal();
        assertThat(total).isEqualTo(15.0);

        verify(cart, times(1)).calculateTotal();

        assertThat(cart.getCartItems())
                .containsEntry(product1.getId(), new CartItem(product1, 1));
        CartItem cartItem = cart.getCartItems().get(product1.getId());
        assertThat(cartItem.getProduct().getPrice()).isEqualTo(10.0);
    }

    @Test
    void testRemoveProductWithSpy() {
        cart.addProduct(product1, 5);

        cart.removeProduct(product1.getId());

        // Проверяем, что метод removeProduct действительно удаляет товар из корзины
        verify(cart).removeProduct(product1.getId());
        assertThat(cart.getCartItems()).doesNotContainKey(product1.getId());
    }
}
