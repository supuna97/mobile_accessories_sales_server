package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.StockRequestDetail;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

import java.util.List;

public interface StockRequestDetailRepository extends CrudRepository<String, StockRequestDetail> {
    List<StockRequestDetail> findAllByStockRequest(String stockRequestId);

    void saveAll(List<StockRequestDetail> stocks);
}
