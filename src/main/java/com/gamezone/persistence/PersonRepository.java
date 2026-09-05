package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence layer repository for Person entities (Customer and Seller).
 * Handles reading, writing, and preloading files on the local filesystem.
 */
public class PersonRepository {
    private static final String DELIMITER = ";";
    private final File customersFile;
    private final File sellersFile;

    public PersonRepository(String dataDirectoryPath) {
        File dataDir = new File(dataDirectoryPath);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        this.customersFile = new File(dataDir, "customers.txt");
        this.sellersFile = new File(dataDir, "sellers.txt");
        ensureFilesExist();
        seedDefaultSellersIfEmpty();
    }

    private void ensureFilesExist() {
        try {
            if (!customersFile.exists()) {
                customersFile.createNewFile();
            }
            if (!sellersFile.exists()) {
                sellersFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing person data files: " + e.getMessage(), e);
        }
    }

    public synchronized void saveCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        List<Customer> customers = findAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equalsIgnoreCase(customer.getId())) {
                customers.set(i, customer);
                writeAllCustomers(customers);
                return;
            }
        }
        customers.add(customer);
        writeAllCustomers(customers);
    }

    public synchronized List<Customer> findAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        if (!customersFile.exists()) {
            return customers;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(customersFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(DELIMITER);
                if (parts.length >= 4) {
                    customers.add(new Customer(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read customers file: " + e.getMessage(), e);
        }
        return customers;
    }

    public synchronized Customer findCustomerById(String id) {
        if (id == null) return null;
        for (Customer c : findAllCustomers()) {
            if (c.getId().equalsIgnoreCase(id.trim())) {
                return c;
            }
        }
        return null;
    }

    public synchronized void saveSeller(Seller seller) {
        if (seller == null) {
            throw new IllegalArgumentException("Seller cannot be null.");
        }
        List<Seller> sellers = findAllSellers();
        for (int i = 0; i < sellers.size(); i++) {
            if (sellers.get(i).getEmployeeCode().equalsIgnoreCase(seller.getEmployeeCode())
                    || sellers.get(i).getId().equalsIgnoreCase(seller.getId())) {
                sellers.set(i, seller);
                writeAllSellers(sellers);
                return;
            }
        }
        sellers.add(seller);
        writeAllSellers(sellers);
    }

    public synchronized List<Seller> findAllSellers() {
        List<Seller> sellers = new ArrayList<>();
        if (!sellersFile.exists()) {
            return sellers;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(sellersFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(DELIMITER);
                if (parts.length >= 5) {
                    sellers.add(new Seller(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read sellers file: " + e.getMessage(), e);
        }
        return sellers;
    }

    public synchronized Seller findSellerByEmployeeCode(String code) {
        if (code == null) return null;
        for (Seller s : findAllSellers()) {
            if (s.getEmployeeCode().equalsIgnoreCase(code.trim())) {
                return s;
            }
        }
        return null;
    }

    public synchronized Seller findSellerById(String id) {
        if (id == null) return null;
        for (Seller s : findAllSellers()) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    public synchronized void seedDefaultSellersIfEmpty() {
        List<Seller> existing = findAllSellers();
        if (existing.isEmpty()) {
            List<Seller> defaults = new ArrayList<>();
            defaults.add(new Seller("1065123001", "Carlos Mendoza", "3001112233", "VEN001", "Mañana"));
            defaults.add(new Seller("1065123002", "Laura Gutierrez", "3004445566", "VEN002", "Tarde"));
            defaults.add(new Seller("1065123003", "Andres Mejia", "3007778899", "VEN003", "Noche"));
            writeAllSellers(defaults);
        }
    }

    private void writeAllCustomers(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(customersFile, false), StandardCharsets.UTF_8))) {
            for (Customer c : customers) {
                writer.println(String.join(DELIMITER,
                        c.getId(),
                        c.getFullName(),
                        c.getPhone(),
                        c.getEmail()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to customers file: " + e.getMessage(), e);
        }
    }

    private void writeAllSellers(List<Seller> sellers) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(sellersFile, false), StandardCharsets.UTF_8))) {
            for (Seller s : sellers) {
                writer.println(String.join(DELIMITER,
                        s.getId(),
                        s.getFullName(),
                        s.getPhone(),
                        s.getEmployeeCode(),
                        s.getShift()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to sellers file: " + e.getMessage(), e);
        }
    }
}
