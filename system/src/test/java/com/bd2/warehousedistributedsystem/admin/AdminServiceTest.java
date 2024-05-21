package com.bd2.warehousedistributedsystem.admin;

import com.bd2.warehousedistributedsystem.client.CategoryRepository;
import com.bd2.warehousedistributedsystem.common.ProductRepository;
import com.bd2.warehousedistributedsystem.model.Category;
import com.bd2.warehousedistributedsystem.model.Product;
import com.bd2.warehousedistributedsystem.model.ProductDTO;
import com.bd2.warehousedistributedsystem.model.Warehouse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminService adminService;


    @Test
    void testLockProduct() {
        long productCode = 123L;
        Product product = new Product();
        product.setCode(productCode);

        when(productRepository.findById(productCode)).thenReturn(Optional.of(product));

        adminService.lockProduct(productCode);

        verify(productRepository).setProductLocked(productCode);
    }

    @Test
    void testLockProductProductNotFound() {
        long productCode = 123L;

        when(productRepository.findById(productCode)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adminService.lockProduct(productCode));
    }

    @Test
    void testSaveProduct() {
        ProductDTO productDTO = new ProductDTO( "Product Name", new BigDecimal(10.0), 1L);
        Category category = Category.builder().id(1L).build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        adminService.saveProduct(productDTO);

        verify(productRepository).save(ArgumentMatchers.any());
    }

    @Test
    void testSaveProductCategoryNotFound() {
        ProductDTO productDTO = new ProductDTO( "Product Name", new BigDecimal(10.0), 1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adminService.saveProduct(productDTO));
    }

    @Test
    void testGetProductsWithQuantitiesInWarehouse() {
        Warehouse warehouse = Warehouse.WAREHOUSE1;

        Product product1 = new Product();
        product1.setCode(1L);
        Product product2 = new Product();
        product2.setCode(2L);

        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);
        System.out.println(productRepository.findAll());
        when(productRepository.getProductCountInWarehouse(1L, warehouse.getOfficialName())).thenReturn(10);
        when(productRepository.getProductCountInWarehouse(2L, warehouse.getOfficialName())).thenReturn(20);

        Map<Product, Integer> result = adminService.getProductsWithQuantitiesInWarehouse(warehouse);

        assertEquals(2, result.size());
        assertEquals(10, result.get(product1));
        assertEquals(20, result.get(product2));
    }
}
