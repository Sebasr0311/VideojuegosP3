# Object-Oriented Analysis and Architectural Design

## Academic Context
- **Course**: Programación III
- **Institution**: Universidad Popular del Cesar (Valledupar, Colombia)
- **Project**: GameZoneUnicesar Information System
- **Topic**: Object-Oriented Programming, Layered Architecture, and Git Version Control

---

## 1. People in the System

### Question 1: Common and Specific Attributes in the Hierarchy
**Question**: *What attributes are common to all people who interact with the store, and which are specific to each specific type of person? How is this distinction reflected in a class hierarchy?*

**Analysis and Design Decision**:
- **Common Attributes**: All physical individuals who interact with GameZone Unicesar share general identification and contact data:
  - `id` (`String`): National legal identification number (Cédula de Ciudadanía or document ID).
  - `fullName` (`String`): Full personal name.
  - `phone` (`String`): Primary contact telephone number.
- **Specific Attributes**:
  - `Customer`: Represents end-users who purchase products. Requires `email` (`String`) for communication and digital receipts, and a record/reference of purchase history.
  - `Seller`: Represents internal store staff responsible for attending customers and processing sales. Requires `employeeCode` (`String`) for corporate identification and `shift` (`String` - e.g., Morning, Afternoon, Night) indicating working schedule.
