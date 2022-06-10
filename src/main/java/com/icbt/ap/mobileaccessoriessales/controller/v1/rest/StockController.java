package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.controller.CommonController;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockQtyUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.StockSaveRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.StockResponse;
import com.icbt.ap.mobileaccessoriessales.dto.CommonResponseDTO;
import com.icbt.ap.mobileaccessoriessales.dto.ContentResponseDTO;
import com.icbt.ap.mobileaccessoriessales.entity.Stock;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockResult;
import com.icbt.ap.mobileaccessoriessales.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = ApiConstant.VERSION + "/stock")
@Slf4j
@RequiredArgsConstructor
public class StockController implements CommonController {

    private final StockService stockService;

    private final MessageSource messageSource;

    @GetMapping(path = "")
    public ResponseEntity<ContentResponseDTO<List<StockResponse>>> getStocks() {
        log.info("Get all stocks");
        return getAllStocks();
    }

    @GetMapping(path = "/{stockId}")
    public ResponseEntity<ContentResponseDTO<StockResponse>> getStock(
            @PathVariable(name = "stockId") String stockId) {

        log.info("Get stock by id, Stock id: {}", stockId);
        return getStockById(stockId);
    }

    @GetMapping(path = "/branch/{branchId}")
    public ResponseEntity<ContentResponseDTO<List<StockResponse>>> getStocksByBranch(
            @PathVariable(name = "branchId") String branchId) {
        log.info("Get all stocks");
        return getAllStocksByBranch(branchId);
    }

    @GetMapping(path = "/product/{productId}")
    public ResponseEntity<ContentResponseDTO<List<StockResponse>>> getStocksBtProduct(
            @PathVariable(name = "productId") String productId) {
        log.info("Get all stocks");
        return getAllStocksByProduct(productId);
    }

    @PostMapping(path = "")
    public ResponseEntity<CommonResponseDTO> saveStock(@Valid @RequestBody StockSaveRequest request) {
        log.info("Add new stock, Stock: {}", request);
        return addNewStock(request);
    }

    @PutMapping(path = "")
    public ResponseEntity<CommonResponseDTO> updateStock(@Valid @RequestBody StockUpdateRequest request) {
        log.info("Update stock, Stock: {}", request);
        return modifyStock(request);
    }

    @DeleteMapping(path = "/{stockId}")
    public ResponseEntity<CommonResponseDTO> deleteStock(@PathVariable(name = "stockId") String stockId) {
        log.info("Delete stock by id, Stock id: {}", stockId);
        return deleteStockTmp(stockId);
    }

    @PutMapping(path = "/qty")
    public ResponseEntity<CommonResponseDTO> updateStockQty(@Valid @RequestBody List<StockQtyUpdateRequest> request) {
        log.info("Update stock qty, Stock details: {}", request);
        return modifyStockQty(request);
    }


    /*Internal functions*/

    private ResponseEntity<ContentResponseDTO<List<StockResponse>>> getAllStocks() {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getStockResponseList(stockService.getAll())));
    }

    private ResponseEntity<ContentResponseDTO<StockResponse>> getStockById(String stockId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getStockResponse(stockService.getById(stockId))));
    }

    private ResponseEntity<ContentResponseDTO<List<StockResponse>>> getAllStocksByBranch(String branchId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getStockResultResponseList(stockService.getAllByBranch(branchId))));
    }

    private ResponseEntity<ContentResponseDTO<List<StockResponse>>> getAllStocksByProduct(String productId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getStockResultResponseList(stockService.getAllByProduct(productId))));
    }

    private ResponseEntity<CommonResponseDTO> addNewStock(StockSaveRequest request) {
        stockService.add(getStockSaveEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.added.code"),
                getMessage("success.confirmation.stock.added.message")),
                HttpStatus.CREATED);
    }

    private ResponseEntity<CommonResponseDTO> modifyStock(StockUpdateRequest request) {
        stockService.update(getStockUpdateEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.stock.updated.message")),
                HttpStatus.OK);
    }

    private ResponseEntity<CommonResponseDTO> modifyStockQty(List<StockQtyUpdateRequest> request) {
        stockService.updateStockQty(request);
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.stock.qty.updated.message")),
                HttpStatus.OK);
    }

    private ResponseEntity<CommonResponseDTO> deleteStockTmp(String stockId) {
        stockService.delete(stockId);
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.stock.deleted.message")),
                HttpStatus.OK);
    }

    private List<StockResponse> getStockResponseList(List<Stock> stocks) {
        return stocks
                .stream()
                .map(this::getStockResponse)
                .collect(Collectors.toList());
    }

    private StockResponse getStockResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .description(stock.getDescription())
                .qty(String.valueOf(stock.getQty()))
                .price(getFormattedAmount(stock.getPrice()))
                .branchId(stock.getBranchId())
                .productId(stock.getProductId())
                .createdAt(getFormattedDateTime(stock.getCreatedAt()))
                .build();
    }

    private List<StockResponse> getStockResultResponseList(List<StockResult> stockResults) {
        return stockResults
                .stream()
                .map(this::getStockResultResponse)
                .collect(Collectors.toList());
    }

    private StockResponse getStockResultResponse(StockResult stockResult) {
        return StockResponse.builder()
                .id(stockResult.getId())
                .description(stockResult.getDescription())
                .qty(String.valueOf(stockResult.getQty()))
                .price(getFormattedAmount(stockResult.getPrice()))
                .branchId(stockResult.getBranchId())
                .branchName(stockResult.getBranchName())
                .productId(stockResult.getProductId())
                .productName(stockResult.getProductName())
                .createdAt(getFormattedDateTime(stockResult.getCreatedAt()))
                .build();
    }

    private Stock getStockSaveEntity(StockSaveRequest request) {
        return Stock.builder()
                .description(request.getDescription())
                .qty(Integer.valueOf(request.getQty()))
                .price(new BigDecimal(request.getPrice()))
                .productId(request.getProductId())
                .branchId(request.getBranchId())
                .build();
    }

    private Stock getStockUpdateEntity(StockUpdateRequest request) {
        return Stock.builder()
                .id(request.getId())
                .description(request.getDescription())
                .qty(Integer.valueOf(request.getQty()))
                .price(new BigDecimal(request.getPrice()))
                .productId(request.getProductId())
                .branchId(request.getBranchId())
                .build();
    }

    private String getFormattedDateTime(LocalDateTime dateTime) {
        return ApiConstant.DATE_TIME_FORMATTER.format(dateTime);
    }

    private String getFormattedAmount(BigDecimal amount) {
        return String.valueOf(amount);
    }

    private String getCode(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }
}
