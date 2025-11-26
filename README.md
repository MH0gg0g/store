# Store – Modular Java Spring Boot E-Commerce REST API

A compact, well-structured **Java Spring Boot** REST API showcasing a production-grade backend for an online store. This project demonstrates modern architectural patterns, best practices, and enterprise-level features for e-commerce applications.

## 🎯 Key Features

### 🔐 Security & Authentication
- **JWT Token-Based Authentication** with access and refresh tokens
- **Role-Based Access Control (RBAC)** with USER and ADMIN roles
- **BCrypt Password Encryption** for secure credential storage
- **Stateless Session Management** for scalable API design
- **Method-Level Security** with `@PreAuthorize` annotations

### 🛍️ E-Commerce Capabilities
- **Product Catalog Management** – List, search, filter, and retrieve product details
- **Shopping Cart** – Add/update/remove items with real-time quantity management
- **Checkout Flow** – Complete checkout process that converts carts to orders
- **Order Management** – Track orders with status tracking (PENDING, PAID, FAILED, CANCELED)
- **User Account Management** – Registration, profile updates, password changes

### 🏗️ Architecture & Design Patterns
- **Layered Architecture** – Clean separation: Controllers → Services → Repositories
- **Data Transfer Objects (DTOs)** – Decouple API contracts from persistence models
- **MapStruct Mappers** – Automated, efficient object transformation
- **Aspect-Oriented Programming (AOP)** – Cross-cutting concerns for logging and performance tracking
- **Repository Pattern** – Abstract data access layer with Spring Data JPA

### 💾 Data Management
- **Flyway Database Migrations** – Versioned schema management with seed data
- **MySQL Database** – Relational data persistence
- **JPA/Hibernate ORM** – Object-relational mapping with lazy/eager loading
- **Redis Caching** – High-performance caching for products, users, and sessions

### 🚀 Production-Ready Features
- **Centralized Exception Handling** – Global `GlobalExceptionHandler` with consistent error responses
- **Request Validation** – Bean Validation with meaningful error messages
- **Performance Monitoring** – AOP-based performance tracking and execution time logging
- **Structured Logging** – SLF4J with Logback configuration for debugging and monitoring
- **OpenAPI/Swagger UI** – Auto-generated, interactive API documentation
- **Redis Integration** – Token blacklist for logout, distributed caching

---

## 📋 Project Structure

