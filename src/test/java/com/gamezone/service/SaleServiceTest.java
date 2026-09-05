package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SaleServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private PersonService personService;
    private ProductService productService;
    private SaleService saleService;

    private Customer testCustomer;
    private Seller testSeller;
    private VideoGame testGame;

    @Before
    public void setUp() throws Exception {
        File dataDir = temporaryFolder.newFolder("test-sale-data");
        PersonRepository personRepo = new PersonRepository(dataDir.getAbsolutePath());
        ProductRepository productRepo = new ProductRepository(dataDir.getAbsolutePath());
        SaleRepository saleRepo = new SaleRepository(dataDir.getAbsolutePath(), productRepo, personRepo);

        personService = new PersonService(personRepo);
        productService = new ProductService(productRepo);
        saleService = new SaleService(saleRepo, productService, personService);

        testCustomer = personService.registerCustomer("1001", "Juan Perez", "3001234567", "juan@test.com");
        testSeller = personService.findSellerByCode("VEN001");
        testGame = productService.registerVideoGame("VG100", "Elden Ring", 60.0, 10, "PS5", "Action RPG", "Mature");
    }

    @Test
    public void testRegisterSaleSuccessAndDeductStock() {
        List<SaleItem> items = new ArrayList<>();
        items.add(new SaleItem(testGame, 2));

        Sale sale = saleService.registerSale(testCustomer.getId(), testSeller.getEmployeeCode(), items);

        assertNotNull(sale);
        assertEquals(120.0, sale.getTotal(), 0.001);
        assertEquals(1, sale.getItems().size());

        // Verify stock deduction
        Product updatedProduct = productService.findProductById(testGame.getId());
        assertEquals(8, updatedProduct.getStock());

        // Verify persistence
        List<Sale> allSales = saleService.getAllSales();
        assertEquals(1, allSales.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterSaleEmptyItemsThrowsException() {
        saleService.registerSale(testCustomer.getId(), testSeller.getEmployeeCode(), Collections.emptyList());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterSaleInsufficientStockThrowsException() {
        List<SaleItem> items = new ArrayList<>();
        items.add(new SaleItem(testGame, 15)); // only 10 in stock

        saleService.registerSale(testCustomer.getId(), testSeller.getEmployeeCode(), items);
    }

    @Test
    public void testQuerySalesByCustomerAndSeller() {
        List<SaleItem> items = new ArrayList<>();
        items.add(new SaleItem(testGame, 1));
        saleService.registerSale(testCustomer.getId(), testSeller.getEmployeeCode(), items);

        List<Sale> customerSales = saleService.getSalesByCustomer(testCustomer.getId());
        assertEquals(1, customerSales.size());

        List<Sale> sellerSales = saleService.getSalesBySeller(testSeller.getEmployeeCode());
        assertEquals(1, sellerSales.size());

        List<Sale> emptyCustomerSales = saleService.getSalesByCustomer("NON_EXISTENT");
        assertTrue(emptyCustomerSales.isEmpty());
    }
}