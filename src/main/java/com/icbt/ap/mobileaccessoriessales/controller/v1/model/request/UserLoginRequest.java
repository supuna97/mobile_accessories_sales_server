package com.icbt.ap.mobileaccessoriessales.controller.v1.model.request;

import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@SuperBuilder
public class UserLoginRequest {
    @NotBlank(message = ApiConstant.Validation.USERNAME_REQUIRED)
    private String username;
    @NotBlank(message = ApiConstant.Validation.PASSWORD_REQUIRED)
    private String password;
}
