package com.icbt.ap.mobileaccessoriessales.controller.v1.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class StockResponse {
    private String id;
    private String description;
    private String qty;
    private String price;
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private String createdAt;
}
