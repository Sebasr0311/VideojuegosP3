# Complete UML Class Diagram

This diagram specifies all classes across the four architectural layers (`model`, `persistence`, `service`, and `ui`), detailing attributes, methods, UML visibilities (`+` public, `-` private, `#` protected), multiplicities, and relationship types.

```mermaid
classDiagram
    direction TB

    %% ==========================================
    %% 1. MODEL LAYER (com.gamezone.model)
    %% ==========================================
    class Person {
        <<abstract>>
        -String id
        -String fullName
        -String phone
        +Person(String id, String fullName, String phone)
        +getId() String
        +getFullName() String
        +getPhone() String
        +setFullName(String fullName) void
        +setPhone(String phone) void
        +toString() String
    }

    class Customer {
        -String email
        +Customer(String id, String fullName, String phone, String email)
        +getEmail() String
        +setEmail(String email) void
        +toString() String
    }

    class Seller {
        -String employeeCode
        -String shift
        +Seller(String id, String fullName, String phone, String employeeCode, String shift)
        +getEmployeeCode() String
        +getShift() String
        +setShift(String shift) void
        +toString() String
    }

    class Product {
        <<abstract>>
        -String id
        -String title
        -double price
        -int stock
        +Product(String id, String title, double price, int stock)
        +getId() String
        +getTitle() String
        +getPrice() double
        +getStock() int
        +setPrice(double price) void
        +setStock(int stock) void
        +reduceStock(int quantity) void
        +hasSufficientStock(int quantity) boolean
        +getDescription()* String
        +toString() String
    }

    class VideoGame {
        -String platform
        -String genre
        -String ageRating
        +VideoGame(String id, String title, double price, int stock, String platform, String genre, String ageRating)
        +getPlatform() String
        +getGenre() String
        +getAgeRating() String
        +getDescription() String
        +toString() String
    }

    class Console {
        -String brand
        -String model
        -String generation
        +Console(String id, String title, double price, int stock, String brand, String model, String generation)
        +getBrand() String
        +getModel() String
        +getGeneration() String
        +getDescription() String
        +toString() String
    }

    class SaleItem {
        -Product product
        -int quantity
        -double unitPrice
        +SaleItem(Product product, int quantity)
        +SaleItem(Product product, int quantity, double unitPrice)
        +getProduct() Product
        +getQuantity() int
        +getUnitPrice() double
        +getSubtotal() double
        +toString() String
    }

    class Sale {
        -String id
        -LocalDateTime dateTime
        -Customer customer
        -Seller seller
        -List~SaleItem~ items
        +Sale(String id, Customer customer, Seller seller, List~SaleItem~ items)
        +Sale(String id, LocalDateTime dateTime, Customer customer, Seller seller, List~SaleItem~ items)
        +getId() String
        +getDateTime() LocalDateTime
        +getCustomer() Customer
        +getSeller() Seller
        +getItems() List~SaleItem~
        +calculateTotal() double
        +getTotal() double
        +toString() String
    }

    Person <|-- Customer : extends
    Person <|-- Seller : extends
    Product <|-- VideoGame : extends
    Product <|-- Console : extends

    Sale "1" *-- "1..*" SaleItem : contains
    Sale "0..*" --> "1" Customer : references
    Sale "0..*" --> "1" Seller : attended by
    SaleItem "0..*" --> "1" Product : references

    %% ==========================================
    %% 2. PERSISTENCE LAYER (com.gamezone.persistence)
    %% ==========================================
    class PersonRepository {
        -String customersFilePath
        -String sellersFilePath
        +PersonRepository(String dataDirectory)
        +saveCustomer(Customer customer) void
        +findAllCustomers() List~Customer~
        +findCustomerById(String id) Customer
        +saveSeller(Seller seller) void
        +findAllSellers() List~Seller~
        +findSellerByEmployeeCode(String code) Seller
        +findSellerById(String id) Seller
        +seedDefaultSellers() void
    }

    class ProductRepository {
        -String productsFilePath
        +ProductRepository(String dataDirectory)
        +saveProduct(Product product) void
        +updateProduct(Product product) void
        +findAllProducts() List~Product~
        +findProductById(String id) Product
        +findAvailableProducts() List~Product~
    }

    class SaleRepository {
        -String salesFilePath
        -ProductRepository productRepository
        -PersonRepository personRepository
        +SaleRepository(String dataDirectory, ProductRepository productRepo, PersonRepository personRepo)
        +saveSale(Sale sale) void
        +findAllSales() List~Sale~
        +findSalesByCustomerId(String customerId) List~Sale~
        +findSalesBySellerCode(String employeeCode) List~Sale~
    }

    PersonRepository ..> Customer : persists
    PersonRepository ..> Seller : persists
    ProductRepository ..> Product : persists
    SaleRepository ..> Sale : persists

    %% ==========================================
    %% 3. SERVICE LAYER (com.gamezone.service)
    %% ==========================================
    class PersonService {
        -PersonRepository personRepository
        +PersonService(PersonRepository personRepository)
        +registerCustomer(String id, String fullName, String phone, String email) Customer
        +getAllCustomers() List~Customer~
        +getAllSellers() List~Seller~
        +findCustomerById(String id) Customer
        +findSellerByCode(String code) Seller
    }

    class ProductService {
        -ProductRepository productRepository
        +ProductService(ProductRepository productRepository)
        +registerVideoGame(String id, String title, double price, int stock, String platform, String genre, String ageRating) VideoGame
        +registerConsole(String id, String title, double price, int stock, String brand, String model, String generation) Console
        +getAllProducts() List~Product~
        +getAvailableProducts() List~Product~
        +findProductById(String id) Product
        +updateProductStock(String id, int quantityToDeduct) void
    }

    class SaleService {
        -SaleRepository saleRepository
        -ProductService productService
        -PersonService personService
        +SaleService(SaleRepository saleRepository, ProductService productService, PersonService personService)
        +registerSale(String customerId, String sellerEmployeeCode, List~SaleItem~ items) Sale
        +getAllSales() List~Sale~
        +getSalesByCustomer(String customerId) List~Sale~
        +getSalesBySeller(String employeeCode) List~Sale~
    }

    PersonService --> PersonRepository : delegates
    ProductService --> ProductRepository : delegates
    SaleService --> SaleRepository : delegates
    SaleService --> ProductService : coordinates
    SaleService --> PersonService : coordinates

    %% ==========================================
    %% 4. UI LAYER (com.gamezone.ui)
    %% ==========================================
    class ConsoleUI {
        -ProductService productService
        -PersonService personService
        -SaleService saleService
        -Scanner scanner
        +ConsoleUI(ProductService prS, PersonService peS, SaleService saS)
        +start() void
        -showMainMenu() void
        -handleRegisterVideoGame() void
        -handleRegisterConsole() void
        -handleListAvailableProducts() void
        -handleRegisterCustomer() void
        -handleListCustomers() void
        -handleListSellers() void
        -handleRegisterSale() void
        -handleListAllSales() void
        -handleListCustomerPurchases() void
        -handleListSellerSales() void
    }

    class Main {
        +main(String[] args)$ void
    }

    ConsoleUI --> ProductService : calls
    ConsoleUI --> PersonService : calls
    ConsoleUI --> SaleService : calls
    Main ..> ConsoleUI : bootstraps
```
