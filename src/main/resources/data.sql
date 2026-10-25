INSERT INTO customers (first_name, last_name, email, phone, address, created_at)
VALUES
    ('Alice', 'Johnson', 'alice@example.com', '555-0101', '123 Market Street', NOW()),
    ('Brandon', 'Smith', 'brandon@example.com', '555-0102', '45 Elm Avenue', NOW());

INSERT INTO products (sku, name, description, price, stock_quantity, category, active, created_at, updated_at)
VALUES
    ('SKU-1001', 'Laptop', 'Business laptop', 999.99, 15, 'Electronics', true, NOW(), NOW()),
    ('SKU-1002', 'Office Chair', 'Ergonomic chair', 199.99, 8, 'Furniture', true, NOW(), NOW()),
    ('SKU-1003', 'Coffee Maker', 'Compact coffee maker', 89.99, 4, 'Home Appliances', true, NOW(), NOW());