```
store/
├── src/main/java/com/example/store/
│   ├── aop/                          # Aspect-oriented programming
│   │   ├── Loggable.java            # Logging annotation
│   │   ├── LoggingAspect.java       # Logging implementation
│   │   ├── PerformanceTrackingAspect.java
│   │   └── TrackTime.java           # Performance annotation
│   │
│   ├── config/                       # Configuration beans
│   │   ├── JwtConfig.java           # JWT setup and token generation
│   │   ├── SecurityConfig.java      # Spring Security configuration
│   │   ├── RedisConfig.java         # Redis caching setup
│   │   ├── DataSeeder.java          # Initial data population
│   │   ├── CustomAuthenticationEntryPoint.java
│   │   └── CustomAccessDeniedHandler.java
│   │
│   ├── controllers/                  # REST endpoints
│   │   ├── AuthController.java      # Login, token refresh
│   │   ├── ProductController.java   # Product CRUD operations
│   │   ├── CartController.java      # Shopping cart operations
│   │   ├── CheckoutController.java  # Checkout flow
│   │   ├── OrderController.java     # Order management
│   │   ├── UserController.java      # User profile management
│   │   └── GlobalExceptionHandler.java
│   │
│   ├── services/                     # Business logic
│   │   ├── AuthService.java         # Authentication logic
│   │   ├── ProductService.java      # Product management
│   │   ├── CartService.java         # Cart operations
│   │   ├── CheckoutService.java     # Checkout workflow
│   │   ├── OrderService.java        # Order processing
│   │   ├── UserService.java         # User management
│   │   ├── JwtService.java          # Token generation/validation
│   │   └── CustomUserDetailsService.java
│   │
│   ├── entities/                     # JPA entities
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── Cart.java & CartItem.java
│   │   ├── Order.java & OrderItem.java
│   │   ├── Category.java
│   │   ├── Address.java
│   │   ├── Role.java                # User roles enum
│   │   └── OrderStatus.java         # Order status enum
│   │
│   ├── dtos/                         # Data Transfer Objects
│   │   ├── LoginRequest.java & LoginResponse.java
│   │   ├── ProductDto.java
│   │   ├── CartDto.java & CartItemDto.java
│   │   ├── OrderDto.java & OrderItemDto.java
│   │   ├── CheckoutRequest.java & CheckoutResponse.java
│   │   ├── UserDto.java
│   │   ├── ErrorDto.java
│   │   └── [Other DTOs...]
│   │
│   ├── mappers/                      # MapStruct mappers
│   │   ├── UserMapper.java
│   │   ├── ProductMapper.java
│   │   ├── CartMapper.java
│   │   ├── OrderMapper.java
│   │   └── [Other mappers...]
│   │
│   ├── repositories/                 # Data access layer
│   │   ├── UserRepository.java
│   │   ├── ProductRepository.java
│   │   ├── CartRepository.java
│   │   ├── OrderRepository.java
│   │   └── [Other repositories...]
│   │
│   ├── exceptions/                   # Custom exceptions
│   │   ├── ProductNotFoundException.java
│   │   ├── OrderNotFoundException.java
│   │   └── [Other exceptions...]
│   │
│   ├── filters/                      # Security filters
│   │   └── JwtAuthenticationFilter.java
│   │
│   ├── scheduler/                    # Scheduled tasks
│   │
│   └── StoreApplication.java         # Main Spring Boot application
│
├── src/main/resources/
│   ├── application.yaml              # Application configuration
│   ├── logback-spring.xml            # Logging configuration
│   └── db/migration/                 # Flyway migrations
│       ├── V1__initial_migration.sql
│       └── V2__populate_database.sql
│
├── src/test/java/                    # Unit & integration tests
└── pom.xml                           # Maven configuration
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 21+** – JDK installation
- **Maven 3.9+** – Build tool
- **MySQL 8.0+** – Database server
- **Redis** – Cache store (optional for local development)

## 📚 API Endpoints

### Authentication
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | `/api/auth/login` | Login with credentials | ❌ No |
| POST | `/api/auth/refresh` | Refresh JWT token | ❌ No |

### Products
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/api/products` | List all products | ❌ No |
| GET | `/api/products/{id}` | Get product details | ❌ No |
| POST | `/api/products` | Create new product | ✅ ADMIN |
| PUT | `/api/products/{id}` | Update product | ✅ ADMIN |
| DELETE | `/api/products/{id}` | Delete product | ✅ ADMIN |

### Shopping Cart
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| GET | `/api/cart` | View current cart | ✅ USER |
| POST | `/api/cart/items` | Add item to cart | ✅ USER |
| PUT | `/api/cart/items/{id}` | Update cart item | ✅ USER |
| DELETE | `/api/cart/items/{id}` | Remove cart item | ✅ USER |

### Checkout & Orders
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | `/api/checkout` | Process checkout | ✅ USER |
| GET | `/api/orders` | List user orders | ✅ USER |
| GET | `/api/orders/{id}` | Get order details | ✅ USER |

### Users
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | `/api/users` | Register new user | ❌ No |
| GET | `/api/users/{id}` | Get user profile | ✅ USER/ADMIN |
| PUT | `/api/users/{id}` | Update profile | ✅ USER/ADMIN |
| POST | `/api/users/{id}/change-password` | Change password | ✅ USER/ADMIN |

---

## 🔑 Authentication & Security

