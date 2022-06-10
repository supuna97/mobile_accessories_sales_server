package com.icbt.ap.mobileaccessoriessales.entity.query;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderResult {
    private String id;
    private String totalAmount;
    private LocalDateTime createdAt;
    private String status;
    private String customerName;
    private String customerMobile;
    private String salesAgentName;
}
