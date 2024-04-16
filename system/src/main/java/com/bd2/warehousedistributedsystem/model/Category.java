package com.bd2.warehousedistributedsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "category", schema = "dbo")
public class Category {
    @Id
    private long id;
    private String name;
}
