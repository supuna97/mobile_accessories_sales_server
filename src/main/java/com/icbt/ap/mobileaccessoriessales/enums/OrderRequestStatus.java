package com.icbt.ap.mobileaccessoriessales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum OrderRequestStatus {
    ORDER_PENDING(0, "Pending"), ORDER_DONE(1, "Done"),
    ORDER_REJECTED(2, "Rejected"), ORDER_DELIVERY(3, "On Delivery");

    private final Integer id;
    private final String description;

    public static List<OrderRequestStatus> getList() {
        return Arrays.asList(OrderRequestStatus.values().clone());
    }

    public static OrderRequestStatus getById(Integer id) {
        return getList()
                .stream()
                .filter(orderRequestStatus -> orderRequestStatus.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}
