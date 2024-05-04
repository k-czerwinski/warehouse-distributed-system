package com.bd2.warehousedistributedsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name = "purchase_order", schema = "dbo")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private Timestamp date;
    private BigDecimal price;
    @Column(name = "orderer_name")
    private String ordererName;
    @Column(name = "shipping_address")
    private String shippingAddress;

    @OneToMany(mappedBy = "purchaseOrder")
    @ToString.Exclude
    private List<OrderProduct> products;

    public PurchaseOrder(Timestamp date, BigDecimal price, String ordererName, String shippingAddress) {
        this.date = date;
        this.price = price;
        this.ordererName = ordererName;
        this.shippingAddress = shippingAddress;
    }
}
