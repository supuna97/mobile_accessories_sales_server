package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockQtyUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.entity.*;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockRequestResult;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockResult;
import com.icbt.ap.mobileaccessoriessales.enums.StockRequestStatus;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.*;
import com.icbt.ap.mobileaccessoriessales.service.*;
import com.icbt.ap.mobileaccessoriessales.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockRequestServiceImpl implements StockRequestService {

    private final StockRequestRepository stockRequestRepository;
    private final StockRequestDetailRepository stockRequestDetailRepository;
    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;
    private final StockRepository stockRepository;

    private final BranchService branchService;
    private final VehicleService vehicleService;
    private final StockService stockService;
    private final ProductService productService;

    @Transactional
    @Override
    public void add(StockRequest stockRequest) {
        /*validates the incoming data*/
        validateStockRequest(stockRequest);

        final String savedId = stockRequestRepository.saveAndGetId(stockRequest);
        /*sets the saved stock request id to details*/
        stockRequest.getStockRequestDetails()
                .forEach(stockRequestDetail -> stockRequestDetail.setStockRequestId(savedId));
        stockRequestDetailRepository.saveAll(stockRequest.getStockRequestDetails());
    }

    @Transactional
    @Override
    public void update(StockRequest stockRequest) {
        /*validates the incoming data*/
        final StockRequest stockRequestById = getById(stockRequest.getId());
        validateStockRequest(stockRequest);

        stockRequestRepository.update(stockRequestById);
    }

    @Override
    public void delete(String id) {
        final StockRequest stockRequest = getById(id);
        stockRequestRepository.delete(stockRequest.getId());
    }

    @Override
    public StockRequest getById(String id) {
        return stockRequestRepository.findById(id).orElseThrow(() -> new CustomServiceException(
                "error.validation.common.not.found.code",
                "error.validation.stock.request.not.found.message"
        ));
    }

    @Override
    public List<StockRequest> getAll() {
        return stockRequestRepository.findAll();
    }

    @Override
    public List<StockRequestResult> getAllByRequestedByBranch(String stockRequestId) {
        return stockRequestRepository.findAllByRequestByBranch(stockRequestId);
    }

    @Override
    public List<StockRequestResult> getAllByRequestedForBranch(String stockRequestId) {
        return stockRequestRepository.findAllByRequestToBranch(stockRequestId);
    }

    @Override
    @Transactional
    public void updateStatus(String stockRequestId, StockRequestStatus status) {
        /*validates the incoming data*/
        final StockRequest stockRequestById = getById(stockRequestId);
        updateStockQty(stockRequestById);
        stockRequestRepository.updateStatus(stockRequestById.getId(), status);
        addNewStockToByBranch(stockRequestById);
    }


    /*Internal functions below*/

    private void validateStockRequest(StockRequest stockRequest) {
        /*checks whether the requested branches and vehicle exist*/
        final Branch byBranch = branchService.getById(stockRequest.getByBranchId());
        final Branch forBranch = branchService.getById(stockRequest.getForBranchId());
        if (StringUtil.isNotBlank(stockRequest.getVehicleId())) {
            final Vehicle vehicle = vehicleService.getById(stockRequest.getVehicleId());
            stockRequest.setVehicleId(vehicle.getId());
        }
        /*explicitly sets IDs*/
        stockRequest.setByBranchId(byBranch.getId());
        stockRequest.setForBranchId(forBranch.getId());
        validateStockDetailsRequest(stockRequest.getStockRequestDetails());
    }

    private void updateStockQty(StockRequest stockRequest) {
        final List<StockRequestDetail> stockRequestDetails = stockRequestDetailRepository
                .findAllByStockRequest(stockRequest.getId());
        stockRequest.setStockRequestDetails(stockRequestDetails);

        stockService.updateStockQty(getStockQtyUpdates(stockRequestDetails));
    }

    private List<StockQtyUpdateRequest> getStockQtyUpdates(List<StockRequestDetail> stockRequestDetails) {
        List<StockQtyUpdateRequest> stockQtyUpdateRequests = new ArrayList<>();
        stockRequestDetails.stream().map(this::getStockQtyUpdateRequest).forEach(stockQtyUpdateRequests::addAll);
        return stockQtyUpdateRequests;
    }

    private List<StockQtyUpdateRequest> getStockQtyUpdateRequest(StockRequestDetail stockRequestDetail) {
        final List<StockResult> stocks = stockService.getAllByProduct(stockRequestDetail.getProductId());
        List<StockQtyUpdateRequest> stockQtyUpdateRequests = new ArrayList<>();
        int qty = stockRequestDetail.getQty();
        for (Stock stock : stocks) {
            if (qty > stock.getQty()) {
                stockQtyUpdateRequests.add(StockQtyUpdateRequest.builder()
                        .id(stock.getId())
                        .qty(stock.getQty() * -1).build());
                qty = qty - stock.getQty();
            } else {
                stockQtyUpdateRequests.add(StockQtyUpdateRequest.builder()
                        .id(stock.getId())
                        .qty(qty * -1).build());
                break;
            }
        }
        return stockQtyUpdateRequests;
    }

    private void validateStockDetailsRequest(List<StockRequestDetail> stockRequestDetails) {
        final List<String> productIdList = stockRequestDetails
                .stream()
                .map(StockRequestDetail::getProductId)
                .collect(Collectors.toList());
        productService.validateAndGetProductsByIds(productIdList);
    }

    private void addNewStockToByBranch(StockRequest stockRequestById) {
        final String byBranchId = stockRequestById.getByBranchId();
        final Branch byBranch = branchService.getById(byBranchId);
        stockRequestById.getStockRequestDetails()
                .stream()
                .map(stockRequestDetail -> getNewAddStock(stockRequestDetail, byBranch))
                .forEach(stockService::add);
    }

    private Stock getNewAddStock(StockRequestDetail stockRequestDetail, Branch byBranch) {
        Stock stock = new Stock();
        stock.setBranchId(byBranch.getId());
        stock.setDescription("New stocks from " + byBranch.getName());
        stock.setQty(stockRequestDetail.getQty());
        stock.setProductId(stockRequestDetail.getProductId());
        stock.setPrice(BigDecimal.ZERO);
        return stock;
    }
}
