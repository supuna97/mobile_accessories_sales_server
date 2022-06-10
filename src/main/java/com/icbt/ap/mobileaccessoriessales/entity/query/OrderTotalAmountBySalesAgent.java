package com.icbt.ap.mobileaccessoriessales.entity.query;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class OrderTotalAmountBySalesAgent {
    private String repId;
    private String repName;
    private String branchId;
    private String branchName;
    private String totalOrder;
    private String totalAmount;
}
