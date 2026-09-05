package com.gamezone.model;

import java.util.Objects;

/**
 * Abstract base class representing any commercial product in GameZone Unicesar.
 * Defines shared pricing, inventory state, stock validation, and polymorphic description.
 */
public abstract class Product {
    private String id;
    private String title;
    private double price;
    private int stock;

    public Product(String id, String title, double price, int stock) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Product title cannot be null or empty.");
        }
        if (price <= 0.0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Product initial stock cannot be negative.");
        }
        this.id = id.trim();
        this.title = title.trim();
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Product title cannot be null or empty.");
        }
        this.title = title.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0.0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    public boolean hasSufficientStock(int quantity) {
        return quantity > 0 && this.stock >= quantity;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to reduce must be greater than zero.");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException("Insufficient inventory for product '" + title + "'. Available: " + stock + ", Requested: " + quantity);
        }
        this.stock -= quantity;
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to increase must be greater than zero.");
        }
        this.stock += quantity;
    }

    /**
     * Abstract polymorphic method to be specialized by every product category.
     * @return Formatted domain description detailing specific attributes.
     */
    public abstract String getDescription();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}