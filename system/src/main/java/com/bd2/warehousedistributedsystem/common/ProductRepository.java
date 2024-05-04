package com.bd2.warehousedistributedsystem.common;

import com.bd2.warehousedistributedsystem.model.Product;
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
}
