package com.skillbox;

import com.skillbox.shop.model.Cart;
import com.skillbox.shop.model.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class CartParametrizedTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testWithValueSource(int value) {
        assertThat(value).isGreaterThan(0);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Product 1, 10.0, 1",
            "2, Product 2, 20.0, 3",
            "3, Product 3, 15.0, 5"
    })
    void shouldAddProductWithCorrectQuantity(String productId, String productName, double price, int quantity) {
        // Arrange
        Cart cart = new Cart();
        Product product = new Product(productId, productName, price);

        // Act
        cart.addProduct(product, quantity);

        // Assert
        assertThat(cart.getCartItems())
                .hasSize(1)
                .containsKey(productId);

        assertThat(cart.getCartItems().get(productId).getQuantity())
                .isEqualTo(quantity);

        assertThat(cart.getCartItems().get(productId).getProduct().getName())
                .isEqualTo(productName);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/cart-test-data.csv", numLinesToSkip = 1)
    void shouldAddProductWithCorrectQuantityCsvFile(String productId, String productName, double price, int quantity) {
        // test implementation
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTotalCalculation")
    void shouldCalculateTotalCorrectly(List<Product> products, List<Integer> quantities, double expectedTotal) {
        // Arrange
        Cart cart = new Cart();
        for (int i = 0; i < products.size(); i++) {
            cart.addProduct(products.get(i), quantities.get(i));
        }

        // Act
        double total = cart.calculateTotal();

        // Assert
        assertThat(total).isEqualTo(expectedTotal);
    }

    // ник данных для теста
    private static Stream<Arguments> provideProductsForTotalCalculation() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Product("1", "Product 1", 10.0),
                                new Product("2", "Product 2", 20.0)
                        ),
                        List.of(1, 2), // Количества
                        50.0 // Ожидаемая итоговая сумма (10*1 + 20*2)
                ),
                Arguments.of(
                        List.of(
                                new Product("1", "Product 1", 15.0),
                                new Product("2", "Product 2", 25.0)
                        ),
                        List.of(3, 4), // Количества
                        145.0 // Ожидаемая итоговая сумма (15*3 + 25*4)
                ),
                Arguments.of(
                        List.of(
                                new Product("1", "Product 1", 5.0)
                        ),
                        List.of(10), // Количества
                        50.0 // Ожидаемая итоговая сумма (5*10)
                )
        );
    }

    @ParameterizedTest
    @ArgumentsSource(CustomArgumentsProvider.class)
    void testWithClassArgumentsSource(List<Product> products, List<Integer> quantities, double expectedTotal){
        // test implementation
    }

    static class CustomArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            // логика генерации данных
            return provideProductsForTotalCalculation();
        }
    }

    @ParameterizedTest
    @NullSource
    void shouldNotHandleNullProduct(Product product) {
        Cart cart = new Cart();
        assertThatThrownBy(() -> cart.addProduct(product, 1)).isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @EmptySource
    void shouldCreateProductWithEmptyName(String productName) {
        final Product product = new Product("1", productName, 10.0);
        assertThat(product.getName()).isEmpty();
    }

}