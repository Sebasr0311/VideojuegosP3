# Model Layer Hierarchy Diagram

This diagram documents the two primary inheritance hierarchies defined in the domain model (`com.gamezone.model`), differentiating between abstract base classes and concrete specializations.

```mermaid
classDiagram
    direction TB

    %% Person Hierarchy
    class Person {
        <<abstract>>
        #String id
        #String fullName
        #String phone
    }

    class Customer {
        -String email
    }

    class Seller {
        -String employeeCode
        -String shift
    }

    Person <|-- Customer : extends
    Person <|-- Seller : extends

    %% Product Hierarchy
    class Product {
        <<abstract>>
        #String id
        #String title
        #double price
        #int stock
        +getDescription()* String
        +reduceStock(int quantity) void
        +hasSufficientStock(int quantity) boolean
    }

    class VideoGame {
        -String platform
        -String genre
        -String ageRating
        +getDescription() String
    }

    class Console {
        -String brand
        -String model
        -String generation
        +getDescription() String
    }

    Product <|-- VideoGame : extends
    Product <|-- Console : extends
```

### Architectural Notes
- **Abstract Base Classes**: `Person` and `Product` are declared abstract to prevent direct instantiation of non-specific entities.
- **Polymorphism**: `Product` declares `getDescription()` as an abstract method (`*`), mandating concrete implementations in `VideoGame` and `Console`.
- **Inheritance vs Association**: Only genuine `is-a` generalizations are depicted in this diagram, fulfilling the requirement of maintaining two clear inheritance hierarchies.
