package com.gamezone.model;

import java.util.Objects;

/**
 * Concrete domain class representing a store seller/employee.
 * Extends the abstract Person entity with employee code and assigned working shift.
 */
public class Seller extends Person {
    private String employeeCode;
    private String shift;

    public Seller(String id, String fullName, String phone, String employeeCode, String shift) {
        super(id, fullName, phone);
        if (employeeCode == null || employeeCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee code cannot be null or empty.");
        }
        if (shift == null || shift.trim().isEmpty()) {
            throw new IllegalArgumentException("Work shift cannot be null or empty.");
        }
        this.employeeCode = employeeCode.trim().toUpperCase();
        this.shift = shift.trim();
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        if (shift == null || shift.trim().isEmpty()) {
            throw new IllegalArgumentException("Work shift cannot be null or empty.");
        }
        this.shift = shift.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Seller seller = (Seller) o;
        return Objects.equals(employeeCode, seller.employeeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeCode);
    }

    @Override
    public String toString() {
        return "[Seller] " + super.toString() + " | Employee Code: " + employeeCode + " | Shift: " + shift;
    }
}
