package com.icbt.ap.mobileaccessoriessales.entity.query;

import com.icbt.ap.mobileaccessoriessales.entity.StockRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class StockRequestResult extends StockRequest {
    private String byBranchName;
    private String forBranchName;
    private String vehicleReg;
}
