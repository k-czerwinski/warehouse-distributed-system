package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.AddToCartRequest;
import com.bd2.warehousedistributedsystem.model.Product;
import com.bd2.warehousedistributedsystem.model.Warehouse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
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

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/client/confirm-order")
                .param("warehouse", warehouse.toString()));

        resultActions.andExpect(status().isOk())
                .andExpect(view().name("client/cart"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    void shoudAddToCart() throws Exception {
        AddToCartRequest addToCartRequest = AddToCartRequest.builder()
                .productCode(10l)
                .quantity(15)
                .build();
        when(clientRepository.findByCode(10l)).thenReturn(Optional.of(new Product()));

        mockMvc.perform(post("/client/products/add-to-cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL)
                        .content(asJsonString(addToCartRequest)))
                .andExpect(status().isOk());
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}