package com.gamezone.model;

import java.util.Objects;

/**
 * Line item component of a Sale, linking a Product with quantity and unit price.
 * Follows composition lifecycle within the Sale aggregate root.
 */
public class SaleItem {
    private Product product;
    private int quantity;
    private double unitPrice;

    public SaleItem(Product product, int quantity) {
        this(product, quantity, product != null ? product.getPrice() : 0.0);
    }

    public SaleItem(Product product, int quantity, double unitPrice) {
        if (product == null) {
            throw new IllegalArgumentException("Product associated with sale item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than zero.");
        }
        if (unitPrice < 0.0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than zero.");
        }
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0.0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return quantity * unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleItem saleItem = (SaleItem) o;
        return quantity == saleItem.quantity &&
                Double.compare(saleItem.unitPrice, unitPrice) == 0 &&
                Objects.equals(product, saleItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, unitPrice);
    }

    @Override
    public String toString() {
        return String.format("%s [Qty: %d @ $%.2f = Subtotal: $%.2f]",
                product.getTitle(), quantity, unitPrice, getSubtotal());
    }
}