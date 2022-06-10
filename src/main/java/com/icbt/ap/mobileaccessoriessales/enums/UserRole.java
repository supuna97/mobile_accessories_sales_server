package com.icbt.ap.mobileaccessoriessales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum UserRole {

    HEAD_OFFICE_ADMIN(1, "Admin"), BRANCH_ADMIN(2, "Sales Agent"),
    SUPPLIER(3,"Supplier"), DELETED(-1, "Deleted"), CUSTOMER(4, "Customer");

    private final Integer id;
    private final String description;

    public static List<UserRole> getList() {
        return Arrays.asList(UserRole.values().clone());
    }

    public static UserRole getById(Integer id) {
        return getList()
                .stream()
                .filter(userRole -> userRole.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}
