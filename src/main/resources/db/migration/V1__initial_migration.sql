create table
    users (
        id BIGINT AUTO_INCREMENT NOT NULL,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        role VARCHAR(20) NOT NULL DEFAULT 'USER',
        PRIMARY KEY (id)
    );

create table
    addresses (
        id BIGINT AUTO_INCREMENT NOT NULL,
        street VARCHAR(255) NOT NULL,
        city VARCHAR(255) NOT NULL,
        user_id BIGINT NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    categories (
        id TINYINT AUTO_INCREMENT NOT NULL,
        name VARCHAR(255) NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    products (
        id BIGINT AUTO_INCREMENT NOT NULL,
        name VARCHAR(255) NOT NULL,
        price BIGINT NOT NULL,
        category_id TINYINT,
        description TEXT NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE
    carts (
        id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
        date_created DATE NOT NULL DEFAULT (CURDATE()),
        PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE
    cart_items (
        id BIGINT AUTO_INCREMENT NOT NULL,
        cart_id BINARY(16) NOT NULL,
        product_id BIGINT NOT NULL,
        quantity INT NOT NULL DEFAULT 1,
        PRIMARY KEY (id),
        CONSTRAINT `unique_cart_product` UNIQUE (cart_id, product_id)
    );

CREATE TABLE
    orders (
        id BIGINT AUTO_INCREMENT NOT NULL,
        customer_id BIGINT NOT NULL,
        status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
        total_price BIGINT NOT NULL,
        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (id)
    );

CREATE TABLE
    order_items (
        id BIGINT AUTO_INCREMENT NOT NULL,
        order_id BIGINT NOT NULL,
        product_id BIGINT NOT NULL,
        quantity INT NOT NULL,
        unit_price BIGINT NOT NULL,
        total_price BIGINT NOT NULL,
        PRIMARY KEY (id)
    );

ALTER TABLE
    addresses
    ADD CONSTRAINT `addresses_users_id_fk` FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

ALTER TABLE
    products
    ADD CONSTRAINT `fk_category` FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE NO ACTION;

ALTER TABLE
    cart_items
    ADD CONSTRAINT `fk_cart` FOREIGN KEY (cart_id) REFERENCES carts (id) ON DELETE CASCADE,
    ADD CONSTRAINT `fk_product` FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE;

ALTER TABLE
    orders
    ADD CONSTRAINT `fk_customer` FOREIGN KEY (customer_id) REFERENCES users (id) ON DELETE NO ACTION;

ALTER TABLE
    order_items
    ADD CONSTRAINT `fk_order` FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    ADD CONSTRAINT `fk_product_order` FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE NO ACTION;
