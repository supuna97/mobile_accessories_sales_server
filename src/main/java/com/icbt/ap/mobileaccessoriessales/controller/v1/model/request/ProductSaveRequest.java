package com.icbt.ap.mobileaccessoriessales.controller.v1.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

import static com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant.Validation;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductSaveRequest {
    @NotBlank(message = Validation.NAME_REQUIRED)
    private String name;
}
