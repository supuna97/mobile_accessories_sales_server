package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.controller.CommonController;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockRequestDetailRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockRequestMakeRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockRequestUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.StockRequestResponse;
import com.icbt.ap.mobileaccessoriessales.dto.CommonResponseDTO;
import com.icbt.ap.mobileaccessoriessales.dto.ContentResponseDTO;
import com.icbt.ap.mobileaccessoriessales.entity.StockRequest;
import com.icbt.ap.mobileaccessoriessales.entity.StockRequestDetail;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockRequestResult;
import com.icbt.ap.mobileaccessoriessales.enums.StockRequestStatus;
import com.icbt.ap.mobileaccessoriessales.service.StockRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = ApiConstant.VERSION + "/stock-request")
@Slf4j
@RequiredArgsConstructor
public class StockRequestController implements CommonController {

    private final StockRequestService stockRequestService;

    private final MessageSource messageSource;

    @GetMapping(path = "")
    public ResponseEntity<ContentResponseDTO<List<StockRequestResponse>>> getStockRequests() {
        log.info("Get all stock requests");
        return getAllStockRequests();
    }

    @GetMapping(path = "/{stockRequestId}")
    public ResponseEntity<ContentResponseDTO<StockRequestResponse>> getStockRequest(
            @PathVariable(name = "stockRequestId") String stockRequestId) {

        log.info("Get stock request by id, StockRequest id: {}", stockRequestId);
        return getStockRequestById(stockRequestId);
    }

    @PostMapping(path = "")
    public ResponseEntity<CommonResponseDTO> saveStockRequest(@Valid @RequestBody StockRequestMakeRequest request) {
        log.info("Add new stock request, StockRequest: {}", request);
        return addNewStockRequest(request);
    }

    @PutMapping(path = "")
    public ResponseEntity<CommonResponseDTO> updateStockRequest(@Valid @RequestBody StockRequestUpdateRequest request) {
        log.info("Update stock request, StockRequest: {}", request);
        return modifyStockRequest(request);
    }

    @DeleteMapping(path = "/{stockRequestId}")
    public ResponseEntity<CommonResponseDTO> deleteStockRequest(
            @PathVariable(name = "stockRequestId") String stockRequestId) {
        log.info("Delete stock request by id, StockRequest id: {}", stockRequestId);
        return deleteStockRequestTmp(stockRequestId);
    }

    @GetMapping(path = "/by-branch/{branchId}")
    public ResponseEntity<ContentResponseDTO<List<StockRequestResponse>>> getAllByRequestedByBranch(
            @PathVariable(name = "branchId") String branchId) {
        log.info("Get all stock requests by-branch: {}", branchId);
        return getAllStockRequestsByBranch(branchId);
    }

    @GetMapping(path = "/for-branch/{branchId}")
    public ResponseEntity<ContentResponseDTO<List<StockRequestResponse>>> getAllByRequestedForBranch(
            @PathVariable(name = "branchId") String branchId) {
        log.info("Get all stock requests for-branch: {}", branchId);
        return getAllStockRequestsForBranch(branchId);
    }

    @PatchMapping(path = "/{stockRequestId}/status/{status}")
    public ResponseEntity<CommonResponseDTO> updateStatus(
            @PathVariable(name = "stockRequestId") String stockRequestId,
            @PathVariable(name = "status") StockRequestStatus status) {
        log.info("Update stock request status by id, StockRequest id: {}", stockRequestId);
        return updateStockRequestStatus(stockRequestId, status);
    }

    /*Internal functions*/

