package com.bd2.warehousedistributedsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddToCartRequest {
    private Long productCode;
    private Integer quantity;
}
