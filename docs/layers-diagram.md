# Layered Architecture Diagram

This diagram specifies the architectural organization into four strict layers and details the allowed and prohibited dependency flows across the system.

```mermaid
flowchart TD
    subgraph UI_Layer ["Presentation Layer: com.gamezone.ui"]
        UI["ConsoleUI / Main CLI"]
    end

    subgraph Service_Layer ["Service Layer: com.gamezone.service"]
        PS["PersonService"]
        PrS["ProductService"]
        SS["SaleService"]
    end

    subgraph Persistence_Layer ["Persistence Layer: com.gamezone.persistence"]
        PR["PersonRepository"]
        PrR["ProductRepository"]
        SR["SaleRepository"]
    end

    subgraph Model_Layer ["Domain Model Layer: com.gamezone.model"]
        PersonModels["Person, Customer, Seller"]
        ProductModels["Product, VideoGame, Console"]
        SaleModels["Sale, SaleItem"]
    end

    %% Allowed Dependencies
    UI -->|"uses"| Service_Layer
    Service_Layer -->|"coordinates"| Persistence_Layer
    Service_Layer -->|"manipulates"| Model_Layer
    Persistence_Layer -->|"hydrates / stores"| Model_Layer

    %% Forbidden / Disallowed Relationships (Styling Annotation)
    classDef allowed fill:#e1f5fe,stroke:#0288d1,stroke-width:2px;
    classDef prohibited fill:#ffebee,stroke:#d32f2f,stroke-width:2px,stroke-dasharray: 5 5;

    class UI,PS,PrS,SS,PR,PrR,SR,PersonModels,ProductModels,SaleModels allowed;
```

## Dependency Rules Matrix

| Source Layer | Target Layer | Status | Justification |
| :--- | :--- | :--- | :--- |
| **UI** | **Service** | **ALLOWED** | UI orchestrates use cases by invoking application services. |
| **UI** | **Persistence** | <span style="color:red">**PROHIBITED**</span> | Presentation must never bypass business validation and access storage directly. |
| **UI** | **Model** | Restricted | Read-only presentation of DTOs/Entities; no direct state mutation. |
| **Service** | **Model** | **ALLOWED** | Services enforce business logic and manipulate domain entities. |
| **Service** | **Persistence** | **ALLOWED** | Services invoke repositories to persist and retrieve domain aggregates. |
| **Persistence** | **Model** | **ALLOWED** | Repositories serialize and deserialize domain entities to/from data files. |
| **Persistence** | **Service / UI** | <span style="color:red">**PROHIBITED**</span> | Lower-level data access components must have zero knowledge of callers. |
| **Model** | **Any Layer** | <span style="color:red">**PROHIBITED**</span> | Domain core is pure Java, completely independent of frameworks, files, or UI. |
