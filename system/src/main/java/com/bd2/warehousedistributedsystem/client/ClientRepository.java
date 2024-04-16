package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.Category;
import com.bd2.warehousedistributedsystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(Category category);
    Optional<Product> findByCode(long code);

    @Procedure(procedureName = "MakeOrderWarehouse1")
    void confirmProductOrderInWarehouse1(long productCode, int quantity);

    @Procedure(procedureName = "MakeOrderWarehouse2")
    void confirmProductOrderInWarehouse2(long productCode, int quantity);

    @Procedure(procedureName = "MakeOrderWarehouse3")
    void confirmProductOrderInWarehouse3(long productCode, int quantity);

    @Procedure(procedureName = "MakeOrderWarehouse4")
    void confirmProductOrderInWarehouse4(long productCode, int quantity);
}
