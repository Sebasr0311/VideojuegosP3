# GameZone Unicesar - Sistema de Información

**Universidad Popular del Cesar (UPC)**  
**Facultad de Ingenierías y Tecnologías - Ingeniería de Sistemas**  
**Asignatura**: Programación III  
**Taller 2**: Programación Orientada a Objetos, Arquitectura en Capas y Control de Versiones con Git  

---

## 1. Contexto del Negocio

**GameZone Unicesar** es una tienda de videojuegos y consolas ubicada en el sector universitario de Valledupar, Colombia. La aplicación sistematiza la gestión de inventario, personal (clientes y vendedores) y el registro transaccional de ventas con descuento automático de inventario y persistencia continua en archivos locales.

---

## 2. Arquitectura del Sistema (4 Capas)

El sistema está estrictamente diseñado bajo el modelo de **Arquitectura en Capas**, garantizando alta cohesión y bajo acoplamiento con dependencias unidireccionales:

```
[ Capa de Presentación (com.gamezone.ui) ]
                  │
                  ▼
[ Capa de Servicios (com.gamezone.service) ]
         │                        │
         ▼                        ▼
[ Capa de Modelo (com.gamezone.model) ] ◄─── [ Capa de Persistencia (com.gamezone.persistence) ]
```

1. **Capa de Modelo (`com.gamezone.model`)**:
   - Contiene las entidades puras del dominio, reglas de encapsulamiento (`private`) e invariantes de negocio.
   - **Jerarquía de Personas**: `Person` (abstracta), `Customer` y `Seller`.
   - **Jerarquía de Productos**: `Product` (abstracta con método polimórfico `getDescription()`), `VideoGame` y `Console`.
   - **Agregado de Ventas**: `Sale` y `SaleItem` (composición y cálculo dinámico del total).
   - Totalmente desacoplada de la interfaz gráfica y de mecanismos de almacenamiento.

2. **Capa de Persistencia (`com.gamezone.persistence`)**:
   - Responsable exclusiva de la lectura, escritura y serialización en archivos de texto plano dentro de la carpeta `data/`.
   - Repositorios: `PersonRepository`, `ProductRepository` y `SaleRepository`.
   - Incluye mecanismo de precarga automática de vendedores (`seedDefaultSellers`).

3. **Capa de Servicios (`com.gamezone.service`)**:
   - Orquesta la lógica de negocio y los casos de uso del sistema.
   - Servicios: `PersonService`, `ProductService` y `SaleService`.
   - Valida reglas de integridad: unicidad de identificadores, stock suficiente antes de ventas y actualización atómica del inventario.

4. **Capa de Interfaz de Usuario (`com.gamezone.ui`)**:
   - `ConsoleUI`: Interfaz de línea de comandos interactiva y amigable.
   - Interactúa exclusivamente con la Capa de Servicios, sin saltarse reglas arquitectónicas.
   - `Main`: Punto de entrada (`main`), ensamblador e inyector de dependencias.

---

## 3. Funcionalidades del Sistema (10 Operaciones)

El sistema cubre al 100% las 10 operaciones requeridas:

| Opción | Operación Funcional | Descripción |
| :---: | :--- | :--- |
| **1** | **Registrar un nuevo videojuego** | Registra SKU, título, precio, stock, plataforma, género y clasificación de edad. |
| **2** | **Registrar una nueva consola** | Registra SKU, título, precio, stock, marca, modelo y generación. |
| **3** | **Listar productos disponibles** | Consulta y muestra productos que cuentan con stock superior a 0 unidades. |
| **4** | **Registrar un nuevo cliente** | Registra cédula/ID, nombre completo, teléfono y correo electrónico validado. |
| **5** | **Listar clientes registrados** | Muestra el listado de todos los clientes almacenados en persistencia. |
| **6** | **Listar vendedores registrados** | Muestra los vendedores del sistema (incluyendo los 3 precargados). |
| **7** | **Registrar una nueva venta** | Selecciona cliente, vendedor y uno o más productos con validación y descuento de stock. |
| **8** | **Consultar historial completo de ventas** | Muestra todas las ventas registradas con fecha, cliente, vendedor, items y total. |
| **9** | **Historial de compras por cliente** | Filtra y presenta las compras realizadas por un cliente específico. |
| **10** | **Historial de ventas por vendedor** | Filtra y presenta las ventas atendidas por un vendedor según su código de empleado. |
| **0** | **Salir del sistema** | Finaliza la ejecución de la aplicación. |

---

## 4. Estructura del Repositorio

```
VideojuegosP3/
├── README.md                      # Documentación general y manual de usuario
├── TEAM.md                        # Estructura del equipo, roles y ramas feature
├── pom.xml                        # Configuración de Maven y plugins de empaquetado
├── .gitignore                     # Exclusión de binarios, IDEs y datos de ejecución
├── src/
│   ├── main/
│   │   ├── java/com/gamezone/
│   │   │   ├── model/             # Entidades del dominio (Person, Customer, Seller, Product, VideoGame, Console, Sale, SaleItem)
│   │   │   ├── persistence/       # Acceso a archivos (PersonRepository, ProductRepository, SaleRepository)
│   │   │   ├── service/           # Lógica de aplicación (PersonService, ProductService, SaleService)
│   │   │   ├── ui/                # Interfaz de consola interactiva (ConsoleUI)
│   │   │   └── Main.java          # Punto de entrada de la aplicación
│   │   └── data/
│   │       └── sellers.txt        # Datos precargados iniciales de vendedores
│   └── test/java/com/gamezone/    # Pruebas unitarias e integración (14 pruebas automatizadas)
│       ├── service/               # Pruebas de servicios (PersonServiceTest, ProductServiceTest, SaleServiceTest)
│       └── ui/                    # Pruebas end-to-end de interfaz (ConsoleUITest)
└── docs/
    ├── analysis.md                # Preguntas orientadoras del análisis en inglés
    ├── hierarchy-diagram.md       # Diagrama Mermaid de jerarquías de herencia del dominio
    ├── class-diagram.md           # Diagrama Mermaid UML de clases completo con 4 capas
    └── layers-diagram.md          # Diagrama Mermaid arquitectónico de flujo de capas
```

---

## 5. Requisitos y Ejecución

### Requisitos Previos
- **Java Development Kit (JDK)**: Java 8 o superior (probado en Java 8, 17 y 21).
- **Apache Maven**: 3.6 o superior.

### Compilación y Ejecución de Pruebas
Para compilar el proyecto y ejecutar la suite completa de pruebas automatizadas:
```bash
mvn clean test
```

### Ejecutar la Aplicación Directamente con Maven
```bash
mvn compile exec:java
```

### Generar el JAR Ejecutable y Correrlo
```bash
mvn clean package
java -jar target/gamezone-unicesar-1.0.0-SNAPSHOT.jar
```

---

## 6. Documentación de Diseño y Diagramas
Para consultar el análisis detallado y los diagramas arquitectónicos en formato Mermaid:
- [Análisis Orientado a Objetos (en inglés)](docs/analysis.md)
- [Diagrama de Jerarquías de Dominio](docs/hierarchy-diagram.md)
- [Diagrama Completo de Clases UML](docs/class-diagram.md)
- [Diagrama de Arquitectura en Capas](docs/layers-diagram.md)
- [Organización del Equipo y Git Flow](TEAM.md)