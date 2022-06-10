package com.icbt.ap.mobileaccessoriessales.service;

import com.icbt.ap.mobileaccessoriessales.entity.Order;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderResult;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderTotalAmountBySalesAgent;
import com.icbt.ap.mobileaccessoriessales.enums.OrderRequestStatus;
import com.icbt.ap.mobileaccessoriessales.enums.StockRequestStatus;
import com.icbt.ap.mobileaccessoriessales.service.main.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService extends CrudService<String, Order> {
    List<OrderResult> findAllOrder();

    List<OrderTotalAmountBySalesAgent> findAllTotalAmountSaleByAgent();

    void updateStatus(String orderId, OrderRequestStatus status);
}
