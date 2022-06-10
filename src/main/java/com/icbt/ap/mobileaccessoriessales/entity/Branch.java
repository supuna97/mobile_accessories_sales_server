package com.icbt.ap.mobileaccessoriessales.entity;

import com.icbt.ap.mobileaccessoriessales.enums.BranchStatus;
import com.icbt.ap.mobileaccessoriessales.enums.BranchType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class Branch {
    private String id;
    private String name;
    private String address;
    private String tel;
    private BranchType type;
    private BranchStatus status;
    private LocalDateTime createdAt;
}
