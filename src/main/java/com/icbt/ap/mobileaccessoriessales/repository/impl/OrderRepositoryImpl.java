package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Order;
import com.icbt.ap.mobileaccessoriessales.entity.OrderDetail;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderResult;
import com.icbt.ap.mobileaccessoriessales.entity.query.OrderTotalAmountBySalesAgent;
import com.icbt.ap.mobileaccessoriessales.enums.OrderRequestStatus;
import com.icbt.ap.mobileaccessoriessales.enums.ProductStatus;
import com.icbt.ap.mobileaccessoriessales.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Order> findAllByOrderBySalesAgent(String bySalesAgentId) {
        String sql = "SELECT o.*, od.*, c.`name` AS customer_name, c.`mobile` AS customer_mobile, c.`address` AS customer_address, u.username AS sale_rep_name" +
                "FROM orders o INNER JOIN customer c on o.customer_id =c.id " +
                "INNER JOIN user u on o.sales_rep_id = u.id" +
                "WHERE o.sales_rep_id = ?";
//        String inSql = String.join(",", Collections.nCopies(bySalesAgentId.size(), "?"));
        return null;
    }

    @Override
    public List<Order> findAll() {

        return jdbcTemplate.query("SELECT * FROM orders", new OrderRowMapper());
    }

    @Override
    public List<OrderResult> findAllOrder() {
        return jdbcTemplate.query("SELECT o.id AS order_id, o.status AS status, o.total_amount AS total_amount, o.created_at AS order_date, \n" +
                "                c.name AS customer_name, c.mobile AS customer_mobile, u.username AS sales_agent_name \n" +
                "                FROM orders o INNER JOIN customer c on o.customer_id=c.id\n" +
                "                INNER JOIN user u on o.sales_rep_id=u.id", new OrderResultRowMapper());
    }

    @Override
    public List<OrderDetail> findAllOrderDetails() {
        return null;
    }

    @Override
    public List<OrderTotalAmountBySalesAgent> findAllTotalAmountSaleByAgent() {
        return jdbcTemplate.query("SELECT u.id AS id, u.username AS agent_name, u.branch_id AS branch_id,\n" +
                        "                        b.name AS branch_name, COUNT(o.id) AS total_order, SUM(o.total_amount) AS total_amount\n" +
                        "                        FROM orders o INNER JOIN user u on o.sales_rep_id=u.id\n" +
                        "                        INNER JOIN branch b on u.branch_id = b.id\n" +
                        "                        GROUP BY o.sales_rep_id"
                , new OrderTotalBySalesAgentRowMapper());
    }

    @Override
    public void updateStatus(String id, OrderRequestStatus status) {
        jdbcTemplate.update("UPDATE orders " + " SET status = ? WHERE id = ?",
                status.getId(), id);
    }

    @Override
    public Optional<Order> findById(String id) {

        String sql = "SELECT * FROM orders WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new OrderRepositoryImpl.OrderRowMapper(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Order entity) {

    }

    @Override
    public void update(Order entity) {

    }

    @Override
    public void delete(String id) {

    }

    private static class OrderResultRowMapper implements RowMapper<OrderResult> {
        @Override
        public OrderResult mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return OrderResult.builder()
                    .id(resultSet.getString("order_id"))
                    .totalAmount(resultSet.getString("total_amount"))
                    .createdAt(resultSet.getTimestamp("order_date").toLocalDateTime())
                    .status(resultSet.getString("status"))
                    .customerName(resultSet.getString("customer_name"))
                    .customerMobile(resultSet.getString("customer_mobile"))
                    .salesAgentName(resultSet.getString("sales_agent_name"))
                    .build();
        }
    }

    private static class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return Order.builder()
                    .id(resultSet.getString("id"))
                    .totalAmount(resultSet.getString("total_amount"))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .status(resultSet.getString("status"))
                    .customerId(resultSet.getString("customer_id"))
                    .salesRepId(resultSet.getString("sales_rep_id"))
                    .build();
        }
    }

    private static class OrderTotalBySalesAgentRowMapper implements RowMapper<OrderTotalAmountBySalesAgent> {
        @Override
        public OrderTotalAmountBySalesAgent mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return OrderTotalAmountBySalesAgent.builder()
                    .repId(resultSet.getString("id"))
                    .repName(resultSet.getString("agent_name"))
                    .branchId(resultSet.getString("branch_id"))
                    .branchName(resultSet.getString("branch_name"))
                    .totalOrder(resultSet.getString("total_order"))
                    .totalAmount(resultSet.getString("total_amount"))
                    .build();
        }
    }

}
