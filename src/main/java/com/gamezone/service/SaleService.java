package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.SaleItem;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;

import java.util.List;

/**
 * Application service orchestrating sales transactions, business validations,
 * automatic inventory deduction, and sales historical reporting.
 */
public class SaleService {
    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final PersonService personService;

    public SaleService(SaleRepository saleRepository,
                       ProductService productService,
                       PersonService personService) {
        if (saleRepository == null) {
            throw new IllegalArgumentException("SaleRepository cannot be null.");
        }
        if (productService == null) {
            throw new IllegalArgumentException("ProductService cannot be null.");
        }
        if (personService == null) {
            throw new IllegalArgumentException("PersonService cannot be null.");
        }
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;
    }

    public synchronized Sale registerSale(String customerId, String sellerEmployeeCode, List<SaleItem> items) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer identification is required to register a sale.");
        }
        if (sellerEmployeeCode == null || sellerEmployeeCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Seller employee code is required to register a sale.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("A sale cannot be registered without at least one product.");
        }

        Customer customer = personService.findCustomerById(customerId.trim());
        if (customer == null) {
            throw new IllegalArgumentException("No customer found with ID: " + customerId);
        }

        Seller seller = personService.findSellerByCode(sellerEmployeeCode.trim());
        if (seller == null) {
            throw new IllegalArgumentException("No seller found with employee code: " + sellerEmployeeCode);
        }

        // 1. Validate sufficient stock for all items before applying any modifications
        for (SaleItem item : items) {
            Product currentProduct = productService.findProductById(item.getProduct().getId());
            if (currentProduct == null) {
                throw new IllegalArgumentException("Product '" + item.getProduct().getTitle() + "' no longer exists in catalog.");
            }
            if (!currentProduct.hasSufficientStock(item.getQuantity())) {
                throw new IllegalStateException("Insufficient stock for product '" + currentProduct.getTitle()
                        + "'. Available: " + currentProduct.getStock() + ", Requested: " + item.getQuantity());
            }
        }

        // 2. Atomically deduct inventory for each product
        for (SaleItem item : items) {
            productService.updateProductStock(item.getProduct().getId(), item.getQuantity());
        }

        // 3. Construct and persist Sale
        String saleId = generateSaleId();
        Sale sale = new Sale(saleId, customer, seller, items);
        saleRepository.saveSale(sale);
        return sale;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAllSales();
    }

    public List<Sale> getSalesByCustomer(String customerId) {
        return saleRepository.findSalesByCustomerId(customerId);
    }

    public List<Sale> getSalesBySeller(String employeeCode) {
        return saleRepository.findSalesBySellerEmployeeCode(employeeCode);
    }

    private synchronized String generateSaleId() {
        int currentCount = saleRepository.findAllSales().size() + 1;
        return String.format("VTA-%04d", currentCount);
    }
}