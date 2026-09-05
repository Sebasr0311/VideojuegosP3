package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;

import java.util.List;

/**
 * Service layer coordinating product management and inventory stock rules.
 */
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        if (productRepository == null) {
            throw new IllegalArgumentException("ProductRepository dependency cannot be null.");
        }
        this.productRepository = productRepository;
    }

    public VideoGame registerVideoGame(String id, String title, double price, int stock,
                                       String platform, String genre, String ageRating) {
        validateCommonProductFields(id, title, price, stock);
        String cleanId = id.trim();
        if (productRepository.findProductById(cleanId) != null) {
            throw new IllegalStateException("A product with ID '" + cleanId + "' already exists.");
        }
        VideoGame game = new VideoGame(cleanId, title.trim(), price, stock, platform, genre, ageRating);
        productRepository.saveProduct(game);
        return game;
    }

    public Console registerConsole(String id, String title, double price, int stock,
                                   String brand, String model, String generation) {
        validateCommonProductFields(id, title, price, stock);
        String cleanId = id.trim();
        if (productRepository.findProductById(cleanId) != null) {
            throw new IllegalStateException("A product with ID '" + cleanId + "' already exists.");
        }
        Console console = new Console(cleanId, title.trim(), price, stock, brand, model, generation);
        productRepository.saveProduct(console);
        return console;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllProducts();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    public Product findProductById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return productRepository.findProductById(id.trim());
    }

    public synchronized void updateProductStock(String id, int quantityToDeduct) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID is required for stock deduction.");
        }
        Product product = findProductById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
        if (!product.hasSufficientStock(quantityToDeduct)) {
            throw new IllegalStateException("Insufficient stock for product '" + product.getTitle()
                    + "'. Available: " + product.getStock() + ", Required: " + quantityToDeduct);
        }
        product.reduceStock(quantityToDeduct);
        productRepository.updateProduct(product);
    }

    private void validateCommonProductFields(String id, String title, double price, int stock) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product identifier (ID) is required.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Product title is required.");
        }
        if (price <= 0.0) {
            throw new IllegalArgumentException("Product price must be strictly positive.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Initial stock cannot be negative.");
        }
    }
}