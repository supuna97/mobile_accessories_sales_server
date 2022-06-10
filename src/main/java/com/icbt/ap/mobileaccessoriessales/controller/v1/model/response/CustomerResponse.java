package com.icbt.ap.mobileaccessoriessales.controller.v1.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class CustomerResponse {
    private String id;
    private String name;
    private String mobile;
    private String address;
    private String username;
}
