package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class ProductServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private ProductRepository productRepository;
    private ProductService productService;

    @Before
    public void setUp() throws Exception {
        File dataDir = temporaryFolder.newFolder("test-product-data");
        productRepository = new ProductRepository(dataDir.getAbsolutePath());
        productService = new ProductService(productRepository);
    }

    @Test
    public void testRegisterVideoGameSuccess() {
        VideoGame game = productService.registerVideoGame(
                "VG001", "The Legend of Zelda", 59.99, 10, "Nintendo Switch", "Adventure", "Everyone 10+");
        assertNotNull(game);
        assertEquals("VG001", game.getId());
        assertEquals("The Legend of Zelda", game.getTitle());
        assertTrue(game.getDescription().contains("Nintendo Switch"));

        Product retrieved = productService.findProductById("VG001");
        assertNotNull(retrieved);
        assertTrue(retrieved instanceof VideoGame);
    }

    @Test
    public void testRegisterConsoleSuccess() {
        Console console = productService.registerConsole(
                "CON001", "PlayStation 5", 499.99, 5, "Sony", "Standard Edition", "9th Gen");
        assertNotNull(console);
        assertEquals("CON001", console.getId());
        assertEquals(5, console.getStock());
        assertTrue(console.getDescription().contains("Sony"));

        Product retrieved = productService.findProductById("CON001");
        assertNotNull(retrieved);
        assertTrue(retrieved instanceof Console);
    }

    @Test
    public void testDeductStockSuccess() {
        productService.registerVideoGame("VG002", "Halo Infinite", 49.99, 8, "Xbox Series X", "FPS", "Teen");
        productService.updateProductStock("VG002", 3);

        Product updated = productService.findProductById("VG002");
        assertNotNull(updated);
        assertEquals(5, updated.getStock());
    }

    @Test(expected = IllegalStateException.class)
    public void testDeductStockInsufficientThrowsException() {
        productService.registerVideoGame("VG003", "Super Mario Odyssey", 59.99, 2, "Nintendo Switch", "Platformer", "Everyone");
        productService.updateProductStock("VG003", 5);
    }

    @Test
    public void testAvailableProductsFilter() {
        productService.registerVideoGame("VG004", "Game In Stock", 20.0, 3, "PC", "RPG", "Teen");
        productService.registerVideoGame("VG005", "Game Out Of Stock", 30.0, 0, "PC", "Action", "Mature");

        List<Product> available = productService.getAvailableProducts();
        assertEquals(1, available.size());
        assertEquals("VG004", available.get(0).getId());
    }
}