package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.ConfirmOrderRequest;
import com.bd2.warehousedistributedsystem.model.ProductQuantityRequest;
import com.bd2.warehousedistributedsystem.model.Product;
import com.bd2.warehousedistributedsystem.model.Warehouse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class, excludeAutoConfiguration = {ClientRepository.class, ClientService.class})
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ClientRepository clientRepository;
    @MockBean
    ClientService clientService;

    @Test
    void confirmOrder() throws Exception {
        Warehouse warehouse = Warehouse.WAREHOUSE1;
        doNothing().when(clientService).confirmOrder(any(), eq(warehouse), anyString(), anyString());
        when(clientRepository.findByCode(anyLong())).thenReturn(Optional.ofNullable(Product.builder().name("test").build()));

        ResultActions resultActions = mockMvc.perform(post("/client/confirm-cart")
                .content(asJsonString(new ConfirmOrderRequest("ABC", "ABC", Warehouse.WAREHOUSE1)))
                .cookie(new Cookie("productCodes", "1.1")));

        resultActions.andExpect(status().isOk());
    }

    @Test
    void shoudAddToCart() throws Exception {
        ProductQuantityRequest productQuantityRequest = ProductQuantityRequest.builder()
                .productCode(10l)
                .quantity(15)
                .build();
        when(clientRepository.findByCode(10l)).thenReturn(Optional.of(new Product()));

        mockMvc.perform(post("/client/products/add-to-cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .content(asJsonString(productQuantityRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void products() throws Exception{
        List<Product> products = List.of(Product.builder().code(1l).build(), Product.builder().code(2l).build());
        when(clientRepository.findAllByLockedIs(false)).thenReturn(products);

        mockMvc.perform(get("/client/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(products)));
    }

    @Test
    void cart() throws Exception {
        Product product1 = Product.builder().code(1l).build();
        Product product2 = Product.builder().code(2l).build();

        Map<Product, Integer> cartStorage = Map.of(product1, 2, product2, 3);
        when(clientRepository.findByCode(1l)).thenReturn(Optional.of(product1));
        when(clientRepository.findByCode(5l)).thenReturn(Optional.of(product2));

        ResultActions resultActions = mockMvc.perform(get("/client/cart")
                .cookie(new Cookie("productCodes", "1.2-5.3")));

        resultActions.andExpect(status().isOk())
                .andExpect(content().json(asJsonString(cartStorage)));
    }

    @Test
    void addToCart() throws Exception{
        Product product1 = Product.builder().code(1l).build();
        when(clientRepository.findByCode(1l)).thenReturn(Optional.of(product1));

        mockMvc.perform(post("/client/products/add-to-cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ProductQuantityRequest.builder().quantity(5).productCode(1l).build()))
                        .cookie(new Cookie("productCodes", "6.1")))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added succesfully"));
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}