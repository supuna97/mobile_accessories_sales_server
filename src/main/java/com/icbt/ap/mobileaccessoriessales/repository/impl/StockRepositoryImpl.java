package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Stock;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockResult;
import com.icbt.ap.mobileaccessoriessales.enums.BranchStatus;
import com.icbt.ap.mobileaccessoriessales.enums.ProductStatus;
import com.icbt.ap.mobileaccessoriessales.repository.StockRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StockRepositoryImpl implements StockRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String BRANCH_AND_PRODUCT_SELECT = "SELECT s.*, b.`name` AS branch_name, p.`name` AS product_name " +
            "FROM stock s INNER JOIN product p on s.product_id = p.id AND p.status = " + ProductStatus.ACTIVE.getId() + " " +
            "INNER JOIN branch b on s.branch_id = b.id AND b.status = " + BranchStatus.ACTIVE.getId();

    @Override
    public List<Stock> findAll() {
        return jdbcTemplate.query(BRANCH_AND_PRODUCT_SELECT, new StockRowMapper());
    }

    @Override
    public Optional<Stock> findById(String id) {

        String sql = BRANCH_AND_PRODUCT_SELECT + " WHERE s.id = ? ";

        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(sql, new StockResultRowMapper(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Stock stock) {
        jdbcTemplate.update("INSERT INTO stock (`id`, `description`, `qty`, `price`, `branch_id`, `product_id`) "
                        + "VALUES (UUID(), ?, ?, ?, ?, ?)",
                stock.getDescription(), stock.getQty(), stock.getPrice(), stock.getBranchId(), stock.getProductId());
    }

    @Override
    public void update(Stock stock) {
        jdbcTemplate.update("UPDATE stock " + " SET description = ?, qty = ?, price = ? , " +
                        "branch_id = ? , product_id = ? " + " WHERE id = ?",
                stock.getDescription(), stock.getQty(), stock.getPrice(),
                stock.getBranchId(), stock.getProductId(), stock.getId());
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("UPDATE stock " + " SET qty = ? WHERE id = ?", 0, id);
    }

    @Override
    public List<StockResult> findAllByBranch(String branchId) {

        String sql = BRANCH_AND_PRODUCT_SELECT + " WHERE s.branch_id = ?";

        return jdbcTemplate.query(sql, new StockResultRowMapper(), branchId);
    }

    @Override
    public List<StockResult> findAllByProduct(String productId) {
        String sql = BRANCH_AND_PRODUCT_SELECT + " WHERE product_id = ?";

        return jdbcTemplate.query(sql, new StockResultRowMapper(), productId);
    }

    @Override
    public List<Stock> findAllByIdsIn(List<String> stockIds) {
        String sql = "SELECT * FROM stock WHERE id IN (%s)";
        String inSql = String.join(",", Collections.nCopies(stockIds.size(), "?"));

        return jdbcTemplate.query(String.format(sql, inSql), new StockRowMapper(), stockIds.toArray());
    }

    @Override
    public void updateListQty(List<Stock> stocks) {
        jdbcTemplate.batchUpdate("UPDATE stock SET qty = ? WHERE id = ? ",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, stocks.get(i).getQty());
                        ps.setString(2, stocks.get(i).getId());
                    }

                    public int getBatchSize() {
                        return stocks.size();
                    }
                });
    }

    private static class StockRowMapper implements RowMapper<Stock> {
        @Override
        public Stock mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return StockResult.builder()
                    .id(resultSet.getString("id"))
                    .description(resultSet.getString("description"))
                    .qty(resultSet.getInt("qty"))
                    .price(resultSet.getBigDecimal("price"))
                    .branchId(resultSet.getString("branch_id"))
                    .productId(resultSet.getString("product_id"))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }

    private static class StockResultRowMapper implements RowMapper<StockResult> {
        @Override
        public StockResult mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return StockResult.builder()
                    .id(resultSet.getString("id"))
                    .description(resultSet.getString("description"))
                    .qty(resultSet.getInt("qty"))
                    .price(resultSet.getBigDecimal("price"))
                    .branchId(resultSet.getString("branch_id"))
                    .branchName(resultSet.getString("branch_name"))
                    .productId(resultSet.getString("product_id"))
                    .productName(resultSet.getString("product_name"))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }
}
