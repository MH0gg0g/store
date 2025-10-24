# Store – Modular Java Spring Boot E‑commerce Backend

A compact, well-structured Java Spring Boot sample REST API demonstrating a production-grade backend for a simple online store. This repository is designed to showcase architecture, patterns, and capabilities you’d expect in a modern e‑commerce service: secure JWT-based auth, product catalog, shopping cart, checkout and orders, Redis caching, Flyway migrations, DTO mapping, layered services, and clean controller APIs.

## Highlights & Selling Points

- Robust authentication and authorization using JWT tokens with a dedicated `JwtConfig` and `SecurityConfig`.
- Complete cart and checkout flow: add/update cart items, view cart, run checkout which creates orders and order items.
- Well-separated layers (controllers, services, repositories, mappers, DTOs) making the code easy to extend and maintain.
- Redis integration for caching/session support (see `RedisConfig`).
- Database migrations and seeded data using Flyway migrations in `src/main/resources/db/migration`.
- Global exception handling and consistent error DTOs for predictable API behavior.
- Comprehensive DTOs and mappers that isolate internal entities from API contracts, enabling safe API evolution.
- Test scaffolding (unit/integration tests) and a clean Maven build so the project is ready to be used as a starter or reference.

## Core Capabilities (What this app can do)

- Authentication & Authorization
  - Login with credentials; application returns JWT tokens (see `AuthController`, `JwtResponse`, `LoginRequest`).
  - Security configuration enforces protected endpoints, role-based access and token validation (see `SecurityConfig`, `JwtConfig`).

- Product Catalog
  - REST endpoints for listing, searching, and retrieving product details (see `ProductController`, `ProductDto`).

- Shopping Cart
  - Add items to cart, update quantities, remove items, and fetch cart contents (see `CartController`, `AddItemToCartRequest`, `updateCartItemRequest`, `CartDto`).

- Checkout & Orders
  - Checkout process turns a cart into an order with items and user shipping address handling (see `CheckoutController`, `CheckoutRequest`, `OrderController`, `OrderDto`).

- Persistence & Data Migration
  - Relational persistence via JPA entities in `src/main/java/com/example/store/entities`.
  - Database schema and seed data applied via Flyway migrations located at `src/main/resources/db/migration/V1__initial_migration.sql` and `V2__populate_database.sql`.

- Caching / Session Store
  - Redis configuration and wiring in `src/main/java/com/example/store/config/RedisConfig` to support caching and scalable session/data access patterns.

- Error Handling & Validation
  - Centralized errors with `GlobalExceptionHandler` and `ErrorDto`, providing consistent HTTP responses for failures.

## Architecture & Design Notes

- Layered architecture: controllers -> services -> repositories. This separation keeps business logic testable and controllers thin.
- DTOs completely separate wire-format from entities. This reduces accidental coupling between API and persistence models.
- Mappers (generated sources present under `generated-sources`) centralize object transformations, simplifying future API changes.
- Security is handled at configuration level with JWT filters and a dedicated config class, allowing token, expiry, and signing algorithm changes without touching controllers.
- Flyway provides reliable deterministic schema migrations and seed data to recreate development/test environments easily.

## Example API Surface (high-level)

These examples illustrate the kinds of endpoints implemented (see controllers for exact routes and payloads):

- POST /api/auth/login — obtain JWT token (LoginRequest -> JwtResponse)
- GET /api/products — list/search products (returns `ProductDto` list)
- GET /api/products/{id} — product details
- POST /api/cart/items — add item to cart (`AddItemToCartRequest`)
- PUT /api/cart/items/{id} — update cart item (`updateCartItemRequest`)
- GET /api/cart — view cart (`CartDto`)
- POST /api/checkout — perform checkout (`CheckoutRequest` -> `CheckoutResponse`)
- GET /api/orders — list user orders (`OrderDto`)

These endpoints demonstrate a realistic e-commerce workflow (auth -> browse -> cart -> checkout -> orders).

## Key details

- Java: 17
- Spring Boot parent: 3.5.6
- Build: Maven
- Main class: `com.example.store.StoreApplication`
- Database: MySQL (Flyway migrations under `src/main/resources/db/migration`)
- Cache: - Redis (for cache and token blacklist)
- Auth: JWT (see `src/main/resources/application.yaml` for defaults)
- API docs: springdoc OpenAPI (Swagger UI)

# API documentation

OpenAPI (Swagger UI) is available when the app is running. Common URLs:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`