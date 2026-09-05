package com.gamezone.model;

import java.util.Objects;

/**
 * Abstract base class representing any human actor in the GameZone Unicesar system.
 * Enforces encapsulation and shared personal identity attributes.
 */
public abstract class Person {
    private String id;
    private String fullName;
    private String phone;

    public Person(String id, String fullName, String phone) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Person ID cannot be null or empty.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty.");
        }
        this.id = id.trim();
        this.fullName = fullName.trim();
        this.phone = phone.trim();
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty.");
        }
        this.fullName = fullName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty.");
        }
        this.phone = phone.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + fullName + " | Phone: " + phone;
    }
}
