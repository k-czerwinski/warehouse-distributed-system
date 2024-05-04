package com.bd2.warehousedistributedsystem.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddToCartRequest {
    private Long productCode;
    private Integer quantity;
}
