package com.gamezone.ui;

import com.gamezone.model.*;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Presentation Layer: Interactive command-line interface (CLI) for GameZone Unicesar.
 * Exposes all required functional operations, coordinating user input with the service layer.
 */
public class ConsoleUI {
    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final Scanner scanner;

    public ConsoleUI(ProductService productService, PersonService personService, SaleService saleService) {
        this(productService, personService, saleService, new Scanner(System.in));
    }

    public ConsoleUI(ProductService productService, PersonService personService, SaleService saleService, Scanner scanner) {
        if (productService == null || personService == null || saleService == null) {
            throw new IllegalArgumentException("Service dependencies cannot be null.");
        }
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.scanner = scanner != null ? scanner : new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        System.out.println("==========================================================");
        System.out.println("       SISTEMA DE GESTION - GAMEZONE UNICESAR            ");
        System.out.println("   Tienda de Videojuegos y Consolas - Valledupar, Cesar   ");
        System.out.println("==========================================================");

        while (running) {
            printMainMenu();
            int option = readInt("Seleccione una opcion: ");
            System.out.println();

            switch (option) {
                case 1:
                    handleRegisterVideoGame();
                    break;
                case 2:
                    handleRegisterConsole();
                    break;
                case 3:
                    handleListAvailableProducts();
                    break;
                case 4:
                    handleRegisterCustomer();
                    break;
                case 5:
                    handleListCustomers();
                    break;
                case 6:
                    handleListSellers();
                    break;
                case 7:
                    handleRegisterSale();
                    break;
                case 8:
                    handleListAllSales();
                    break;
                case 9:
                    handleListCustomerPurchases();
                    break;
                case 10:
                    handleListSellerSales();
                    break;
                case 0:
                    System.out.println("Gracias por usar el sistema GameZone Unicesar. Hasta pronto!");
                    running = false;
                    break;
                default:
                    System.out.println("[ERROR] Opcion invalida. Por favor seleccione una opcion entre 0 y 10.");
            }
            System.out.println();
        }
    }

    private void printMainMenu() {
        System.out.println("----------------- MENU PRINCIPAL -----------------");
        System.out.println("  1. Registrar un nuevo videojuego");
        System.out.println("  2. Registrar una nueva consola");
        System.out.println("  3. Listar productos disponibles en inventario");
        System.out.println("  4. Registrar un nuevo cliente");
        System.out.println("  5. Listar clientes registrados");
        System.out.println("  6. Listar vendedores registrados");
        System.out.println("  7. Registrar una nueva venta");
        System.out.println("  8. Consultar historial completo de ventas");
        System.out.println("  9. Consultar historial de compras por cliente");
        System.out.println(" 10. Consultar historial de ventas por vendedor");
        System.out.println("  0. Salir del sistema");
        System.out.println("--------------------------------------------------");
    }

    // Operation 1: Registrar Videojuego
    private void handleRegisterVideoGame() {
        System.out.println("--- [1] REGISTRO DE VIDEOJUEGO ---");
        try {
            String id = readNonEmptyString("Identificador (SKU/Codigo): ");
            String title = readNonEmptyString("Titulo del videojuego: ");
            double price = readPositiveDouble("Precio unitario ($): ");
            int stock = readNonNegativeInt("Cantidad inicial en inventario: ");
            String platform = readNonEmptyString("Plataforma (PS5, Xbox, Switch, PC, etc.): ");
            String genre = readNonEmptyString("Genero (Accion, RPG, Deportes, etc.): ");
            String ageRating = readNonEmptyString("Clasificacion de edad (E, T, M, etc.): ");

            VideoGame game = productService.registerVideoGame(id, title, price, stock, platform, genre, ageRating);
            System.out.println("\n[EXITO] Videojuego registrado satisfactoriamente:");
            System.out.println("  " + game.getDescription());
        } catch (Exception e) {
            System.out.println("\n[ERROR] No se pudo registrar el videojuego: " + e.getMessage());
        }
    }

    // Operation 2: Registrar Consola
    private void handleRegisterConsole() {
        System.out.println("--- [2] REGISTRO DE CONSOLA ---");
        try {
            String id = readNonEmptyString("Identificador (SKU/Codigo): ");
            String title = readNonEmptyString("Nombre/Titulo comercial: ");
            double price = readPositiveDouble("Precio unitario ($): ");
            int stock = readNonNegativeInt("Cantidad inicial en inventario: ");
            String brand = readNonEmptyString("Marca (Sony, Microsoft, Nintendo, etc.): ");
            String model = readNonEmptyString("Modelo (Slim, Pro, OLED, etc.): ");
            String generation = readNonEmptyString("Generacion (ej. 9na Generacion): ");

            Console console = productService.registerConsole(id, title, price, stock, brand, model, generation);
            System.out.println("\n[EXITO] Consola registrada satisfactoriamente:");
            System.out.println("  " + console.getDescription());
        } catch (Exception e) {
            System.out.println("\n[ERROR] No se pudo registrar la consola: " + e.getMessage());
        }
    }

