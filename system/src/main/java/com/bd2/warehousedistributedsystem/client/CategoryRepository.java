package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
