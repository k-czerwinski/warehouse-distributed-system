package com.bd2.warehousedistributedsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductQuantityRequest {
    private Long productCode;
    private Integer quantity;
}
