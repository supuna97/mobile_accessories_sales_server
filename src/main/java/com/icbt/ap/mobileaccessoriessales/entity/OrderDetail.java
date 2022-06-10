package com.icbt.ap.mobileaccessoriessales.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderDetail {
    private String id;
    private LocalDateTime createdAt;
    private String orderId;
    private String stockId;
}
