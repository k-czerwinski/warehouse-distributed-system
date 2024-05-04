package com.bd2.warehousedistributedsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "product", schema = "dbo")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue
    private long code;
    private String name;
    private BigDecimal price;
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