- **Hierarchy Representation**:
  An abstract base class `Person` is defined to encapsulate the common state (`id`, `fullName`, `phone`) and shared methods (accessors and baseline representation). Specializations `Customer` and `Seller` inherit from `Person` using the `extends` keyword (`is-a` relationship). This guarantees DRY (Don't Repeat Yourself), avoids data duplication, and enables polymorphic references when handling general personnel.

---

### Question 2: Generic Person Representation and Instantiation
**Question**: *Should there be a class that represents a "generic person" without specifying their role? Why or why not? What implication does this decision have on the possibility of instantiating said class?*

**Analysis and Design Decision**:
- In the business domain of GameZone Unicesar, a "generic person" does not exist as an operational entity. Every human being interacting with the system acts strictly under a defined business role: either as a customer acquiring video games/consoles or as an employee (seller) executing transactions.
- Consequently, having a concrete, instantiable `Person` would introduce domain inconsistency (an entity with no operational purpose or role permissions).
- **Design Implication**: The `Person` class must be explicitly declared with the `abstract` modifier. Declaring it abstract formally prevents direct instantiation via `new Person(...)` at compile time, forcing developers to instantiate only concrete, role-specific subclasses (`Customer` or `Seller`), while still allowing polymorphic type references (`Person p = new Customer(...)`).

---

## 2. Products in the System

### Question 3: Common and Specialized Product Characteristics
**Question**: *What characteristics do all the products marketed by the store have in common, regardless of their type? What characteristics are specific to each type of product?*

**Analysis and Design Decision**:
- **Common Characteristics**: Every commercial item sold in GameZone Unicesar has basic commercial and inventory properties:
  - `id` (`String`): Unique alphanumeric SKU / product code.
  - `title` (`String`): Commercial display name / title.
  - `price` (`double`): Base unit selling price (must be positive).
  - `stock` (`int`): Quantity of units currently available in inventory (must be non-negative).
- **Specialized Characteristics**:
  - `VideoGame`: Characterized by software entertainment dimensions:
    - `platform` (`String`): The target hardware environment (e.g., PlayStation 5, Xbox Series X, Nintendo Switch, PC).
    - `genre` (`String`): Categorical genre (e.g., Action-Adventure, RPG, Sports, Racing).
    - `ageRating` (`String`): Recommended age classification / rating (e.g., Everyone, Teen, Mature 17+ / ESRB rating).
  - `Console`: Characterized by hardware manufacturing dimensions:
    - `brand` (`String`): Manufacturer / hardware brand (e.g., Sony, Microsoft, Nintendo).
    - `model` (`String`): Hardware revision / edition (e.g., Slim, Pro, OLED, Digital Edition).
    - `generation` (`String`): Architectural console generation (e.g., 9th Generation, 8th Generation).

---

### Question 4: Polymorphic Descriptions via Abstract Base Methods
**Question**: *Each type of product must be able to present a description that integrates its particular characteristics. How should this behavior be declared in the base class to guarantee that all subclasses implement it in their own way? What OOP mechanism allows this?*

**Analysis and Design Decision**:
- **Declaration in Base Class**:
  The base class `Product` declares an abstract method:
  ```java
  public abstract String getDescription();
  ```
- **OOP Mechanism**:
  This leverages **dynamic polymorphism** backed by **method overriding** (`@Override`). Because `getDescription()` is declared `abstract` inside an abstract class without a body, the Java compiler strictly enforces that any non-abstract subclass (`VideoGame` and `Console`) must provide a concrete implementation tailored to its specific fields.
- **Subclass Implementation**:
  - `VideoGame.getDescription()` outputs a formatted string combining title, price, platform, genre, and age rating.
  - `Console.getDescription()` outputs a formatted string combining title, price, brand, model, and generation.
  - Client code (services, console UI) can invoke `.getDescription()` on any `Product` reference without needing to inspect runtime types or perform downcasting with `instanceof`.

---

## 3. Sales and Relationships between Entities

### Question 5: Entity Relationships in the Sales Domain
**Question**: *A sale involves a customer, a seller, and one or more products. What type of relationships exist between the class that represents the sale and the other classes in the system? Are these relationships inheritance, association, composition, or another type? Justify.*

**Analysis and Design Decision**:
- **Sale to Customer (`Association - Aggregation / Reference`)**:
  - Multiplicity: `1` Customer to `0..*` Sales.
  - Justification: A sale references the customer who made the purchase. It is an association relationship. The customer exists independently of the sale: deleting or modifying a customer does not destroy historical transaction records, and creating a customer does not mandate immediate sales.
- **Sale to Seller (`Association - Aggregation / Reference`)**:
  - Multiplicity: `1` Seller to `0..*` Sales.
  - Justification: A sale references the employee who attended the transaction. Similar to Customer, the seller has an autonomous lifecycle independent of any single sale record.
- **Sale to SaleItem (`Composition`)**:
  - Multiplicity: `1` Sale contains `1..*` `SaleItem` instances.
  - Justification: A `SaleItem` (representing a line item with a product reference, unit price, quantity, and line subtotal) has no autonomous existence outside of its parent `Sale`. If a sale record is discarded or cancelled, its internal line items cease to exist. This constitutes strict **Composition** (strong ownership and coincidental lifecycle).
- **SaleItem to Product (`Aggregation / Association`)**:
  - Multiplicity: `0..*` `SaleItem` instances reference `1` `Product`.
  - Justification: A line item refers to an existing product in the catalog. Products exist in the catalog regardless of whether they have been included in sales items.

---

### Question 6: Responsibility for Total Calculation
**Question**: *Should the sale be responsible for calculating its own total, or should this responsibility fall on another class? Argue your decision.*

**Analysis and Design Decision**:
- Under the **Information Expert** principle (GRASP), responsibility for calculating information should be assigned to the class that possesses all the required data to perform that calculation.
- The `Sale` aggregate root contains the complete collection of purchased items (`SaleItem`), each knowing its product, quantity, and unit price. Therefore, `Sale` is the natural Information Expert:
  ```java
  public double calculateTotal() {
      double total = 0.0;
      for (SaleItem item : this.items) {
          total += item.getSubtotal();
      }
      return total;
  }
  ```
- Delegating this calculation to an external service would create an anemic domain model and leak domain invariants. Placing the computation inside `Sale` (and line item subtotal inside `SaleItem`) ensures high cohesion, encapsulation, and domain integrity.

---

## 4. Business Rules and Invariants

### Question 7: Guaranteeing Minimum One Product per Sale
**Question**: *How does the design guarantee that a sale cannot be registered without at least one product? At what point in the system should this rule be validated?*

**Analysis and Design Decision**:
- **Multi-Level Defense in Depth**:
  1. **Domain Invariant (Entity level)**:
     The constructor or registration method of `Sale` guards its invariant by verifying `items != null && !items.isEmpty()`. If attempted without items, it rejects construction with an `IllegalArgumentException` or `IllegalStateException`.
  2. **Service Layer Validation (Application level)**:
     `SaleService.registerSale(...)` inspects the incoming list of items before executing any stock adjustments or persistence. If the item list is empty, it aborts the operation throwing a domain validation exception (`EmptySaleException` or `IllegalArgumentException`) before modifying inventory.
  3. **User Interface Guard (Presentation level)**:
     The CLI menu loops while asking the user to add items, requiring at least one valid item before confirming the checkout.

---

### Question 8: Automatic Inventory Deduction and Involved Classes
**Question**: *How is the automatic update of the inventory reflected in the design when a sale is registered? What classes are involved in this operation?*

**Analysis and Design Decision**:
- **Flow of Execution**:
  1. The user finalizes a sale transaction via `ConsoleUI`.
  2. `ConsoleUI` calls `SaleService.registerSale(customerId, sellerId, items)`.
  3. `SaleService` coordinates with `ProductService` / `ProductRepository` to:
     - Verify current stock levels for each requested product (`product.getStock() >= requestedQty`).
     - Atomically deduct the stock using `product.reduceStock(requestedQty)`.
     - Persist the updated product state back to `ProductRepository`.
  4. Once inventory is successfully updated, `SaleService` constructs the `Sale` entity, calculates the total, and delegates persistence to `SaleRepository`.
- **Involved Classes**:
  - `SaleService`: Coordinates transaction logic, stock verification, and orchestration.
  - `Product`: Domain entity executing the stock reduction (`reduceStock(qty)`).
  - `ProductRepository`: Persists updated inventory to storage.
  - `Sale`: Represents the finalized transaction.
  - `SaleRepository`: Persists the registered sale to storage.

---

## 5. Layered Architecture and Separation of Concerns

### Question 9: 4-Layer System Organization and Placement Criteria
**Question**: *The system must be organized into four layers: model, persistence, services, and user interface. What type of classes belong to each layer? What criterion allows deciding in which layer a class should be located?*

**Analysis and Design Decision**:
1. **Model Layer (`com.gamezone.model`)**:
   - *Classes*: Entities, value objects, and domain invariants (`Person`, `Customer`, `Seller`, `Product`, `VideoGame`, `Console`, `Sale`, `SaleItem`).
   - *Criterion*: Classes that represent core business concepts, independent of technological details, user interfaces, or storage mechanisms.
2. **Persistence Layer (`com.gamezone.persistence`)**:
   - *Classes*: Repositories and file data access objects (`PersonRepository`, `ProductRepository`, `SaleRepository`).
   - *Criterion*: Classes responsible for persisting, serializing, reading, and writing domain entities to and from files.
3. **Service Layer (`com.gamezone.service`)**:
   - *Classes*: Application use-case coordinators (`PersonService`, `ProductService`, `SaleService`).
   - *Criterion*: Classes that coordinate workflows, validate business rules across multiple domain aggregates, and mediate between UI and persistence.
4. **User Interface Layer (`com.gamezone.ui`)**:
   - *Classes*: Console views, menus, and input handlers (`ConsoleUI`, `InputHelper`).
   - *Criterion*: Classes that handle human interaction, parse CLI input, display output formatting, and invoke service use-cases.

---

### Question 10: Prohibition of Persistence Logic in Domain Classes
**Question**: *Why should the logic of saving and retrieving data from files not be inside domain classes? What problems are generated when these responsibilities are mixed?*

**Analysis and Design Decision**:
- Mixing file I/O into domain entities directly violates the **Single Responsibility Principle (SRP)** and the principle of **Separation of Concerns (SoC)**.
- **Problems Caused by Mixing**:
  1. **High Coupling to Storage Technology**: If `Product` directly wrote to CSV or text files, changing storage formats (e.g., to JSON, SQLite, or PostgreSQL) would require modifying core business logic.
  2. **Untestable Domain Logic**: Unit testing domain invariants would mandate filesystem access, slowing down tests and introducing environmental flakiness.
  3. **Loss of Portability**: Domain entities could not be reused in other contexts (e.g., web services, microservices, mobile clients) without carrying file system baggage.
  4. **Violation of Encapsulation and Lifecycle Mismatches**: Entities represent state in memory; file repositories manage persistence life cycles. Conflating the two creates God Objects.

---

### Question 11: Allowed and Prohibited Dependencies between Layers
**Question**: *What dependencies are allowed between the layers and which are prohibited? Justify the direction of the allowed dependencies.*

**Analysis and Design Decision**:
- **Allowed Dependencies**:
  - `UI -> Service`: The user interface interacts exclusively with services to execute business actions.
  - `Service -> Model`: Services manipulate domain entities and enforce domain rules.
  - `Service -> Persistence`: Services coordinate data fetching and persisting through repositories.
  - `Persistence -> Model`: Repositories reconstitute domain entities from files and serialize entities to storage.
  - `Model -> None`: The domain model has zero outgoing dependencies on any other layer. It is pure Java.
- **Prohibited Dependencies**:
  - `UI -> Persistence`: Bypassing services violates business validation rules and leaks data access concerns to the presentation.
  - `UI -> Model (Direct mutation)`: UI must not bypass application services to alter business state.
  - `Model -> Persistence / Service / UI`: Domain classes must never import repositories, services, or UI components.
  - `Persistence -> Service / UI`: Data access components must never depend on higher-level use cases or presentation logic.
- **Justification**:
  This unidirectional inward dependency rule conforms to Clean and Hexagonal Architecture principles. Changes in outer layers (such as migrating from CLI to Web, or changing file formats) have zero cascading impact on the inner core business rules.
