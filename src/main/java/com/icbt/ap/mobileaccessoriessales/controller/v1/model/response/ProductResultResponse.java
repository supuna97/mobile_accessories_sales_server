package com.icbt.ap.mobileaccessoriessales.controller.v1.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ProductResultResponse {
    private String id;
    private String name;
    private Integer qty;
    private String price;
    private String description;
    private String status;
}
