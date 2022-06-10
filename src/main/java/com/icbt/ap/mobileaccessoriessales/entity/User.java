package com.icbt.ap.mobileaccessoriessales.entity;

import com.icbt.ap.mobileaccessoriessales.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor //default constructor
@SuperBuilder //builder design pattern -> object creation readable
public class User {
    private String id;
    private String username;
    private String password;
    private UserRole userRole;
    /*FKs*/
    private String branchId;
}
