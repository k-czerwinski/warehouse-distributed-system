package com.bd2.warehousedistributedsystem.common;

import com.bd2.warehousedistributedsystem.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query(value = "update Product p set p.locked = true where p.code = :productCode")
    void setProductLocked(@Param("productCode") long productCode);

    @Query(value = "EXEC GetProductCount @productCode = :productCode, @warehouseName = :warehouseName", nativeQuery = true)
    Integer getProductCountInWarehouse(@Param("productCode") long productCode, @Param("warehouseName") String warehouseName);

    @Modifying
    @Transactional
    @Query(value = "EXEC UpdateProductCount @productCode = :productCode, @warehouseName = :warehouseName, @quantity=:quantity", nativeQuery = true)
    void updateProductCountInWarehouse(@Param("productCode") long productCode, @Param("warehouseName") String warehouseName, @Param("quantity") int quantity);
}
