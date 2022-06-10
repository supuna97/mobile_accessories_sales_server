package com.icbt.ap.mobileaccessoriessales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum StockRequestStatus {

    PENDING(0, "Pending"), DONE(1, "Done"),
    REJECTED(2, "Rejected"), ON_DELIVERY(3, "On Delivery");

    private final Integer id;
    private final String description;

    public static List<StockRequestStatus> getList() {
        return Arrays.asList(StockRequestStatus.values().clone());
    }

    public static StockRequestStatus getById(Integer id) {
        return getList()
                .stream()
                .filter(stockRequestStatus -> stockRequestStatus.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}
