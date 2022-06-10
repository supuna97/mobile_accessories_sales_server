package com.icbt.ap.mobileaccessoriessales.entity;

import com.icbt.ap.mobileaccessoriessales.enums.StockRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class StockRequest {
    private String id;
    private StockRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    /*FKs*/
    private String byBranchId;
    private String forBranchId;
    private String vehicleId;
    /*Child tables*/
    private List<StockRequestDetail> stockRequestDetails;
}
