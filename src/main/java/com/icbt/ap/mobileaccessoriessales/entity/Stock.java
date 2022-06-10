package com.icbt.ap.mobileaccessoriessales.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class Stock {
    private String id;
    private String description;
    private Integer qty;
    private BigDecimal price;
    private String branchId;
    private String productId;
    private LocalDateTime createdAt;
}
