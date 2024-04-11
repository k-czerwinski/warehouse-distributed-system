USE central
GO

CREATE TRIGGER trigger_insert_product
ON central.dbo.product
AFTER INSERT
AS
BEGIN 
    DECLARE @code bigint, @quantity int
    SELECT @code = code, @quantity = 0 FROM inserted
    
    INSERT INTO warehouse1.dbo.product_storage (code, quantity) VALUES (@code, @quantity)
    INSERT INTO warehouse2.dbo.product_storage (code, quantity) VALUES (@code, @quantity)
    INSERT INTO warehouse3.dbo.product_storage (code, quantity) VALUES (@code, @quantity)
    INSERT INTO warehouse4.dbo.product_storage (code, quantity) VALUES (@code, @quantity)
END
GO

USE warehouse1
GO
CREATE TRIGGER trigger_insert_product
ON warehouse1.dbo.product_storage
AFTER INSERT, UPDATE
AS
BEGIN 
    DECLARE @code bigint, @quantity int
    SELECT @code = code, @quantity = quantity FROM inserted
    
    IF @quantity < 0
      RAISERROR('Number of items must be non negative number.', 16, 1);
END
GO

USE warehouse2
GO
CREATE TRIGGER trigger_insert_product
ON warehouse2.dbo.product_storage
AFTER INSERT, UPDATE
AS
BEGIN 
    DECLARE @code bigint, @quantity int
    SELECT @code = code, @quantity = quantity FROM inserted
    
    IF @quantity < 0
      RAISERROR('Number of items must be non negative number.', 16, 1);
END
GO

USE warehouse3
GO
CREATE TRIGGER trigger_insert_product
ON warehouse3.dbo.product_storage
AFTER INSERT, UPDATE
AS
BEGIN 
    DECLARE @code bigint, @quantity int
    SELECT @code = code, @quantity = quantity FROM inserted
    
    IF @quantity < 0
      RAISERROR('Number of items must be non negative number.', 16, 1);
END
GO

USE warehouse4
GO
CREATE TRIGGER trigger_insert_product
ON warehouse4.dbo.product_storage
AFTER INSERT, UPDATE
AS
BEGIN 
    DECLARE @code bigint, @quantity int
    SELECT @code = code, @quantity = quantity FROM inserted
    
    IF @quantity < 0
      RAISERROR('Number of items must be non negative number.', 16, 1);
END
GO
