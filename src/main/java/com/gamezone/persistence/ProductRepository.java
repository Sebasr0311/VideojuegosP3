package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence layer repository for Product entities (VideoGame and Console).
 * Handles persistent serialization and retrieval from the local file system.
 */
public class ProductRepository {
    private static final String DELIMITER = ";";
    private static final String TYPE_VIDEOGAME = "VIDEOGAME";
    private static final String TYPE_CONSOLE = "CONSOLE";

    private final File productsFile;

    public ProductRepository(String dataDirectoryPath) {
        File dataDir = new File(dataDirectoryPath);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        this.productsFile = new File(dataDir, "products.txt");
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!productsFile.exists()) {
                productsFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing products data file: " + e.getMessage(), e);
        }
    }

    public synchronized void saveProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        List<Product> products = findAllProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equalsIgnoreCase(product.getId())) {
                products.set(i, product);
                writeAllProducts(products);
                return;
            }
        }
        products.add(product);
        writeAllProducts(products);
    }

    public synchronized void updateProduct(Product product) {
        saveProduct(product);
    }

    public synchronized List<Product> findAllProducts() {
        List<Product> products = new ArrayList<>();
        if (!productsFile.exists()) {
            return products;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(productsFile), StandardCharsets.UTF_8))) {
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
                String type = parts[0];
                String id = parts[1];
                String title = parts[2];
                double price = Double.parseDouble(parts[3]);
                int stock = Integer.parseInt(parts[4]);

                if (TYPE_VIDEOGAME.equalsIgnoreCase(type) && parts.length >= 8) {
                    String platform = parts[5];
                    String genre = parts[6];
                    String ageRating = parts[7];
                    products.add(new VideoGame(id, title, price, stock, platform, genre, ageRating));
                } else if (TYPE_CONSOLE.equalsIgnoreCase(type) && parts.length >= 8) {
                    String brand = parts[5];
                    String model = parts[6];
                    String generation = parts[7];
                    products.add(new Console(id, title, price, stock, brand, model, generation));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read products file: " + e.getMessage(), e);
        }
        return products;
    }

    public synchronized List<Product> findAvailableProducts() {
        List<Product> available = new ArrayList<>();
        for (Product p : findAllProducts()) {
            if (p.getStock() > 0) {
                available.add(p);
            }
        }
        return available;
    }

    public synchronized Product findProductById(String id) {
        if (id == null) return null;
        for (Product p : findAllProducts()) {
            if (p.getId().equalsIgnoreCase(id.trim())) {
                return p;
            }
        }
        return null;
    }

    private void writeAllProducts(List<Product> products) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(productsFile, false), StandardCharsets.UTF_8))) {
            for (Product p : products) {
                if (p instanceof VideoGame) {
                    VideoGame vg = (VideoGame) p;
                    writer.println(String.join(DELIMITER,
                            TYPE_VIDEOGAME,
                            vg.getId(),
                            vg.getTitle(),
                            String.valueOf(vg.getPrice()),
                            String.valueOf(vg.getStock()),
                            vg.getPlatform(),
                            vg.getGenre(),
                            vg.getAgeRating()));
                } else if (p instanceof Console) {
                    Console c = (Console) p;
                    writer.println(String.join(DELIMITER,
                            TYPE_CONSOLE,
                            c.getId(),
                            c.getTitle(),
                            String.valueOf(c.getPrice()),
                            String.valueOf(c.getStock()),
                            c.getBrand(),
                            c.getModel(),
                            c.getGeneration()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to products file: " + e.getMessage(), e);
        }
    }
}