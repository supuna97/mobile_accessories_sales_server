package com.icbt.ap.mobileaccessoriessales.entity.query;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductResult {
    private String id;
    private String name;
    private Integer qty;
    private String price;
    private String description;
    private String status;
}
