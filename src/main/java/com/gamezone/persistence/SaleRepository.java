package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.SaleItem;
import com.gamezone.model.Seller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence layer repository for Sale transactions.
 * Serializes sales and reconstitutes entities using referenced repositories.
 */
public class SaleRepository {
    private static final String DELIMITER = ";";
    private static final String ITEM_DELIMITER = ",";
    private static final String ITEM_FIELD_DELIMITER = ":";
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final File salesFile;
    private final ProductRepository productRepository;
    private final PersonRepository personRepository;

    public SaleRepository(String dataDirectoryPath,
                          ProductRepository productRepository,
                          PersonRepository personRepository) {
        if (productRepository == null) {
            throw new IllegalArgumentException("ProductRepository cannot be null.");
        }
        if (personRepository == null) {
            throw new IllegalArgumentException("PersonRepository cannot be null.");
        }
        this.productRepository = productRepository;
        this.personRepository = personRepository;

        File dataDir = new File(dataDirectoryPath);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        this.salesFile = new File(dataDir, "sales.txt");
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!salesFile.exists()) {
                salesFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing sales data file: " + e.getMessage(), e);
        }
    }

    public synchronized void saveSale(Sale sale) {
        if (sale == null) {
            throw new IllegalArgumentException("Sale cannot be null.");
        }
        List<Sale> sales = findAllSales();
        sales.add(sale);
        writeAllSales(sales);
    }

    public synchronized List<Sale> findAllSales() {
        List<Sale> sales = new ArrayList<>();
        if (!salesFile.exists()) {
            return sales;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(salesFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(DELIMITER);
                if (parts.length < 5) {
                    continue;
                }

                String saleId = parts[0];
                LocalDateTime dateTime = LocalDateTime.parse(parts[1], ISO_FORMATTER);
                String customerId = parts[2];
                String sellerCode = parts[3];
                String itemsData = parts[4];

                Customer customer = personRepository.findCustomerById(customerId);
                Seller seller = personRepository.findSellerByEmployeeCode(sellerCode);

                if (customer == null) {
                    // Fallback stub if customer was removed
                    customer = new Customer(customerId, "Unknown Customer (" + customerId + ")", "0000000000", "unknown@mail.com");
                }
                if (seller == null) {
                    // Fallback stub if seller was removed
                    seller = new Seller("0", "Unknown Seller", "0000000000", sellerCode, "Unknown");
                }

                List<SaleItem> items = new ArrayList<>();
                String[] itemTokens = itemsData.split(ITEM_DELIMITER);
                for (String token : itemTokens) {
                    String[] tokenFields = token.split(ITEM_FIELD_DELIMITER);
                    if (tokenFields.length >= 3) {
                        String productId = tokenFields[0];
                        int quantity = Integer.parseInt(tokenFields[1]);
                        double unitPrice = Double.parseDouble(tokenFields[2]);

                        Product product = productRepository.findProductById(productId);
                        if (product == null) {
                            // Lightweight placeholder to preserve history
                            product = new com.gamezone.model.VideoGame(
                                    productId, "Archived Product (" + productId + ")", unitPrice, 0, "General", "General", "General");
                        }
                        items.add(new SaleItem(product, quantity, unitPrice));
                    }
                }

                if (!items.isEmpty()) {
                    sales.add(new Sale(saleId, dateTime, customer, seller, items));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read sales file: " + e.getMessage(), e);
        }
        return sales;
    }

    public synchronized List<Sale> findSalesByCustomerId(String customerId) {
        List<Sale> result = new ArrayList<>();
        if (customerId == null || customerId.trim().isEmpty()) {
            return result;
        }
        String target = customerId.trim();
        for (Sale sale : findAllSales()) {
            if (sale.getCustomer().getId().equalsIgnoreCase(target)) {
                result.add(sale);
            }
        }
        return result;
    }

    public synchronized List<Sale> findSalesBySellerEmployeeCode(String employeeCode) {
        List<Sale> result = new ArrayList<>();
        if (employeeCode == null || employeeCode.trim().isEmpty()) {
            return result;
        }
        String target = employeeCode.trim();
        for (Sale sale : findAllSales()) {
            if (sale.getSeller().getEmployeeCode().equalsIgnoreCase(target)) {
                result.add(sale);
            }
        }
        return result;
    }

    private void writeAllSales(List<Sale> sales) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(salesFile, false), StandardCharsets.UTF_8))) {
            for (Sale sale : sales) {
                StringBuilder itemsBuilder = new StringBuilder();
                for (int i = 0; i < sale.getItems().size(); i++) {
                    SaleItem item = sale.getItems().get(i);
                    itemsBuilder.append(item.getProduct().getId())
                            .append(ITEM_FIELD_DELIMITER)
                            .append(item.getQuantity())
                            .append(ITEM_FIELD_DELIMITER)
                            .append(item.getUnitPrice());
                    if (i < sale.getItems().size() - 1) {
                        itemsBuilder.append(ITEM_DELIMITER);
                    }
                }

                writer.println(String.join(DELIMITER,
                        sale.getId(),
                        sale.getDateTime().format(ISO_FORMATTER),
                        sale.getCustomer().getId(),
                        sale.getSeller().getEmployeeCode(),
                        itemsBuilder.toString()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to sales file: " + e.getMessage(), e);
        }
    }
}