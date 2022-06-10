package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.entity.query.ProductResult;
import com.icbt.ap.mobileaccessoriessales.enums.ProductStatus;
import com.icbt.ap.mobileaccessoriessales.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product WHERE status <> ?",
                new ProductRowMapper(), ProductStatus.DELETED.getId());
    }

    @Override
    public Optional<Product> findById(String id) {

        String sql = "SELECT * FROM product WHERE status <> ? AND id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new ProductRowMapper(),
                    ProductStatus.DELETED.getId(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update("INSERT INTO product (id, name, status) " + "VALUES (UUID(), ?, ?)",
                product.getName(), product.getStatus().getId());
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update("UPDATE product " + " SET name = ?, status = ? " + " WHERE id = ?",
                product.getName(), product.getStatus().getId(), product.getId());
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("UPDATE product " + " SET status = ? " + " WHERE id = ?",
                ProductStatus.DELETED.getId(), id);
    }

    @Override
    public Product findByName(String name) {

        String sql = "SELECT * FROM product WHERE status <> ? AND name = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new ProductRowMapper(),
                    ProductStatus.DELETED.getId(), name);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Product> findAllByIdsIn(List<String> productIds) {
        String sql = "SELECT * FROM product WHERE id IN (%s)";
        String inSql = String.join(",", Collections.nCopies(productIds.size(), "?"));

        return jdbcTemplate.query(String.format(sql, inSql), new ProductRowMapper(), productIds.toArray());
    }

    @Override
    public List<ProductResult> findAllProductDetails() {
        return jdbcTemplate.query("SELECT p.id as id, p.name as name, SUM(s.price) as price, s.qty as qty, s.description as description, p.status as status FROM stock s " +
                        "INNER JOIN product p on s.product_id=p.id WHERE p.status = 1 GROUP BY p.id",
                new ProductResultRowMapper());
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return Product.builder()
                    .id(resultSet.getString("id"))
                    .name(resultSet.getString("name"))
                    .status(ProductStatus.getById(resultSet.getInt("status")))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }

    private static class ProductResultRowMapper implements RowMapper<ProductResult> {
        @Override
        public ProductResult mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return ProductResult.builder()
                    .id(resultSet.getString("id"))
                    .name(resultSet.getString("name"))
                    .price(resultSet.getString("price"))
                    .qty(resultSet.getInt("qty"))
                    .status(resultSet.getString("status"))
                    .description(resultSet.getString("description"))
                    .build();
        }
    }
}
