package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.controller.CommonController;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.OrderResultResponse;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.OrderTotalAmountBySalesAgentResponse;
import com.icbt.ap.mobileaccessoriessales.dto.CommonResponseDTO;
import com.icbt.ap.mobileaccessoriessales.dto.ContentResponseDTO;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderResult;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderTotalAmountBySalesAgent;
import com.icbt.ap.mobileaccessoriessales.enums.OrderRequestStatus;
import com.icbt.ap.mobileaccessoriessales.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant.VERSION;

@RestController
@RequestMapping(value = VERSION + "/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController implements CommonController {
    private final OrderService orderService;
    private final MessageSource messageSource;

    @GetMapping(path = "")
    public ResponseEntity<ContentResponseDTO<List<OrderResultResponse>>> getOrderResults() {
        log.info("Get all order details");
        return getAllOrderResults();
    }

    @GetMapping(path = "/total_sales_by_sales_agents")
    public ResponseEntity<ContentResponseDTO<List<OrderTotalAmountBySalesAgentResponse>>> getOrderTotalAmountBySalesAgentResults() {
        log.info("Get all order details by sales agent wise");
        return getAllOrderTotalAmountBySalesAgentResults();
    }

    @PatchMapping(path = "/{orderRequestId}/status/{status}")
    public ResponseEntity<CommonResponseDTO> updateStatus(
            @PathVariable(name = "orderRequestId") String orderRequestId,
            @PathVariable(name = "status") OrderRequestStatus status) {
        log.info("Update order request status by id, OrderRequest id: {}", orderRequestId);
        return updateOrderRequestStatus(orderRequestId, status);
    }

    /*Internal functions*/

    private ResponseEntity<ContentResponseDTO<List<OrderResultResponse>>> getAllOrderResults() {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getOrderResultResponseList(orderService.findAllOrder())));
    }

    private ResponseEntity<ContentResponseDTO<List<OrderTotalAmountBySalesAgentResponse>>> getAllOrderTotalAmountBySalesAgentResults() {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getOrderTotalAmountBySalesAgentResultResponseList(orderService.findAllTotalAmountSaleByAgent())));
    }

    private List<OrderResultResponse> getOrderResultResponseList(List<OrderResult> orderResults) {
        return orderResults
                .stream()
                .map(this::getOrderResultResponse)
                .collect(Collectors.toList());
    }

    private ResponseEntity<CommonResponseDTO> updateOrderRequestStatus(
            String orderRequestId, OrderRequestStatus status) {
        orderService.updateStatus(orderRequestId, status);
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.stock.request.updated.message")),
                HttpStatus.OK);
    }

    private OrderResultResponse getOrderResultResponse(OrderResult orderResult) {
        return OrderResultResponse.builder()
                .id(orderResult.getId())
                .totalAmount(orderResult.getTotalAmount())
                .status(orderResult.getStatus())
                .customerName(orderResult.getCustomerName())
                .customerMobile(orderResult.getCustomerMobile())
                .salesAgentName(orderResult.getSalesAgentName())
                .createdAt(orderResult.getCreatedAt())
                .build();
    }

    private List<OrderTotalAmountBySalesAgentResponse> getOrderTotalAmountBySalesAgentResultResponseList(List<OrderTotalAmountBySalesAgent> orderResults) {
        return orderResults
                .stream()
                .map(this::getOrderTotalAmountBySalesAgentResultResponse)
                .collect(Collectors.toList());
    }

    private OrderTotalAmountBySalesAgentResponse getOrderTotalAmountBySalesAgentResultResponse(OrderTotalAmountBySalesAgent orderResult) {
        return OrderTotalAmountBySalesAgentResponse.builder()
                .repId(orderResult.getRepId())
                .repName(orderResult.getRepName())
                .branchId(orderResult.getBranchId())
                .branchName(orderResult.getBranchName())
                .totalOrder(orderResult.getTotalOrder())
                .totalAmount(orderResult.getTotalAmount())
                .build();
    }

    private String getCode(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }
}
