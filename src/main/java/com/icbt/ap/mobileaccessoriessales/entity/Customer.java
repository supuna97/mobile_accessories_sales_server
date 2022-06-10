package com.icbt.ap.mobileaccessoriessales.entity;

import com.icbt.ap.mobileaccessoriessales.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@SuperBuilder
public class Customer {
    private String id;
    private String name;
    private String mobile;
    private String address;
    private String username;
    private String password;
    private UserRole userRole;
}
