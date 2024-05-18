package com.bd2.warehousedistributedsystem.admin;

import com.bd2.warehousedistributedsystem.common.ProductRepository;
import com.bd2.warehousedistributedsystem.common.PurchaseOrderRepository;
import com.bd2.warehousedistributedsystem.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final AdminService adminService;

    @Operation(summary = "Get basic informations about all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "All created orders has been returned"),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @GetMapping("/orders")
    public List<PurchaseOrder> getOrders() {
        return purchaseOrderRepository.findAll();
    }

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "All products returned"),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Operation(summary = "Lock product", description = "Lock product with id passed inside body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product has been locked."),
            @ApiResponse(responseCode = "404", description = "Product has not been found."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @GetMapping("/product/{product_code}/lock")
    @ResponseStatus(HttpStatus.OK)
    public void lockProduct(@PathVariable(name = "product_code") Long productCode) {
        adminService.lockProduct(productCode);
    }

    @Operation(summary = "Save new product", description = "Save new product in central warehouse. Add entry in product_storage in all warehouses databases.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product has been saved."),
            @ApiResponse(responseCode = "404", description = "Invalid category or other fields."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/product")
    public void saveProduct(@RequestBody ProductDTO productDTO) {
        adminService.saveProduct(productDTO);
    }

    @Operation(summary = "Get order details", description = "Get details about order which id is passed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details returned."),
            @ApiResponse(responseCode = "404", description = "Order with given id do not exist."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @GetMapping(value = "/order/{order_id}")
    public PurchaseOrder getOrderDetails(@PathVariable("order_id") Long orderId) {
        return purchaseOrderRepository.findById(orderId).orElseThrow();
    }

    @Operation(summary = "Get products in specified warehouse", description = "Get all products stored in specified warehouse, information includes quanity of product in that warehouse.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "404", description = "Warehouse is not available or not exists."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @GetMapping(value = "/warehouse/{warehouse_name}/products")
    public Map<Product, Integer> getProductForWarehouse(@PathVariable("warehouse_name") Warehouse warehouse) {
        return adminService.getProductsWithQuantitiesInWarehouse(warehouse);
    }

    @Operation(summary = "Update quantity of product in specified warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "404", description = "Warehouse or product is not available or not exists."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @PatchMapping (value = "/warehouse/{warehouse_name}/update-quantity")
    @ResponseStatus(HttpStatus.OK)
    public void updateProductQuantity(@PathVariable("warehouse_name") Warehouse warehouse, @RequestBody ProductQuantityRequest body) {
        productRepository.findById(body.getProductCode()).orElseThrow();
        productRepository.updateProductCountInWarehouse(body.getProductCode(), warehouse.getOfficialName(), body.getQuantity());
    }
}
