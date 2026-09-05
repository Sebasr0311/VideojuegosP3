package com.gamezone.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain Aggregate Root representing a finalized sales transaction in GameZone Unicesar.
 * Relates the buyer Customer, the attending Seller, and the composite collection of SaleItems.
 * Acts as the Information Expert for total calculation and enforces the minimum one item rule.
 */
public class Sale {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String id;
    private LocalDateTime dateTime;
    private Customer customer;
    private Seller seller;
    private List<SaleItem> items;

    public Sale(String id, Customer customer, Seller seller, List<SaleItem> items) {
        this(id, LocalDateTime.now(), customer, seller, items);
    }

    public Sale(String id, LocalDateTime dateTime, Customer customer, Seller seller, List<SaleItem> items) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Sale ID cannot be null or empty.");
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("Sale date and time cannot be null.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Sale must have an associated customer.");
        }
        if (seller == null) {
            throw new IllegalArgumentException("Sale must have an associated seller.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one product.");
        }

        this.id = id.trim();
        this.dateTime = dateTime;
        this.customer = customer;
        this.seller = seller;
        this.items = new ArrayList<>(items);
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getFormattedDateTime() {
        return dateTime.format(FORMATTER);
    }

    public Customer getCustomer() {
        return customer;
    }

    public Seller getSeller() {
        return seller;
    }

    public List<SaleItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double calculateTotal() {
        double total = 0.0;
        for (SaleItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public double getTotal() {
        return calculateTotal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Sale ID: %s | Date: %s ===\n", id, getFormattedDateTime()));
        sb.append(String.format("Customer: %s (ID: %s)\n", customer.getFullName(), customer.getId()));
        sb.append(String.format("Seller: %s (Code: %s)\n", seller.getFullName(), seller.getEmployeeCode()));
        sb.append("Products:\n");
        for (SaleItem item : items) {
            sb.append(" - ").append(item.toString()).append("\n");
        }
        sb.append(String.format("Total: $%.2f\n", calculateTotal()));
        return sb.toString();
    }
}