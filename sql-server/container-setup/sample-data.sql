USE central;
-- Insert categories
INSERT INTO central.dbo.category (name) VALUES
('Smart Home Devices'),
('Wearable Technology'),
('Entertainment Gadgets'),
('Personal Electronics');

-- Insert products
INSERT INTO central.dbo.product (name, price, category_id, summary_quantity, is_locked) VALUES
('Smart Thermostat', 149.99, 1, 20, 0),
('Smart Doorbell', 199.99, 1, 30, 0),
('Smart Lock', 129.99, 1, 40, 0),
('Smart Light Bulbs', 39.99, 1, 50, 0),
('Smart Plug', 29.99, 1, 60, 0),
('Fitness Tracker', 79.99, 2, 100, 0),
('Smartwatch', 249.99, 2, 21, 0),
('Smart Glasses', 399.99, 2, 37, 0),
('GPS Tracker', 59.99, 2, 20, 0),
('Locked product 1', 129.99, 2, 20, 0),
('Locked product 2', 79.99, 3, 20, 1),
('Virtual Reality Headset', 299.99, 3, 20, 0),
('Bluetooth Speaker', 49.99, 3, 20, 0),
('Portable Projector', 199.99, 3, 20, 0),
('Digital Camera', 299.99, 3, 20, 0),
('Smartphone', 699.99, 4, 20, 0),
('E-reader', 129.99, 4, 20, 0),
('Noise-Canceling Headphones', 199.99, 4, 20, 0),
('Digital Voice Recorder', 49.99, 4, 20, 0),
('Smart Pen', 149.99, 4, 20, 0);
