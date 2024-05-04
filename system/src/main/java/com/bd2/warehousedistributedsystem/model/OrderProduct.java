package com.bd2.warehousedistributedsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_product", schema = "dbo")
public class OrderProduct {
    @MapsId
    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @MapsId
    @ManyToOne
    @Id
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;
    @Id
    @Column(name = "warehouse_id")
    private long warehouseId;
    private int quantity;
}
