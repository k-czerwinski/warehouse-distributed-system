package com.bd2.warehousedistributedsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "product", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long code;
    private String name;
    private BigDecimal price;
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "is_locked")
    private boolean locked;

    @Column(name = "summary_quantity")
    private int quantity;

    public Product(ProductDTO productDTO, Category category) {
        this.name = productDTO.name();
        this.price = productDTO.price();
        this.locked = false;
        this.quantity = 0;
        this.category = category;
    }
}
