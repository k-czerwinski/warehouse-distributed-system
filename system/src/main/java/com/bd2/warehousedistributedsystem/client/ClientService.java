package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.Cart;
import com.bd2.warehousedistributedsystem.model.Warehouse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional()
    public void confirmOrder(Cart cart, Warehouse warehouse, String ordererName, String shippingAddress) {
        cart.getProductsInCart().entrySet().forEach(entry -> {
            clientRepository.decreaseProductCount(entry.getKey().getCode(), entry.getValue(), warehouse.getOfficialName());
        });
        BigDecimal price = cart.getProductsInCart().entrySet().stream().map(entry -> entry.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        Long orderId = clientRepository.createNewOrder(ordererName, shippingAddress, price);
        cart.getProductsInCart().entrySet().forEach(entry -> {
            clientRepository.assignProductToOrder(warehouse.getOfficialName(), orderId, entry.getKey().getCode(), entry.getValue());
        });
    }
}
