package com.icbt.ap.mobileaccessoriessales.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class Vehicle {
    private String id;
    private String regNo;
    private String driverId;
    private LocalDateTime createdAt;
    /*FKs*/
    private String branchId;
}
