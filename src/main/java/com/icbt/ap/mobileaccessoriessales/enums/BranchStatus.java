package com.icbt.ap.mobileaccessoriessales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum BranchStatus {

    ACTIVE(1, "Active"), INACTIVE(0, "Inactive"), DELETED(-1, "Deleted");

    private final Integer id;
    private final String description;

    public static List<BranchStatus> getList() {
        return Arrays.asList(BranchStatus.values().clone());
    }

    public static BranchStatus getById(Integer id) {
        return getList()
                .stream()
                .filter(branchStatus -> branchStatus.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}
