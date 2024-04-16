package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private Cart cart = new Cart();
    private static final Logger LOGGER = Logger.getLogger("ClientController");
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping(value = "/products")
    public String products(Model model) {
        List<Product> productList = clientRepository.findAll();
        model.addAttribute("products", productList);
        return "client/products";
    }

    @GetMapping(value = "/cart")
    public String cart(Model model) {
        model.addAttribute("cart", cart.getProductsInCart());
        return "client/cart";
    }

    @GetMapping(value = "/confirm-order")
    public String confirmOrder(Model model, @RequestParam("warehouse") Warehouse warehouse) {
        try {
            clientService.confirmOrder(cart, warehouse);
            LOGGER.info("Order confirmed in warehouse %s".formatted(warehouse.name()));
            model.addAttribute("success", true);
        } catch (Exception exception) {
            LOGGER.warning("Order cannot be confirmed in warehouse %s".formatted(warehouse.name()));
            LOGGER.warning("Exception: ".formatted(exception.getMessage()));
            model.addAttribute("error", true);
        }
        cart.clearCart();
        return "client/cart";
    }

    @PostMapping(value = "/products/add-to-cart")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest addToCartRequest) {
        Product product = clientRepository.findByCode(addToCartRequest.getProductCode()).orElseThrow(EntityNotFoundException::new);
        cart.addToCart(product, addToCartRequest.getQuantity());
        LOGGER.info("%s has been added to cart in quantity: %s".formatted(product.getName(), addToCartRequest.getQuantity()));
        return ResponseEntity.ok("Product added succesfully");
    }
}
