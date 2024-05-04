package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.Category;
import com.bd2.warehousedistributedsystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(Category category);
    Optional<Product> findByCode(long code);

    @Procedure(procedureName = "MakeOrder")
    void decreaseProductCount(long productCode, int quantity, String warehouseName);

    /**
     *
     * @param ordererName
     * @param shippingAddress
     * @param price
     * @return id of order which has been created
     */
    @Query(value = "EXEC CreateNewOrder @ordererName = :ordererName, @shippingAddress = :shippingAddress, @price = :price", nativeQuery = true)
    Long createNewOrder(@Param("ordererName") String ordererName, @Param("shippingAddress") String shippingAddress, @Param("price") BigDecimal price);

    @Procedure(procedureName = "AddProductToOrder")
    void assignProductToOrder(String warehouseName, long orderId, long productCode, int quantity);
}
