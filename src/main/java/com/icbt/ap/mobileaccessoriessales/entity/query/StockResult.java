package com.icbt.ap.mobileaccessoriessales.entity.query;

import com.icbt.ap.mobileaccessoriessales.entity.Stock;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class StockResult extends Stock {
    private String branchName;
    private String productName;
}
