USE central
GO
CREATE PROCEDURE MakeOrder
   @ProductCode bigint,
   @Quantity int
AS
BEGIN
    SET NOCOUNT ON;
		UPDATE warehouse1.dbo.product_storage set quantity = quantity - @Quantity where code = @ProductCode;
		UPDATE central.dbo.product set summary_quantity = summary_quantity - @Quantity where code = @ProductCode;
END
GO