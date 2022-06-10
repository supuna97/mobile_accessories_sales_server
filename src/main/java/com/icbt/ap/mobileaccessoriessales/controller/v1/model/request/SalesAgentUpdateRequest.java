package com.icbt.ap.mobileaccessoriessales.controller.v1.model.request;

import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@SuperBuilder
public class SalesAgentUpdateRequest {
    @NotBlank(message = ApiConstant.Validation.ID_REQUIRED)
    private String id;
    @NotBlank(message = ApiConstant.Validation.USERNAME_REQUIRED)
    private String username;
    @NotBlank(message = ApiConstant.Validation.PASSWORD_REQUIRED)
    private String password;
    @NotBlank(message = ApiConstant.Validation.BRANCH_REQUIRED)
    private String branchId;
}
