package com.bd2.warehousedistributedsystem.model;

import lombok.Getter;

@Getter
public enum Warehouse {
    WAREHOUSE1("warehouse1", 1),
    WAREHOUSE2("warehouse2", 2),
    WAREHOUSE3("warehouse3", 3),
    WAREHOUSE4("warehouse4", 4);
    private final String officialName;
    private final long officialCode;
    Warehouse(String officialName, long officialCode) {
        this.officialName = officialName;
        this.officialCode = officialCode;
    }
}
