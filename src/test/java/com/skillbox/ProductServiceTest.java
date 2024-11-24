package com.skillbox;

import com.skillbox.shop.Product;
import com.skillbox.shop.ProductNotFoundException;
import com.skillbox.shop.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    void testAddProduct() {
        Product product = new Product("1", "Product 1", 10.0);
        productService.addProduct(product);

        assertEquals(1, productService.getProducts().size());
        assertEquals(product, productService.getProducts().get(0));
    }

    @Test
    void testRemoveProduct() {
        Product product = new Product("1", "Product 1", 10.0);
        productService.addProduct(product);

        productService.removeProduct(product.getId());

        assertEquals(0, productService.getProducts().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void testFindProductById(String id) {
        Product product = new Product(id, "Product " + id, 10.0);
        productService.addProduct(product);

        Product foundProduct = productService.findProductById(id);

        assertEquals(product, foundProduct);
    }

    @ParameterizedTest
    @CsvSource({"1, Product 1, 10.0", "2, Product 2, 20.0", "3, Product 3, 30.0"})
    void testAddProductCsv(String id, String name, double price) {
        Product product = new Product(id, name, price);
        productService.addProduct(product);

        assertEquals(1, productService.getProducts().size());
        assertEquals(product, productService.getProducts().get(0));
    }

    @ParameterizedTest
    @MethodSource("provideProducts")
    void testAddProductMethodSource(Product product) {
        productService.addProduct(product);

        assertEquals(1, productService.getProducts().size());
        assertEquals(product, productService.getProducts().get(0));
    }

    private static Stream<Product> provideProducts() {
        return Stream.of(
                new Product("1", "Product 1", 10.0),
                new Product("2", "Product 2", 20.0),
                new Product("3", "Product 3", 30.0)
        );
    }

    @Test
    void testFindProductByIdNotFound() {
        assertThrows(ProductNotFoundException.class, () -> productService.findProductById("nonexistent"));
    }

    @Test
    void testGetProducts() {
        Product product1 = new Product("1", "Product 1", 10.0);
        Product product2 = new Product("2", "Product 2", 20.0);
        productService.addProduct(product1);
        productService.addProduct(product2);

        List<Product> products = productService.getProducts();

        assertEquals(2, products.size());
        assertEquals(product1, products.get(0));
        assertEquals(product2, products.get(1));
    }

    @Test
    void testGetProductsEmpty() {
        List<Product> products = productService.getProducts();

        assertEquals(0, products.size());
    }
}