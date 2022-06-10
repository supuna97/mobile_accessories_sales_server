package com.icbt.ap.mobileaccessoriessales.service;

import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockQtyUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.entity.Stock;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockResult;
import com.icbt.ap.mobileaccessoriessales.service.main.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockService extends CrudService<String, Stock> {

    List<StockResult> getAllByBranch(String branchId);

    List<StockResult> getAllByProduct(String productId);

    void updateStockQty(List<StockQtyUpdateRequest> qtyUpdateRequests);

    List<Stock> validateAndGetStocksByIds(List<String> stockIds);
}
