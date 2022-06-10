package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.StockRequestDetail;
import com.icbt.ap.mobileaccessoriessales.repository.StockRequestDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StockRequestDetailRepositoryImpl implements StockRequestDetailRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String STOCK_REQUEST_SELECT = "SELECT * FROM stock_request_detail srd ";

    @Override
    public List<StockRequestDetail> findAll() {
        return jdbcTemplate.query(STOCK_REQUEST_SELECT, new StockRequestDetailRowMapper());
    }

    @Override
    public Optional<StockRequestDetail> findById(String id) {

        String sql = STOCK_REQUEST_SELECT + " WHERE srd.id = ? ";

        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(sql, new StockRequestDetailRowMapper(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(StockRequestDetail stockRequestDetail) {
        jdbcTemplate.update("INSERT INTO stock_request_detail (`id`, `stock_request_id`, `product_id`, `qty`) "
                        + "VALUES (UUID(), ?, ?, ?)",
                stockRequestDetail.getStockRequestId(), stockRequestDetail.getProductId(), stockRequestDetail.getQty());
    }

    @Override
    public void update(StockRequestDetail stockRequestDetail) {
        jdbcTemplate.update("UPDATE stock_request_detail " + " SET stock_request_id = ?, product_id = ?, qty = ? " +
                        " WHERE id = ?",
                stockRequestDetail.getStockRequestId(), stockRequestDetail.getProductId(), stockRequestDetail.getQty(),
                stockRequestDetail.getId());
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("UPDATE stock_request_detail " + " SET qty = ? WHERE id = ?", 0, id);
    }

    @Override
    public List<StockRequestDetail> findAllByStockRequest(String stockRequestId) {

        String sql = STOCK_REQUEST_SELECT + " WHERE srd.stock_request_id = ?";

        return jdbcTemplate.query(sql, new StockRequestDetailRowMapper(), stockRequestId);
    }

    @Override
    public void saveAll(List<StockRequestDetail> stocks) {
        jdbcTemplate.batchUpdate("INSERT INTO stock_request_detail (`id`, `stock_request_id`, `product_id`, `qty`) "
                        + "VALUES (UUID(), ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setString(1, stocks.get(i).getStockRequestId());
                        ps.setString(2, stocks.get(i).getProductId());
                        ps.setInt(3, stocks.get(i).getQty());
                    }

                    public int getBatchSize() {
                        return stocks.size();
                    }
                });
    }

    private static class StockRequestDetailRowMapper implements RowMapper<StockRequestDetail> {
        @Override
        public StockRequestDetail mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return StockRequestDetail.builder()
                    .id(resultSet.getString("id"))
                    .stockRequestId(resultSet.getString("stock_request_id"))
                    .productId(resultSet.getString("product_id"))
                    .qty(resultSet.getInt("qty"))
                    .build();
        }
    }

}
