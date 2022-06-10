package com.icbt.ap.mobileaccessoriessales.controller.v1.model.request;

import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductUpdateRequest {
    @NotBlank(message = ApiConstant.Validation.ID_REQUIRED)
    private String id;
    @NotBlank(message = ApiConstant.Validation.NAME_REQUIRED)
    private String name;
    private Integer statusId;
}
