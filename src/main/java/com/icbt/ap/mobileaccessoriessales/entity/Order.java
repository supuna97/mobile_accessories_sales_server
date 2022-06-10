package com.icbt.ap.mobileaccessoriessales.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class Order {
    private String id;
    private String totalAmount;
    private LocalDateTime createdAt;
    private String status;
    private String customerId;
    private String salesRepId;
}
