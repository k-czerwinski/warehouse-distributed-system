package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.common.PurchaseOrderRepository;
import com.bd2.warehousedistributedsystem.model.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private static final Logger LOGGER = Logger.getLogger("ClientController");
    private final ClientRepository clientRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
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
    public String cart(Model model, @CookieValue(value = "productCodes", defaultValue = "") String productCodesString) {
        Map<Long, Integer> productsMap = Cart.getProductCodesListFromCookie(productCodesString);
        model.addAttribute("cart", mapProductCodes(productsMap));
        return "client/cart";
    }

    @PostMapping(value = "/confirm-cart", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String confirmOrder(Model model, ConfirmOrderRequest body,
                                     @CookieValue(value = "productCodes", defaultValue = "") String productCodesString,
                                     HttpServletResponse response) {
        Cart cart = new Cart(mapProductCodes(Cart.getProductCodesListFromCookie(productCodesString)));
        try {
            clientService.confirmOrder(cart, body.warehouse(), body.ordererName(), body.shippingAddress());
            LOGGER.info("Cart confirmed in warehouse %s".formatted(body.warehouse().name()));
            model.addAttribute("success", true);
            response.addCookie(createCartCookie(""));
        } catch (Exception exception) {
            LOGGER.warning("Cart cannot be confirmed in warehouse %s".formatted(body.warehouse().name()));
            LOGGER.warning("Exception: %s".formatted(exception.getMessage()));
            model.addAttribute("error", true);
            model.addAttribute("cart", cart.getProductsInCart());
        }
        return "client/cart";
    }

    @PostMapping(value = "/products/add-to-cart")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest addToCartRequest,
                                            @CookieValue(value = "productCodes", defaultValue = "") String productCodesString,
                                            HttpServletResponse response) {
        Product product = clientRepository.findByCode(addToCartRequest.getProductCode()).orElseThrow(EntityNotFoundException::new);
        Map<Long, Integer> products = Cart.getProductCodesListFromCookie(productCodesString);
        products.put(addToCartRequest.getProductCode(), products.getOrDefault(addToCartRequest.getProductCode(), 0)
                    + addToCartRequest.getQuantity());
        response.addCookie(createCartCookie(Cart.toProductCodesString(products)));
        LOGGER.info("%s has been added to cart in quantity: %s".formatted(product.getName(), addToCartRequest.getQuantity()));
        return ResponseEntity.ok("Product added succesfully");
    }

    private Map<Product, Integer> mapProductCodes(Map<Long, Integer> productCodes) {
        return productCodes.entrySet().stream()
                .map(entry -> Map.entry(clientRepository.findByCode(entry.getKey()).orElseThrow(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Cookie createCartCookie(String value) {
        Cookie cookie = new Cookie("productCodes", value);
        cookie.setPath("/");
        return cookie;
    }
}
