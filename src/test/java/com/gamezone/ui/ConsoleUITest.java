package com.gamezone.ui;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class ConsoleUITest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private ProductService productService;
    private PersonService personService;
    private SaleService saleService;

    @Before
    public void setUp() throws Exception {
        File dataDir = temporaryFolder.newFolder("test-ui-data");
        PersonRepository personRepo = new PersonRepository(dataDir.getAbsolutePath());
        ProductRepository productRepo = new ProductRepository(dataDir.getAbsolutePath());
        SaleRepository saleRepo = new SaleRepository(dataDir.getAbsolutePath(), productRepo, personRepo);

        personService = new PersonService(personRepo);
        productService = new ProductService(productRepo);
        saleService = new SaleService(saleRepo, productService, personService);
    }

    @Test
    public void testFullMenuFlowCoversAllTenOperations() {
        String input = String.join("\n",
                // 1. Register VideoGame
                "1", "VG01", "God of War", "69.99", "10", "PS5", "Action", "Mature",
                // 2. Register Console
                "2", "CON01", "Nintendo Switch OLED", "349.99", "5", "Nintendo", "OLED Model", "8th Gen",
                // 3. List Available Products
                "3",
                // 4. Register Customer
                "4", "1065001", "Carlos Vives", "3151234567", "carlos@vives.com",
                // 5. List Customers
                "5",
                // 6. List Sellers
                "6",
                // 7. Register Sale
                "7", "1065001", "VEN001", "VG01", "2", "N",
                // 8. List All Sales
                "8",
                // 9. Query Sales by Customer
                "9", "1065001",
                // 10. Query Sales by Seller
                "10", "VEN001",
                // 0. Exit
                "0"
        ) + "\n";

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ConsoleUI consoleUI = new ConsoleUI(productService, personService, saleService, new Scanner(in));

        consoleUI.start();

        assertEquals(2, productService.getAllProducts().size());
        assertEquals(1, personService.getAllCustomers().size());
        assertEquals(3, personService.getAllSellers().size());
        assertEquals(1, saleService.getAllSales().size());
        assertEquals(8, productService.findProductById("VG01").getStock());
    }
}