    private ResponseEntity<ContentResponseDTO<List<StockRequestResponse>>> getAllStockRequests() {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getStockRequestResponseList(stockRequestService.getAll())));
    }

    private ResponseEntity<ContentResponseDTO<StockRequestResponse>> getStockRequestById(String stockRequestId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getStockRequestResponse(stockRequestService.getById(stockRequestId))));
    }

    private ResponseEntity<CommonResponseDTO> addNewStockRequest(StockRequestMakeRequest request) {
        stockRequestService.add(getStockRequestSaveEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.added.code"),
                getMessage("success.confirmation.stock.request.added.message")),
                HttpStatus.CREATED);
    }

    private ResponseEntity<CommonResponseDTO> modifyStockRequest(StockRequestUpdateRequest request) {
        stockRequestService.update(getStockRequestUpdateEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.stock.request.updated.message")),
                HttpStatus.OK);
    }

    private ResponseEntity<CommonResponseDTO> deleteStockRequestTmp(String stockRequestId) {
        stockRequestService.delete(stockRequestId);
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.stock.request.deleted.message")),
                HttpStatus.OK);
    }

    private ResponseEntity<CommonResponseDTO> updateStockRequestStatus(
            String stockRequestId, StockRequestStatus status) {
        stockRequestService.updateStatus(stockRequestId, status);
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.stock.request.updated.message")),
                HttpStatus.OK);
    }

    private ResponseEntity<ContentResponseDTO<List<StockRequestResponse>>> getAllStockRequestsByBranch(String branchId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getStockRequestResultResponseList(stockRequestService.getAllByRequestedByBranch(branchId))));
    }

    private ResponseEntity<ContentResponseDTO<List<StockRequestResponse>>> getAllStockRequestsForBranch(String branchId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getStockRequestResultResponseList(stockRequestService.getAllByRequestedForBranch(branchId))));
    }

    private List<StockRequestResponse> getStockRequestResponseList(List<StockRequest> stockrequests) {
        return stockrequests
                .stream()
                .map(this::getStockRequestResponse)
                .collect(Collectors.toList());
    }

    private List<StockRequestResponse> getStockRequestResultResponseList(List<StockRequestResult> stockRequests) {
        return stockRequests
                .stream()
                .map(this::getStockRequestResponse)
                .collect(Collectors.toList());
    }

    private StockRequestResponse getStockRequestResponse(StockRequest stockRequest) {
        return StockRequestResponse.builder()
                .id(stockRequest.getId())
                .byBranchId(stockRequest.getByBranchId())
                .forBranchId(stockRequest.getForBranchId())
                .vehicleId(stockRequest.getVehicleId())
                .byBranchName(stockRequest.getByBranchId())
                .forBranchName(stockRequest.getForBranchId())
                .vehicleReg(stockRequest.getVehicleId())
                .status(stockRequest.getStatus().getDescription())
                .createdAt(getFormattedDateTime(stockRequest.getCreatedAt()))
                .updatedAt(getFormattedDateTime(stockRequest.getUpdatedAt()))
                .build();
    }

    private StockRequestResponse getStockRequestResponse(StockRequestResult stockRequest) {
        return StockRequestResponse.builder()
                .id(stockRequest.getId())
                .byBranchId(stockRequest.getByBranchId())
                .forBranchId(stockRequest.getForBranchId())
                .vehicleId(stockRequest.getVehicleId())
                .byBranchName(stockRequest.getByBranchName())
                .forBranchName(stockRequest.getForBranchName())
                .vehicleReg(stockRequest.getVehicleReg())
                .status(stockRequest.getStatus().getDescription())
                .createdAt(getFormattedDateTime(stockRequest.getCreatedAt()))
                .updatedAt(getFormattedDateTime(stockRequest.getUpdatedAt()))
                .build();
    }

    private StockRequest getStockRequestSaveEntity(StockRequestMakeRequest request) {
        return StockRequest.builder()
                .byBranchId(request.getByBranchId())
                .forBranchId(request.getForBranchId())
                .vehicleId(request.getVehicleId())
                .stockRequestDetails(request.getStockRequestDetails()
                        .stream()
                        .map(this::getStockRequestDetailEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    private StockRequest getStockRequestUpdateEntity(StockRequestUpdateRequest request) {
        return StockRequest.builder()
                .id(request.getId())
                .byBranchId(request.getByBranchId())
                .forBranchId(request.getForBranchId())
                .vehicleId(request.getVehicleId())
                .stockRequestDetails(request.getStockRequestDetails()
                        .stream()
                        .map(this::getStockRequestDetailEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    private StockRequestDetail getStockRequestDetailEntity(StockRequestDetailRequest request) {
        return StockRequestDetail.builder()
                .id(request.getId())
                .productId(request.getProductId())
                .qty(request.getQty())
                .build();
    }

    private String getFormattedDateTime(LocalDateTime dateTime) {
        return dateTime != null ? ApiConstant.DATE_TIME_FORMATTER.format(dateTime) : null;
    }

    private String getCode(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }
}
