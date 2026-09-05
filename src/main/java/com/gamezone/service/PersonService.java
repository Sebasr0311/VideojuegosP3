package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;

import java.util.List;

/**
 * Service layer orchestrating use-cases and business rules for Person entities.
 * Sits strictly between the UI presentation and the persistence repository.
 */
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        if (personRepository == null) {
            throw new IllegalArgumentException("PersonRepository dependency cannot be null.");
        }
        this.personRepository = personRepository;
    }

    public Customer registerCustomer(String id, String fullName, String phone, String email) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer identification (ID) is required.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer full name is required.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer contact phone is required.");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Valid customer email address is required.");
        }

        String cleanId = id.trim();
        String cleanEmail = email.trim().toLowerCase();

        if (personRepository.findCustomerById(cleanId) != null) {
            throw new IllegalStateException("A customer with ID '" + cleanId + "' is already registered.");
        }

        for (Customer existing : personRepository.findAllCustomers()) {
            if (existing.getEmail().equalsIgnoreCase(cleanEmail)) {
                throw new IllegalStateException("A customer with email '" + cleanEmail + "' is already registered.");
            }
        }

        Customer customer = new Customer(cleanId, fullName.trim(), phone.trim(), cleanEmail);
        personRepository.saveCustomer(customer);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return personRepository.findAllCustomers();
    }

    public Customer findCustomerById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return personRepository.findCustomerById(id.trim());
    }

    public Seller registerSeller(String id, String fullName, String phone, String employeeCode, String shift) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Seller identification is required.");
        }
        if (employeeCode == null || employeeCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee code is required.");
        }

        String cleanId = id.trim();
        String cleanCode = employeeCode.trim().toUpperCase();

        if (personRepository.findSellerById(cleanId) != null) {
            throw new IllegalStateException("A seller with ID '" + cleanId + "' already exists.");
        }
        if (personRepository.findSellerByEmployeeCode(cleanCode) != null) {
            throw new IllegalStateException("A seller with employee code '" + cleanCode + "' already exists.");
        }

        Seller seller = new Seller(cleanId, fullName.trim(), phone.trim(), cleanCode, shift.trim());
        personRepository.saveSeller(seller);
        return seller;
    }

    public List<Seller> getAllSellers() {
        return personRepository.findAllSellers();
    }

    public Seller findSellerByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return personRepository.findSellerByEmployeeCode(code.trim());
    }

    public Seller findSellerById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return personRepository.findSellerById(id.trim());
    }
}
