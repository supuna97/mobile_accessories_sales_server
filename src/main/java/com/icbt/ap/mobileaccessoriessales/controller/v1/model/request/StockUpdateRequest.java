package com.icbt.ap.mobileaccessoriessales.controller.v1.model.request;

import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant.Validation.PATTERN_DECIMAL;
import static com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant.Validation.PATTERN_NUMBER;

@Data
@NoArgsConstructor
@SuperBuilder
public class StockUpdateRequest {
    @NotBlank(message = ApiConstant.Validation.ID_REQUIRED)
    private String id;
    private String description;
    @NotNull
    @Pattern(message = "branchId must be a number", regexp = PATTERN_NUMBER)
    private String qty;
    @Pattern(message = "Price must be a number", regexp = PATTERN_DECIMAL)
    private String price;
    @NotBlank(message = ApiConstant.Validation.BRANCH_REQUIRED)
    private String branchId;
    @NotBlank(message = ApiConstant.Validation.PRODUCT_REQUIRED)
    private String productId;
}
