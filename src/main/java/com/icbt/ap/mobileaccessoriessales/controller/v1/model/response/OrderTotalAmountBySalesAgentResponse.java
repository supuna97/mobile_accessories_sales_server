package com.icbt.ap.mobileaccessoriessales.controller.v1.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderTotalAmountBySalesAgentResponse {
    private String repId;
    private String repName;
    private String branchId;
    private String branchName;
    private String totalOrder;
    private String totalAmount;
}
