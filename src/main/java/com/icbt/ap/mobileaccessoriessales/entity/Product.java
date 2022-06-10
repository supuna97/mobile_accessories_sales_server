package com.icbt.ap.mobileaccessoriessales.entity;

import com.icbt.ap.mobileaccessoriessales.enums.ProductStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class Product {
    private String id;
    @EqualsAndHashCode.Exclude
    private String name;
    private ProductStatus status;
    private LocalDateTime createdAt;
}
