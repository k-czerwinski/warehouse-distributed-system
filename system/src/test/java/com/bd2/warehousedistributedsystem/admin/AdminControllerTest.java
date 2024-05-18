package com.bd2.warehousedistributedsystem.admin;

import com.bd2.warehousedistributedsystem.common.ProductRepository;
import com.bd2.warehousedistributedsystem.common.PurchaseOrderRepository;
import com.bd2.warehousedistributedsystem.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class, excludeAutoConfiguration = {AdminService.class})
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PurchaseOrderRepository purchaseOrderRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private AdminService adminService;

    @Test
    void getOrders() throws Exception{
        List<PurchaseOrder> orders = List.of(new PurchaseOrder(), new PurchaseOrder());
        when(purchaseOrderRepository.findAll()).thenReturn(orders);

        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(orders)));
    }

    @Test
    void getProducts() throws Exception {
        List<Product> products = List.of(Product.builder().code(123l).build(), Product.builder().code(456l).build());
        when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(products)));
    }

    @Test
    void lockProduct() throws Exception {
        long productCode = 123l;
        doNothing().when(adminService).lockProduct(productCode);

        mockMvc.perform(get("/admin/product/%d/lock".formatted(productCode)))
                .andExpect(status().isOk());
        verify(adminService, only()).lockProduct(productCode);
    }

    @Test
    void saveProduct() throws Exception {
        ProductDTO product = new ProductDTO("same product", BigDecimal.ONE, 1l);
        doNothing().when(adminService).saveProduct(product);

        mockMvc.perform(post("/admin/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product)))
                .andExpect(status().isOk());
        verify(adminService, only()).saveProduct(product);
    }

    @Test
    void getOrderDetails() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(123l);
        when(purchaseOrderRepository.findById(123l)).thenReturn(Optional.of(purchaseOrder));

        mockMvc.perform(get("/admin/order/" + 123l))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(purchaseOrder)));
    }

    @Test
    void getProductForWarehouse() throws Exception {
        Map<Product, Integer> productsWithQuantities = Map.of(
                Product.builder().code(12l).build(), 5,
                Product.builder().code(456l).build(), 6);
        when(adminService.getProductsWithQuantitiesInWarehouse(Warehouse.WAREHOUSE1)).thenReturn(productsWithQuantities);

        mockMvc.perform(get("/admin/warehouse/%s/products".formatted(Warehouse.WAREHOUSE1)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(productsWithQuantities)));
    }

    @Test
    void updateProductQuantity() throws Exception {
        long productCode = 21;
        int quantity = 37;
        when(productRepository.findById(productCode)).thenReturn(Optional.of(new Product()));
        doNothing().when(productRepository).updateProductCountInWarehouse(productCode, Warehouse.WAREHOUSE1.getOfficialName(), quantity);

        mockMvc.perform(patch("/admin/warehouse/%s/update-quantity".formatted(Warehouse.WAREHOUSE1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new ProductQuantityRequest(productCode, quantity))))
                .andExpect(status().isOk());
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}