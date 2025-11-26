INSERT INTO categories (name) 
VALUES
('Electronics'),
('Books'),
('Apparel'),
('Home Goods'),
('Groceries');

INSERT INTO products (name, price, quantity, category_id, description)
VALUES
('Wireless Noise-Cancelling Headphones', 24.99, 5, 1, 'Premium over-ear headphones with 30-hour battery life and exceptional sound clarity.'),
('The Silent Garden (Fiction Novel)', 15.99, 150, 2, 'A gripping psychological thriller set in a remote mountain town, ISBN: 978-0123456789.'),
('Organic Cotton T-Shirt (Large)', 29.99, 18, 3, 'A comfortable, eco-friendly t-shirt made from 100% organic cotton. Color: Heather Gray.'),
('Smart Coffee Maker with WiFi', 129.99, 3, 4, 'Program your brewing schedule or start brewing remotely using a smartphone app. 12-cup capacity.'),
('Artisan Dark Roast Coffee Beans (12oz)', 14.50, 80, 5, 'Whole bean coffee, sustainably sourced and dark roasted for a rich, bold flavor.');