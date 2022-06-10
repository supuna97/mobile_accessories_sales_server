package com.icbt.ap.mobileaccessoriessales.controller.v1.model.request;

import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@SuperBuilder
public class StockSaveRequest {
    private String description;
    @NotNull
    @Pattern(message = "branchId must be a number", regexp = ApiConstant.Validation.PATTERN_NUMBER)
    private String qty;
    @Pattern(message = "Price must be a number", regexp = ApiConstant.Validation.PATTERN_DECIMAL)
    private String price;
    @NotBlank(message = ApiConstant.Validation.BRANCH_REQUIRED)
    private String branchId;
    @NotBlank(message = ApiConstant.Validation.PRODUCT_REQUIRED)
    private String productId;
}
