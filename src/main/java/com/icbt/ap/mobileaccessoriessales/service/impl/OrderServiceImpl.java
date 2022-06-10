package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Order;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderResult;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderTotalAmountBySalesAgent;
import com.icbt.ap.mobileaccessoriessales.enums.OrderRequestStatus;
import com.icbt.ap.mobileaccessoriessales.enums.ProductStatus;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.OrderRepository;
import com.icbt.ap.mobileaccessoriessales.repository.impl.ProductRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public void add(Order order) {

    }

    @Override
    public void update(Order order) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Order getById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new CustomServiceException(
                "error.validation.common.not.found.code",
                "error.validation.product.not.found.message"
        ));
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderResult> findAllOrder() {
        return orderRepository.findAllOrder();
    }

    @Override
    public List<OrderTotalAmountBySalesAgent> findAllTotalAmountSaleByAgent() {
        return orderRepository.findAllTotalAmountSaleByAgent();
    }

    @Override
    public void updateStatus(String orderId, OrderRequestStatus status) {
        /*validates the incoming data*/
        final Order orderResult = getById(orderId);
//        if (orderResult == null) {
//            System.out.println("hello");;
//        }
        orderRepository.updateStatus(orderResult.getId(), status);
    }
}
