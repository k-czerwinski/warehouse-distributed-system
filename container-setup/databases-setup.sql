IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'central')
BEGIN
  CREATE DATABASE central
  GO

  CREATE TABLE central.[dbo].[product] (
    [code] bigint PRIMARY KEY NOT NULL IDENTITY(1, 1),
    [name] varchar(50) NOT NULL,
    [price] decimal NOT NULL,
    [category_id] bigint NOT NULL
  )
  GO

  CREATE TABLE central.[dbo].[category] (
    [id] bigint PRIMARY KEY NOT NULL IDENTITY(1, 1),
    [name] varchar(50) NOT NULL
  )
  GO

  CREATE TABLE central.[dbo].[warehouse] (
    [id] bigint PRIMARY KEY NOT NULL IDENTITY(1, 1),
    [name] varchar(50) NOT NULL,
    [location] varchar(50) NOT NULL
  )
  GO

  CREATE TABLE central.[dbo].[order] (
    [id] bigint PRIMARY KEY NOT NULL IDENTITY(1, 1),
    [date] datetime NOT NULL,
    [price] decimal NOT NULL,
    [orderer_name] varchar(50),
    [shipping_address] varchar(100)
  )
  GO

  CREATE TABLE central.[dbo].[order_product] (
    [order_id] bigint NOT NULL,
    [product_id] bigint NOT NULL,
    [warehouse_id] bigint NOT NULL,
    PRIMARY KEY ([order_id], [product_id], [warehouse_id])
  )
  GO

  ALTER TABLE central.[dbo].[product] ADD FOREIGN KEY ([category_id]) REFERENCES central.[dbo].[category] ([id])
  ALTER TABLE central.[dbo].[order_product] ADD FOREIGN KEY ([order_id]) REFERENCES central.[dbo].[order] ([id])
  ALTER TABLE central.[dbo].[order_product] ADD FOREIGN KEY ([product_id]) REFERENCES central.[dbo].[product] ([code])
  ALTER TABLE central.[dbo].[order_product] ADD FOREIGN KEY ([warehouse_id]) REFERENCES central.[dbo].[warehouse] ([id])
END

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'warehouse1')
BEGIN
  CREATE DATABASE warehouse1
  GO
  CREATE TABLE warehouse1.[dbo].[product_storage] (
    [code] bigint PRIMARY KEY NOT NULL,
    [quantity] int NOT NULL
  )
  GO
END

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'warehouse2')
BEGIN
  CREATE DATABASE warehouse2
  GO
  CREATE TABLE warehouse2.[dbo].[product_storage] (
    [code] bigint PRIMARY KEY NOT NULL,
    [quantity] int NOT NULL
  )
  GO
END

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'warehouse3')
BEGIN
  CREATE DATABASE warehouse3
  GO
  CREATE TABLE warehouse3.[dbo].[product_storage] (
    [code] bigint PRIMARY KEY NOT NULL,
    [quantity] int NOT NULL
  )
  GO
END

IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'warehouse4')
BEGIN
  CREATE DATABASE warehouse4
  GO
  CREATE TABLE warehouse4.[dbo].[product_storage] (
    [code] bigint PRIMARY KEY NOT NULL,
    [quantity] int NOT NULL
  )
  GO
END
