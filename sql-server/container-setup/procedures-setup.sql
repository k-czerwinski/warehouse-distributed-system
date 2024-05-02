USE central
GO
CREATE PROCEDURE MakeOrder
   @ProductCode bigint,
   @Quantity int,
   @warehouseName varchar(20)
AS
BEGIN
   SET NOCOUNT ON;
    IF @warehouseName = 'warehouse1'
    BEGIN
        UPDATE warehouse1.dbo.product_storage SET quantity = quantity - @quantity WHERE code = @productCode;
    END
    ELSE IF @warehouseName = 'warehouse2'
    BEGIN
        UPDATE warehouse2.dbo.product_storage SET quantity = quantity - @quantity WHERE code = @productCode;
    END
    ELSE IF @warehouseName ='warehouse3'
    BEGIN
        UPDATE warehouse3.dbo.product_storage SET quantity = quantity - @quantity WHERE code = @productCode;
    END
    ELSE IF @warehouseName ='warehouse4'
    BEGIN
        UPDATE warehouse4.dbo.product_storage SET quantity = quantity - @quantity WHERE code = @productCode;
    END
    ELSE
    BEGIN
        RAISERROR ('Invalid warehouse Name', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END
    
    UPDATE central.dbo.product
    SET summary_quantity = summary_quantity - @quantity
    WHERE code = @productCode;
END
GO
-------------------------
CREATE PROCEDURE CreateNewOrder
   @ordererName varchar(50),
   @shippingAddress varchar(100),
   @price int
AS
BEGIN
   INSERT INTO central.dbo.[order] ([date], [price], [orderer_name], [shipping_address])
   VALUES (GETDATE(), @price, @ordererName, @shippingAddress);
END
GO
--------------------------------
CREATE PROCEDURE AddProductToOrder
   @warehouseName varchar(20),
   @orderId bigint,
   @productCode bigint,
   @quantity int
AS
BEGIN
   DECLARE @warehouseID bigint;
   SELECT @warehouseID = id from central.dbo.warehouse where [name] = @warehouseName;
   INSERT INTO central.dbo.order_product (order_id, product_id, warehouse_id, quantity)
   VALUES (@orderId, @productCode, @warehouseID, @quantity);
END


        