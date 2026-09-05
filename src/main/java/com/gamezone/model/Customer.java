package com.gamezone.model;

import java.util.Objects;

/**
 * Concrete domain class representing a store customer.
 * Extends the abstract Person entity with customer-specific contact information.
 */
public class Customer extends Person {
    private String email;

    public Customer(String id, String fullName, String phone, String email) {
        super(id, fullName, phone);
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Valid customer email is required.");
        }
        this.email = email.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Valid customer email is required.");
        }
        this.email = email.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email);
    }

    @Override
    public String toString() {
        return "[Customer] " + super.toString() + " | Email: " + email;
    }
}
