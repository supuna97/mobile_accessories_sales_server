package com.icbt.ap.mobileaccessoriessales.controller.v1.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class BranchResponse {
    private String id;
    private String name;
    private String address;
    private String tel;
    private String type;
    private Integer typeId;
    private String status;
    private Integer statusId;
    private String createdAt;
}