    // Operation 3: Listar Productos Disponibles
    private void handleListAvailableProducts() {
        System.out.println("--- [3] PRODUCTOS DISPONIBLES EN INVENTARIO ---");
        List<Product> available = productService.getAvailableProducts();
        if (available.isEmpty()) {
            System.out.println("No hay productos con stock disponible actualmente.");
            return;
        }
        System.out.println("Total productos disponibles: " + available.size());
        for (Product p : available) {
            System.out.println(" - " + p.getDescription());
        }
    }

    // Operation 4: Registrar Cliente
    private void handleRegisterCustomer() {
        System.out.println("--- [4] REGISTRO DE CLIENTE ---");
        try {
            String id = readNonEmptyString("Numero de Identificacion (CC/Doc): ");
            String fullName = readNonEmptyString("Nombre completo: ");
            String phone = readNonEmptyString("Telefono de contacto: ");
            String email = readNonEmptyString("Correo electronico: ");

            Customer customer = personService.registerCustomer(id, fullName, phone, email);
            System.out.println("\n[EXITO] Cliente registrado satisfactoriamente:");
            System.out.println("  " + customer.toString());
        } catch (Exception e) {
            System.out.println("\n[ERROR] No se pudo registrar el cliente: " + e.getMessage());
        }
    }

