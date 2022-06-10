package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.StockRequest;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockRequestResult;
import com.icbt.ap.mobileaccessoriessales.enums.StockRequestStatus;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

import java.util.List;

public interface StockRequestRepository extends CrudRepository<String, StockRequest> {
    String saveAndGetId(StockRequest stock);

    void updateStatus(String id, StockRequestStatus status);

    List<StockRequestResult> findAllByRequestByBranch(String byBranchId);

    List<StockRequestResult> findAllByRequestToBranch(String toBranchId);
}
