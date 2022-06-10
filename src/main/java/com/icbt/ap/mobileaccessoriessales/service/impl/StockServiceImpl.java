package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockQtyUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.entity.Stock;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockResult;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.StockRepository;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
import com.icbt.ap.mobileaccessoriessales.service.ProductService;
import com.icbt.ap.mobileaccessoriessales.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    private final ProductService productService;
    private final BranchService branchService;

    @Override
    public void add(Stock stock) {
        /*checks whether the requested stock foreign table data exists*/
        final Branch branch = branchService.getById(stock.getBranchId());
        final Product product = productService.getById(stock.getProductId());

        /*explicitly re-assigns the FK ids*/
        stock.setBranchId(branch.getId());
        stock.setProductId(product.getId());

        stockRepository.save(stock);
    }

    @Override
    public void update(Stock stock) {
        /*validates the incoming data*/
        final Stock stockById = getById(stock.getId());

        stockById.setDescription(stock.getDescription());
        stockById.setPrice(stock.getPrice());
        stockById.setQty(stock.getQty());
        /*checks and validates whether the foreign tables are requested to change*/
        if (stock.getBranchId() != null) {
            final Branch branch = branchService.getById(stock.getBranchId());
            stockById.setBranchId(branch.getId());
        }
        if (stock.getProductId() != null) {
            final Product product = productService.getById(stock.getProductId());
            stock.setProductId(product.getId());
        }

        stockRepository.update(stockById);
    }

    @Override
    public void delete(String id) {
        final Stock stock = getById(id);
        stockRepository.delete(stock.getId());
    }

    @Override
    public Stock getById(String id) {
        return stockRepository.findById(id).orElseThrow(() -> new CustomServiceException("error.validation.common.not.found.code", "error.validation.stock.not.found.message"));
    }

    @Override
    public List<Stock> getAll() {
        return stockRepository.findAll();
    }

    @Override
    public List<StockResult> getAllByBranch(String branchId) {
        return stockRepository.findAllByBranch(branchId);
    }

    @Override
    public List<StockResult> getAllByProduct(String productId) {
        return stockRepository.findAllByProduct(productId);
    }

    /**
     * @param qtyUpdateRequests the stock id and the qty that needs to increase/decrease from existing qty.
     */
    @Override
    public void updateStockQty(List<StockQtyUpdateRequest> qtyUpdateRequests) {
        final List<String> stockIds = qtyUpdateRequests.stream().map(StockQtyUpdateRequest::getId).collect(Collectors.toList());
        final List<Stock> stockListByIds = validateAndGetStocksByIds(stockIds);

        /*filters and sets qty for the corresponding id*/
        stockListByIds.forEach(stock -> {
            final Optional<StockQtyUpdateRequest> updateRequestOptional = qtyUpdateRequests.stream().filter(qtyUpdateRequest -> qtyUpdateRequest.getId().equals(stock.getId())).findFirst();
            if (updateRequestOptional.isEmpty()) return;

            /*calculation is always a plus (+) operation,
            therefore a qty decrease request should send minus values*/
            stock.setQty(stock.getQty() + updateRequestOptional.get().getQty());
        });
        /*updates entire list*/
        stockRepository.updateListQty(stockListByIds);
    }

    @Override
    public List<Stock> validateAndGetStocksByIds(List<String> stockIds) {
        final List<Stock> stockListByIds = stockRepository.findAllByIdsIn(stockIds);

        /*validates whether all the requested ids are available in the result*/
        validateStockReqAndResult(stockIds, stockListByIds);
        return stockListByIds;
    }

    /*Internal functions below*/

    private void validateStockReqAndResult(List<String> stockIdsReq, List<Stock> stocks) {
        stockIdsReq.forEach(stockId -> {
            if (stocks.stream().noneMatch(stock -> stock.getId().equals(stockId))) {
                throw new CustomServiceException("error.validation.common.not.found.code", "error.validation.stock.id.not.found.message", new String[]{stockId});
            }
        });
    }


}
