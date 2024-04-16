USE central
GO
CREATE PROCEDURE MakeOrderWarehouse1
   @ProductCode bigint,
   @Quantity int
AS
BEGIN
    SET NOCOUNT ON;
		UPDATE warehouse1.dbo.product_storage set quantity = quantity - @Quantity where code = @ProductCode;
		UPDATE central.dbo.product set summary_quantity = summary_quantity - @Quantity where code = @ProductCode;
END
GO

CREATE PROCEDURE MakeOrderWarehouse2
   @ProductCode bigint,
   @Quantity int
AS
BEGIN
    SET NOCOUNT ON;
		UPDATE warehouse2.dbo.product_storage set quantity = quantity - @Quantity where code = @ProductCode;
		UPDATE central.dbo.product set summary_quantity = summary_quantity - @Quantity where code = @ProductCode;
END
GO

CREATE PROCEDURE MakeOrderWarehouse3
   @ProductCode bigint,
   @Quantity int
AS
BEGIN
    SET NOCOUNT ON;
		UPDATE warehouse3.dbo.product_storage set quantity = quantity - @Quantity where code = @ProductCode;
		UPDATE central.dbo.product set summary_quantity = summary_quantity - @Quantity where code = @ProductCode;
END
GO

CREATE PROCEDURE MakeOrderWarehouse4
   @ProductCode bigint,
   @Quantity int
AS
BEGIN
    SET NOCOUNT ON;
		UPDATE warehouse4.dbo.product_storage set quantity = quantity - @Quantity where code = @ProductCode;
		UPDATE central.dbo.product set summary_quantity = summary_quantity - @Quantity where code = @ProductCode;
END
GO