package com.bd2.warehousedistributedsystem.model;

import java.math.BigDecimal;

public record ProductDTO(String name, BigDecimal price, Long categoryId){}
