package com.icbt.ap.mobileaccessoriessales.service;

import com.icbt.ap.mobileaccessoriessales.entity.StockRequest;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockRequestResult;
import com.icbt.ap.mobileaccessoriessales.enums.StockRequestStatus;
import com.icbt.ap.mobileaccessoriessales.service.main.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockRequestService extends CrudService<String, StockRequest> {

    List<StockRequestResult> getAllByRequestedByBranch(String branchId);

    List<StockRequestResult> getAllByRequestedForBranch(String branchId);

    void updateStatus(String stockRequestId, StockRequestStatus status);
}
