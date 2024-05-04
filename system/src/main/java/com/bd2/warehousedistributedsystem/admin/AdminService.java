package com.bd2.warehousedistributedsystem.admin;

import com.bd2.warehousedistributedsystem.client.CategoryRepository;
import com.bd2.warehousedistributedsystem.common.ProductRepository;
import com.bd2.warehousedistributedsystem.model.Category;
import com.bd2.warehousedistributedsystem.model.Product;
import com.bd2.warehousedistributedsystem.model.ProductDTO;
import com.bd2.warehousedistributedsystem.model.Warehouse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {
    private static final Logger logger = Logger.getLogger("AdminService");
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void lockProduct(long productCode) {
        Product product = productRepository.findById(productCode).orElseThrow();
        productRepository.setProductLocked(product.getCode());
        logger.info("Product has been locked!");
    }

    public void saveProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.categoryId()).orElseThrow();
        productRepository.save(new Product(productDTO, category));
        logger.info("Product has been saved!");
    }

    public Map<Product, Integer> getProductsWithQuantitiesInWarehouse(Warehouse warehouse) {
        return productRepository.findAll().stream().map(product -> Map.entry(product, productRepository.getProductCountInWarehouse(product.getCode(), warehouse.getOfficialName())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
