package com.skillbox.shop;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final List<Product> products;

    public ProductService() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(String productId) {
        Product product = findProductById(productId);
        products.remove(product);
    }

    public Product findProductById(String id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}
