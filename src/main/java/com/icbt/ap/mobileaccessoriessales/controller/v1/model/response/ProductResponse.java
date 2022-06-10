package com.icbt.ap.mobileaccessoriessales.controller.v1.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductResponse {
    private String id;
    private String name;
    private String status;
    private Integer statusId;
    private String createdAt;
}
