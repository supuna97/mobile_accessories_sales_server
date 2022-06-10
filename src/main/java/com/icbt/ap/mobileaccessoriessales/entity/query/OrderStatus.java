package com.icbt.ap.mobileaccessoriessales.entity.query;

import com.icbt.ap.mobileaccessoriessales.enums.OrderRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderStatus {
    private String id;
    private String totalAmount;
    private LocalDateTime createdAt;
    private OrderRequestStatus status;
    private String customerName;
    private String customerMobile;
}
