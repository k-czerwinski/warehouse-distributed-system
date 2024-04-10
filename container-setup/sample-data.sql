USE central;
-- Insert categories
INSERT INTO central.dbo.category (name) VALUES
('Smart Home Devices'),
('Wearable Technology'),
('Entertainment Gadgets'),
('Personal Electronics');

-- Insert products
INSERT INTO central.dbo.product (name, price, category_id) VALUES
('Smart Thermostat', 149.99, 1),
('Smart Doorbell', 199.99, 1),
('Smart Lock', 129.99, 1),
('Smart Light Bulbs', 39.99, 1),
('Smart Plug', 29.99, 1),
('Fitness Tracker', 79.99, 2),
('Smartwatch', 249.99, 2),
('Smart Glasses', 399.99, 2),
('GPS Tracker', 59.99, 2),
('Biometric Ring', 129.99, 2),
('Streaming Media Player', 79.99, 3),
('Virtual Reality Headset', 299.99, 3),
('Bluetooth Speaker', 49.99, 3),
('Portable Projector', 199.99, 3),
('Digital Camera', 299.99, 3),
('Smartphone', 699.99, 4),
('E-reader', 129.99, 4),
('Noise-Canceling Headphones', 199.99, 4),
('Digital Voice Recorder', 49.99, 4),
('Smart Pen', 149.99, 4);
