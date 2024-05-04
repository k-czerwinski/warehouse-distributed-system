package com.bd2.warehousedistributedsystem.admin;

import com.bd2.warehousedistributedsystem.client.CategoryRepository;
import com.bd2.warehousedistributedsystem.common.ProductRepository;
import com.bd2.warehousedistributedsystem.common.PurchaseOrderRepository;
import com.bd2.warehousedistributedsystem.model.ProductDTO;
import com.bd2.warehousedistributedsystem.model.ProductQuantityRequest;
import com.bd2.warehousedistributedsystem.model.Warehouse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AdminService adminService;

    @GetMapping("/orders")
    public String getOrders(Model model) {
        model.addAttribute("orders", purchaseOrderRepository.findAll());
        return "admin/orders";
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/products";
    }

    @GetMapping("/product/{product_code}/lock")
    public ResponseEntity<String> lockProduct(@PathVariable(name = "product_code") Long productCode) {
        adminService.lockProduct(productCode);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/product/add")
    public String addProductPage(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/add-product";
    }

    @PostMapping(value = "/product/save-product", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String saveProduct(Model model, ProductDTO productDTO) {
        adminService.saveProduct(productDTO);
        model.addAttribute("success", true);
        return "admin/add-product";
    }

    @GetMapping(value = "/order/{order_id}")
    public String getOrderDetails(Model model, @PathVariable("order_id") Long orderId) {
        model.addAttribute("order", purchaseOrderRepository.findById(orderId).orElseThrow());
        return "admin/order-details";
    }

    @GetMapping(value = "/warehouse/{warehouse_name}/products")
    public String getProductForWarehouse(Model model, @PathVariable("warehouse_name") Warehouse warehouse) {
        model.addAttribute("productsMap", adminService.getProductsWithQuantitiesInWarehouse(warehouse));
        model.addAttribute("warehouse", warehouse);
        return "admin/warehouse-product";
    }

    @PatchMapping (value = "/warehouse/{warehouse_name}/update-quantity")
    public ResponseEntity<String> updateProductQuantity(Model model, @PathVariable("warehouse_name") Warehouse warehouse, @RequestBody ProductQuantityRequest body) {
        productRepository.updateProductCountInWarehouse(body.getProductCode(), warehouse.getOfficialName(), body.getQuantity());
        return ResponseEntity.status(200).build();
    }
}
