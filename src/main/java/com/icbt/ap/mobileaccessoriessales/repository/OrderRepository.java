package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.Order;
import com.icbt.ap.mobileaccessoriessales.entity.OrderDetail;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderResult;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderTotalAmountBySalesAgent;
import com.icbt.ap.mobileaccessoriessales.enums.OrderRequestStatus;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<String, Order> {
    List<Order> findAllByOrderBySalesAgent(String bySalesAgentId);

    List<OrderResult> findAllOrder();

    List<OrderDetail> findAllOrderDetails();

    List<OrderTotalAmountBySalesAgent> findAllTotalAmountSaleByAgent();

    void updateStatus(String id, OrderRequestStatus status);
}
