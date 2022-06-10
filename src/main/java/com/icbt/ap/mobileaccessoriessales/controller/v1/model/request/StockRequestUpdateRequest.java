package com.icbt.ap.mobileaccessoriessales.controller.v1.model.request;

import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class StockRequestUpdateRequest {
    @NotBlank(message = ApiConstant.Validation.ID_REQUIRED)
    private String id;
    @NotBlank(message = ApiConstant.Validation.BY_BRANCH_REQUIRED)
    private String byBranchId;
    @NotBlank(message = ApiConstant.Validation.FOR_BRANCH_REQUIRED)
    private String forBranchId;
    private String vehicleId;
    private List<StockRequestDetailRequest> stockRequestDetails;
}
