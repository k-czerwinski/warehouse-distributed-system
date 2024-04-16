USE central
GO

CREATE TRIGGER trigger_insert_product
ON central.dbo.product
AFTER INSERT
AS
BEGIN 
    INSERT INTO warehouse1.dbo.product_storage (code, quantity)
    SELECT code, summary_quantity / 4 FROM inserted;

    INSERT INTO warehouse2.dbo.product_storage (code, quantity)
    SELECT code, summary_quantity / 4 FROM inserted;

    INSERT INTO warehouse3.dbo.product_storage (code, quantity)
    SELECT code, summary_quantity / 4 FROM inserted;

    INSERT INTO warehouse4.dbo.product_storage (code, quantity)
    SELECT code, summary_quantity - ((summary_quantity / 4 ) * 3) FROM inserted;
END
GO

CREATE TRIGGER trigger_update_product
ON central.dbo.product
AFTER UPDATE
AS
BEGIN
  IF UPDATE(summary_quantity)
    BEGIN
      DECLARE @code bigint, @is_locked bit, @summary_quantity int
      SELECT @code = code, @is_locked = is_locked, @summary_quantity = summary_quantity FROM inserted;

      IF @is_locked = 1
      BEGIN
        RAISERROR('Product is locked.', 16, 1);
        ROLLBACK TRANSACTION
      END

      IF @summary_quantity < 0
      BEGIN
        RAISERROR('Product summary qunatity cannot be lower than 0.', 16, 1);
        ROLLBACK TRANSACTION
      END
    END
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
    BEGIN
      RAISERROR('Number of items must be non negative number.', 16, 1);
      ROLLBACK TRANSACTION
    END
    
    DECLARE @is_locked bit
    SELECT @is_locked = is_locked FROM central.dbo.product WHERE code = @code
    
    IF @is_locked = 1
    BEGIN
      RAISERROR('Product is locked!', 16, 1);
      ROLLBACK TRANSACTION
    END
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
    BEGIN
      RAISERROR('Number of items must be non negative number.', 16, 1);
      ROLLBACK TRANSACTION
    END

    DECLARE @is_locked bit
    SELECT @is_locked = is_locked FROM central.dbo.product WHERE code = @code
    
    IF @is_locked = 1
    BEGIN
      RAISERROR('Product is locked!', 16, 1);
      ROLLBACK TRANSACTION
    END
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
    BEGIN
      RAISERROR('Number of items must be non negative number.', 16, 1);
      ROLLBACK TRANSACTION
    END
          
    DECLARE @is_locked bit
    SELECT @is_locked = is_locked FROM central.dbo.product WHERE code = @code
    
    IF @is_locked = 1
    BEGIN
      RAISERROR('Product is locked!', 16, 1);
      ROLLBACK TRANSACTION
    END
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
    BEGIN
      RAISERROR('Number of items must be non negative number.', 16, 1);
      ROLLBACK TRANSACTION
    END
        
    DECLARE @is_locked bit
    SELECT @is_locked = is_locked FROM central.dbo.product WHERE code = @code
    
    IF @is_locked = 1
    BEGIN
      RAISERROR('Product is locked!', 16, 1);
      ROLLBACK TRANSACTION
    END
END
GO
