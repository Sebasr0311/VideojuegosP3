# Equipo de Desarrollo - GameZoneUnicesar

## Información del Proyecto
- **Institución**: Universidad Popular del Cesar (Valledupar - Cesar, Colombia)
- **Programa**: Ingeniería de Sistemas
- **Asignatura**: Programación III
- **Proyecto**: Taller 2 - GameZoneUnicesar
- **Modalidad**: Desarrollo Individual / Roles Asumidos por Módulos Verticales

---

## Integrantes y Roles

| Integrante | Código Estudiantil | Correo Institucional | Rol Principal | Módulo Asignado |
| :--- | :--- | :--- | :--- | :--- |
| **Juan Sebastian Rincón Farelo** | *Autor Único* | juansebastianrincon@unicesar.edu.co | **Líder Técnico** | Módulo de Ventas, Integración, UI Consola y Docs |
| **Juan Sebastian Rincón Farelo** | *Autor Único* | juansebastianrincon@unicesar.edu.co | **Desarrollador 1** | Módulo de Productos (Inventario, Videojuegos y Consolas) |
| **Juan Sebastian Rincón Farelo** | *Autor Único* | juansebastianrincon@unicesar.edu.co | **Desarrollador 2** | Módulo de Personas (Clientes y Vendedores) |

*Nota: Por disposición del estudiante y modalidad individual de trabajo, todos los roles del corte vertical fueron asumidos e implementados de manera integral por Juan Sebastian Rincón Farelo, respetando la separación de ramas feature, commits atómicos y pull requests exigidos en la rúbrica.*

---

## Distribución de Responsabilidades por Rol

### 1. Líder Técnico (Ventas, Integración, UI y Documentación)
- **Actividades Comprometidas**:
  - Administración del repositorio, configuración de Maven y Git Flow.
  - Redacción del análisis en inglés (`docs/analysis.md`) y diagramas UML Mermaid (`docs/hierarchy-diagram.md`, `docs/class-diagram.md`, `docs/layers-diagram.md`).
  - Implementación del modelo de Ventas: `SaleItem`, `Sale` con cálculo dinámico de total y validación de cantidad mínima.
  - Implementación de la persistencia de ventas: `SaleRepository` con almacenamiento en archivos.
  - Implementación del servicio de ventas: `SaleService` con descuento automático de inventario y consultas por cliente/vendedor.
  - Implementación de la interfaz de consola interactiva (`ConsoleUI`) cubriendo las 10 operaciones funcionales.
  - Implementación de la clase de arranque `Main` con inyección de dependencias y precarga de vendedores.
  - Revisión, aprobación e integración de Pull Requests en `develop` y pase final a `main`.
- **Ramas Feature Asignadas**:
  - `feature/analysis-and-diagrams`
  - `feature/sale-module`
  - `feature/ui-console`
  - `feature/integration-release`

### 2. Desarrollador 1 (Módulo de Productos)
- **Actividades Comprometidas**:
  - Implementación de la jerarquía de productos: clase base abstracta `Product` con método polimórfico `getDescription()`, y clases derivadas `VideoGame` y `Console`.
  - Implementación de persistencia de productos: `ProductRepository` para lectura y escritura en archivos (`data/products.txt`).
  - Implementación de la lógica de negocio: `ProductService` con validaciones de precios, stock no negativo y reducción de existencias.
  - Pruebas unitarias de catálogo y existencias.
- **Rama Feature Asignada**:
  - `feature/product-module`

### 3. Desarrollador 2 (Módulo de Personas)
- **Actividades Comprometidas**:
  - Implementación de la jerarquía de personas: clase base abstracta `Person`, y clases derivadas `Customer` y `Seller`.
  - Implementación de persistencia de personas: `PersonRepository` para clientes (`data/customers.txt`) y vendedores (`data/sellers.txt`).
  - Implementación de la lógica de negocio: `PersonService` con registro y validación de clientes y consulta de vendedores.
  - Sembrado de vendedores iniciales precargados.
  - Pruebas unitarias del módulo de personas.
- **Rama Feature Asignada**:
  - `feature/person-module`

---

## Convenciones de Trabajo y Git Flow
- **Ramas Base**: `main` (producción/estable) y `develop` (integración continua).
- **Ramas Feature**: Nombres con prefijo `feature/<modulo>`, integradas exclusivamente mediante Pull Requests.
- **Mensajes de Commit**: Formato *Conventional Commits* en inglés (`feat:`, `fix:`, `docs:`, `test:`, `refactor:`, `chore:`).
