package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.Stock;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockResult;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

import java.util.List;

public interface StockRepository extends CrudRepository<String, Stock> {
    List<StockResult> findAllByBranch(String branchId);

    List<StockResult> findAllByProduct(String productId);

    List<Stock> findAllByIdsIn(List<String> stockIds);

    void updateListQty(List<Stock> stocks);
}
