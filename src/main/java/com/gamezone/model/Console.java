package com.gamezone.model;

import java.util.Objects;

/**
 * Concrete domain entity representing a video game console.
 * Specializes Product with brand, model, and hardware generation.
 */
public class Console extends Product {
    private String brand;
    private String model;
    private String generation;

    public Console(String id, String title, double price, int stock,
                   String brand, String model, String generation) {
        super(id, title, price, stock);
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Console brand cannot be empty.");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Console model cannot be empty.");
        }
        if (generation == null || generation.trim().isEmpty()) {
            throw new IllegalArgumentException("Console generation cannot be empty.");
        }
        this.brand = brand.trim();
        this.model = model.trim();
        this.generation = generation.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Console brand cannot be empty.");
        }
        this.brand = brand.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Console model cannot be empty.");
        }
        this.model = model.trim();
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        if (generation == null || generation.trim().isEmpty()) {
            throw new IllegalArgumentException("Console generation cannot be empty.");
        }
        this.generation = generation.trim();
    }

    @Override
    public String getDescription() {
        return String.format("[Console] ID: %s | Title: %s | Brand: %s | Model: %s | Gen: %s | Price: $%.2f | Stock: %d",
                getId(), getTitle(), brand, model, generation, getPrice(), getStock());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Console console = (Console) o;
        return Objects.equals(brand, console.brand) && Objects.equals(model, console.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), brand, model);
    }
}