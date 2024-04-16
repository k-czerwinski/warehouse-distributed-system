package com.bd2.warehousedistributedsystem.client;

import com.bd2.warehousedistributedsystem.model.Cart;
import com.bd2.warehousedistributedsystem.model.Warehouse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional()
    public void confirmOrder(Cart cart, Warehouse warehouse) {
        cart.getProductsInCart().entrySet().stream().forEach(entry -> {
            switch (warehouse) {
                case WAREHOUSE1 -> clientRepository.confirmProductOrderInWarehouse1(entry.getKey().getCode(), entry.getValue());
                case WAREHOUSE2 -> clientRepository.confirmProductOrderInWarehouse2(entry.getKey().getCode(), entry.getValue());
                case WAREHOUSE3 -> clientRepository.confirmProductOrderInWarehouse3(entry.getKey().getCode(), entry.getValue());
                case WAREHOUSE4 -> clientRepository.confirmProductOrderInWarehouse4(entry.getKey().getCode(), entry.getValue());
            }
        });
    }
}
