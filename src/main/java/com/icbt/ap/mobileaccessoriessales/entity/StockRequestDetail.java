package com.icbt.ap.mobileaccessoriessales.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class StockRequestDetail {
    private String id;
    private Integer qty;
    /*FKs*/
    private String stockRequestId;
    private String productId;
}
