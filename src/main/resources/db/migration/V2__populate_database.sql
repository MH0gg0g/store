-- Users (passwords are bcrypt hashes for 'password')
INSERT INTO users (name, email, password, role) VALUES
  ('TEST', 'test@mail.com', '$2a$10$/YNG/1DeFeLK2yPdN81BReJXxCqWUOtaJuHXDMQbRkWvjFB7TvpQe', 'USER');
SET @test_id = LAST_INSERT_ID();

-- Addresses (belongs to TEST)
INSERT INTO addresses (street, city, user_id) VALUES
  ('123 Main St', 'Springfield', @test_id),
  ('456 Side Ave', 'Somewhere', @test_id);

-- Categories
INSERT INTO categories (name) VALUES ('Electronics');
SET @cat_electronics = LAST_INSERT_ID();
INSERT INTO categories (name) VALUES ('Books');
SET @cat_books = LAST_INSERT_ID();

-- Products
INSERT INTO products (name, price, category_id, description) VALUES
  ('Smartphone X', 699, @cat_electronics, 'Sample smartphone with 128GB storage'),
  ('Laptop Pro', 1299, @cat_electronics, 'Powerful laptop for developers'),
  ('Learning SQL', 29, @cat_books, 'Practical guide to SQL and relational databases');

-- Capture product IDs explicitly to make sure we reference correct ones
SELECT id INTO @prod_smartphone FROM products WHERE name='Smartphone X' LIMIT 1;
SELECT id INTO @prod_laptop FROM products WHERE name='Laptop Pro' LIMIT 1;
SELECT id INTO @prod_book FROM products WHERE name='Learning SQL' LIMIT 1;

-- Create two carts (use UUIDs so we can reference them)
SET @cart1 = UUID();
INSERT INTO carts (id) VALUES (UUID_TO_BIN(@cart1));

-- Add items to cart1 (TEST's basket for testing)
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
  (UUID_TO_BIN(@cart1), @prod_smartphone, 1),
  (UUID_TO_BIN(@cart1), @prod_book, 2);

-- Create an order for TEST (completed example)
INSERT INTO orders (customer_id, status, total_price) VALUES
  (@test_id, 'COMPLETED', 1999);
SET @order1 = LAST_INSERT_ID();

-- Order items for that order
INSERT INTO order_items (order_id, product_id, quantity, unit_price, total_price) VALUES
  (@order1, @prod_smartphone, 1, 699, 699),
  (@order1, @prod_laptop, 1, 1299, 1299);