    // Operation 5: Listar Clientes
    private void handleListCustomers() {
        System.out.println("--- [5] LISTADO DE CLIENTES REGISTRADOS ---");
        List<Customer> customers = personService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No hay clientes registrados en el sistema.");
            return;
        }
        System.out.println("Total clientes: " + customers.size());
        for (Customer c : customers) {
            System.out.println(" - " + c.toString());
        }
    }

    // Operation 6: Listar Vendedores
    private void handleListSellers() {
        System.out.println("--- [6] LISTADO DE VENDEDORES REGISTRADOS ---");
        List<Seller> sellers = personService.getAllSellers();
        if (sellers.isEmpty()) {
            System.out.println("No hay vendedores registrados en el sistema.");
            return;
        }
        System.out.println("Total vendedores: " + sellers.size());
        for (Seller s : sellers) {
            System.out.println(" - " + s.toString());
        }
    }

    // Operation 7: Registrar Venta
    private void handleRegisterSale() {
        System.out.println("--- [7] REGISTRO DE NUEVA VENTA ---");
        try {
            // Validate that we have customers, sellers, and products
            if (personService.getAllCustomers().isEmpty()) {
                System.out.println("[ALERTA] No hay clientes registrados. Debe registrar al menos un cliente primero (Opcion 4).");
                return;
            }
            if (personService.getAllSellers().isEmpty()) {
                System.out.println("[ALERTA] No hay vendedores disponibles en el sistema.");
                return;
            }
            if (productService.getAvailableProducts().isEmpty()) {
                System.out.println("[ALERTA] No hay productos disponibles en inventario para la venta.");
                return;
            }

            String customerId = readNonEmptyString("Identificacion del cliente comprador: ");
            Customer customer = personService.findCustomerById(customerId);
            if (customer == null) {
                System.out.println("[ERROR] Cliente no encontrado con ID: " + customerId);
                return;
            }
            System.out.println("  Cliente seleccionado: " + customer.getFullName());

            String sellerCode = readNonEmptyString("Codigo de empleado del vendedor: ");
            Seller seller = personService.findSellerByCode(sellerCode);
            if (seller == null) {
                System.out.println("[ERROR] Vendedor no encontrado con codigo: " + sellerCode);
                return;
            }
            System.out.println("  Vendedor asignado: " + seller.getFullName() + " (Turno: " + seller.getShift() + ")");

            List<SaleItem> items = new ArrayList<>();
            boolean addingProducts = true;

            while (addingProducts) {
                System.out.println("\n--- Agregar Producto a la Venta ---");
                String prodId = readNonEmptyString("ID del producto a comprar: ");
                Product product = productService.findProductById(prodId);
                if (product == null) {
                    System.out.println("[ERROR] Producto no existe en el catalogo.");
                } else if (product.getStock() <= 0) {
                    System.out.println("[ERROR] El producto '" + product.getTitle() + "' esta agotado (Stock 0).");
                } else {
                    System.out.println("  Producto: " + product.getTitle() + " | Stock disponible: " + product.getStock() + " | Precio: $" + product.getPrice());
                    int quantity = readPositiveInt("Cantidad a comprar: ");
                    if (quantity > product.getStock()) {
                        System.out.println("[ERROR] Cantidad solicitada (" + quantity + ") supera el inventario disponible (" + product.getStock() + ").");
                    } else {
                        items.add(new SaleItem(product, quantity));
                        System.out.println("[OK] Producto agregado a la venta. Subtotal: $" + String.format("%.2f", (quantity * product.getPrice())));
                    }
                }

                if (items.isEmpty()) {
                    System.out.println("Debe agregar al menos un producto para registrar la venta.");
                } else {
                    String more = readNonEmptyString("Desea agregar otro producto a esta venta? (S/N): ");
                    if (!more.equalsIgnoreCase("s") && !more.equalsIgnoreCase("si")) {
                        addingProducts = false;
                    }
                }
            }

            Sale sale = saleService.registerSale(customer.getId(), seller.getEmployeeCode(), items);
            System.out.println("\n==================================================");
            System.out.println("        VENTA REGISTRADA EXITOSAMENTE             ");
            System.out.println("==================================================");
            System.out.println(sale.toString());
            System.out.println("==================================================");

        } catch (Exception e) {
            System.out.println("\n[ERROR] No se pudo completar el registro de la venta: " + e.getMessage());
        }
    }

    // Operation 8: Historial Completo de Ventas
    private void handleListAllSales() {
        System.out.println("--- [8] HISTORIAL COMPLETO DE VENTAS REALIZADAS ---");
        List<Sale> sales = saleService.getAllSales();
        if (sales.isEmpty()) {
            System.out.println("No se han registrado ventas en el sistema todavia.");
            return;
        }
        System.out.println("Total de ventas registradas: " + sales.size());
        System.out.println();
        for (Sale s : sales) {
            System.out.println(s.toString());
        }
    }

    // Operation 9: Historial de Compras por Cliente
    private void handleListCustomerPurchases() {
        System.out.println("--- [9] HISTORIAL DE COMPRAS POR CLIENTE ---");
        String customerId = readNonEmptyString("Identificacion del cliente: ");
        Customer customer = personService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("[ALERTA] No se encontro cliente con ID: " + customerId);
            return;
        }

        List<Sale> purchases = saleService.getSalesByCustomer(customerId);
        System.out.println("\nCompras realizadas por: " + customer.getFullName() + " (Total: " + purchases.size() + ")");
        if (purchases.isEmpty()) {
            System.out.println("Este cliente aun no ha realizado compras en la tienda.");
            return;
        }
        for (Sale s : purchases) {
            System.out.println(s.toString());
        }
    }

    // Operation 10: Historial de Ventas por Vendedor
    private void handleListSellerSales() {
        System.out.println("--- [10] HISTORIAL DE VENTAS POR VENDEDOR ---");
        String code = readNonEmptyString("Codigo de empleado del vendedor: ");
        Seller seller = personService.findSellerByCode(code);
        if (seller == null) {
            System.out.println("[ALERTA] No se encontro vendedor con codigo: " + code);
            return;
        }

        List<Sale> sales = saleService.getSalesBySeller(code);
        System.out.println("\nVentas atendidas por: " + seller.getFullName() + " (" + seller.getEmployeeCode() + ") (Total: " + sales.size() + ")");
        if (sales.isEmpty()) {
            System.out.println("Este vendedor aun no ha atendido ventas en la tienda.");
            return;
        }
        for (Sale s : sales) {
            System.out.println(s.toString());
        }
    }

    // Helper Input Methods
    private String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            }
            System.out.println("[!] El valor no puede ser vacio. Intente de nuevo.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Debe ingresar un numero entero valido.");
            }
        }
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val > 0) {
                return val;
            }
            System.out.println("[!] El valor debe ser mayor a cero.");
        }
    }

    private int readNonNegativeInt(String prompt) {
        while (true) {
            int val = readInt(prompt);
            if (val >= 0) {
                return val;
            }
            System.out.println("[!] El valor no puede ser negativo.");
        }
    }

    private double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                double val = Double.parseDouble(input.trim().replace(',', '.'));
                if (val > 0.0) {
                    return val;
                }
                System.out.println("[!] El valor debe ser mayor a cero.");
            } catch (NumberFormatException e) {
                System.out.println("[!] Debe ingresar un numero decimal valido.");
            }
        }
    }
}