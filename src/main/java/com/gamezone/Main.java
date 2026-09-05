package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.ConsoleUI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Main application entry point for GameZoneUnicesar.
 * Configures the layered architecture runtime environment, seeds preloaded datasets,
 * and launches the interactive console UI.
 */
public class Main {

    public static void main(String[] args) {
        String dataDirPath = resolveDataDirectory();

        // 1. Persistence Layer Initialization
        PersonRepository personRepository = new PersonRepository(dataDirPath);
        ProductRepository productRepository = new ProductRepository(dataDirPath);
        SaleRepository saleRepository = new SaleRepository(dataDirPath, productRepository, personRepository);

        // 2. Service Layer Initialization (Dependency Injection)
        PersonService personService = new PersonService(personRepository);
        ProductService productService = new ProductService(productRepository);
        SaleService saleService = new SaleService(saleRepository, productService, personService);

        // 3. User Interface Layer Initialization
        ConsoleUI consoleUI = new ConsoleUI(productService, personService, saleService);

        // 4. Start Application Loop
        consoleUI.start();
    }

    /**
     * Resolves and prepares the active runtime data directory.
     * Ensures initial preloaded files are copied from src/main/data if present.
     */
    private static String resolveDataDirectory() {
        File runtimeDataDir = new File("data");
        if (!runtimeDataDir.exists()) {
            runtimeDataDir.mkdirs();
        }

        File targetSellers = new File(runtimeDataDir, "sellers.txt");
        File preloadedSellers = new File("src/main/data/sellers.txt");

        if ((!targetSellers.exists() || targetSellers.length() == 0) && preloadedSellers.exists()) {
            try {
                Files.copy(preloadedSellers.toPath(), targetSellers.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("[WARN] Could not copy preloaded sellers: " + e.getMessage());
            }
        }

        return runtimeDataDir.getAbsolutePath();
    }
}