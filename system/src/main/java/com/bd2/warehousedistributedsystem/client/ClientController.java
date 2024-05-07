package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private static final Logger LOGGER = Logger.getLogger("ClientController");
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @Operation(summary = "Get available products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All products which are not locked"),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @GetMapping(value = "/products")
    public List<Product> products() {
        return clientRepository.findAllByLockedIs(false);
    }

    @Operation(summary = "Get products stored in cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products in cart", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @GetMapping(value = "/cart")
    public Map<Product, Integer> cart(@CookieValue(value = "productCodes", defaultValue = "") String productCodesString) {
        Map<Long, Integer> productsMap = Cart.getProductCodesListFromCookie(productCodesString);
        return mapProductCodes(productsMap);
    }

    @Operation(summary = "Create order with selected products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order has been confirmed."),
            @ApiResponse(responseCode = "404", description = "Order could not be confirmed, there is insufficent number of products in selected warehouse or product is locked."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @PostMapping(value = "/confirm-cart")
    public ResponseEntity<String> confirmOrder(ConfirmOrderRequest body,
                                     @CookieValue(value = "productCodes", defaultValue = "") String productCodesString,
                                     HttpServletResponse response) {
        Cart cart = new Cart(mapProductCodes(Cart.getProductCodesListFromCookie(productCodesString)));
        try {
            clientService.confirmOrder(cart, body.warehouse(), body.ordererName(), body.shippingAddress());
            LOGGER.info("Cart confirmed in warehouse %s".formatted(body.warehouse().name()));
            response.addCookie(createCartCookie(""));
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            LOGGER.warning("Cart cannot be confirmed in warehouse %s".formatted(body.warehouse().name()));
            LOGGER.warning("Exception: %s".formatted(exception.getMessage()));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Add product to cart", description = "Add product to cart, products ids and quantities are stored in cookies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product has been added to cart."),
            @ApiResponse(responseCode = "404", description = "Product has not been found."),
            @ApiResponse(responseCode = "500", description = "Unexpected error.")})
    @PostMapping(value = "/products/add-to-cart")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addToCart(@RequestBody ProductQuantityRequest productQuantityRequest,
                                            @CookieValue(value = "productCodes", defaultValue = "") String productCodesString,
                                            HttpServletResponse response) {
        Product product = clientRepository.findByCode(productQuantityRequest.getProductCode()).orElseThrow(EntityNotFoundException::new);
        Map<Long, Integer> products = Cart.getProductCodesListFromCookie(productCodesString);
        products.put(productQuantityRequest.getProductCode(), products.getOrDefault(productQuantityRequest.getProductCode(), 0)
                    + productQuantityRequest.getQuantity());
        response.addCookie(createCartCookie(Cart.toProductCodesString(products)));
        LOGGER.info("%s has been added to cart in quantity: %s".formatted(product.getName(), productQuantityRequest.getQuantity()));
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