### JWT Token Structure
The application uses **JWT (JSON Web Tokens)** with the following claims:
- `sub` – User ID
- `role` – User role (USER or ADMIN)
- `exp` – Token expiration
- `iat` – Issued at timestamp
- `jti` – JWT ID (for blacklisting)

### Example Login Request
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

### Example Response
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

### Using the Token
Include the access token in subsequent requests:
```bash
curl -X GET http://localhost:8080/api/cart \
  -H "Authorization: Bearer <access_token>"
```

---

## 💻 Code Examples

### 1. Creating a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stock": 50,
    "categoryId": 1
  }'
```

### 2. Adding Item to Cart
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

### 3. Processing Checkout
```bash
curl -X POST http://localhost:8080/api/checkout \
  -H "Authorization: Bearer <user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "shippingAddressId": 1,
    "paymentMethod": "CREDIT_CARD"
  }'
```

---

## 🔄 Database Migrations

The project uses **Flyway** for database schema management:

- **V1__initial_migration.sql** – Creates all tables (users, products, orders, etc.)
- **V2__populate_database.sql** – Seeds initial data (admin user, sample products, categories)

Migrations are automatically applied on application startup.

---

## 🎨 Architecture Highlights

### Layered Architecture
```
Controllers (HTTP Layer)
    ↓
Services (Business Logic)
    ↓
Repositories (Data Access)
    ↓
Database (Persistence)
```

### Request Flow Example
1. Client sends POST request to `/api/cart/items`
2. `CartController` receives the request
3. `CartService` validates and processes the business logic
4. `CartRepository` persists data to the database
5. Response is mapped to `CartItemDto` via `CartMapper`
6. JSON response is returned to the client

### Caching Strategy
- Products are cached in Redis with key `products:item:{productId}`
- Product list is cached as `products:list`
- Cache is automatically invalidated when products are updated/deleted
- User data cached with 1-hour TTL

---

## 📊 Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Java** | Java Development Kit | 21 |
| **Framework** | Spring Boot | 3.5.6 |
| **Security** | Spring Security + JWT | JJWT 0.12.6 |
| **Database** | MySQL | 8.0+ |
| **ORM** | JPA/Hibernate | 6.4+ |
| **Migration** | Flyway | Latest |
| **Cache** | Redis | 7.0+ |
| **Mapping** | MapStruct | 1.6.3 |
| **API Docs** | SpringDoc OpenAPI | 2.8.8 |
| **Build** | Maven | 3.9+ |
| **Testing** | JUnit 5 + Spring Test | Latest |

---

## 🌟 Key Design Decisions

1. **Stateless Authentication** – JWT tokens eliminate need for session storage, enabling horizontal scaling
2. **DTO Layer** – Separates internal data model from API contract, allowing safe evolution
3. **Redis Caching** – Reduces database load for frequently accessed data
4. **AOP for Cross-Cutting Concerns** – Logging and performance tracking without cluttering business logic
5. **Flyway Migrations** – Deterministic, version-controlled schema changes
6. **Method-Level Security** – Fine-grained access control directly on service methods

---

## 📝 Configuration

### application.yaml Settings
```yaml
spring:
  jwt:
    secret: <your-secret-key>              # JWT signing secret
    accessTokenExpiration: 9999             # Access token lifetime (seconds)
    refreshTokenExpiration: 420             # Refresh token lifetime (seconds)
    jwtBlacklistprefix: jwt:blacklist-      # Redis key prefix for blacklist
  
  cache:
    type: redis                             # Cache provider
  
  datasource:
    url: jdbc:mysql://localhost:3306/store
    username: root
    password: root

logging:
  level:
    com.example.store: TRACE                # Application log level
  file:
    name: ./logs/App.log                    # Log file location
```

### JWT Token Expired
- Use the refresh token endpoint to get a new access token
- Token expiration is configurable in `application.yaml`

---

**Built with ❤️ using Spring Boot**
