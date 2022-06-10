package com.icbt.ap.mobileaccessoriessales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    ACTIVE(1, "Active"), INACTIVE(0, "Inactive"), DELETED(-1, "Deleted");

    private final Integer id;
    private final String description;

    public static List<ProductStatus> getList() {
        return Arrays.asList(ProductStatus.values().clone());
    }

    public static ProductStatus getById(Integer id) {
        return getList()
                .stream()
                .filter(productStatus -> productStatus.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}
