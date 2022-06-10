package com.icbt.ap.mobileaccessoriessales.controller.v1.model.request;

import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@SuperBuilder
public class BranchUpdateRequest {
    @NotBlank(message = ApiConstant.Validation.ID_REQUIRED)
    private String id;
    @NotBlank(message = ApiConstant.Validation.NAME_REQUIRED)
    private String name;
    @NotBlank(message = ApiConstant.Validation.ADDRESS_REQUIRED)
    private String address;
    @NotBlank(message = ApiConstant.Validation.TEL_REQUIRED)
    private String tel;
    private Integer statusId;
}
