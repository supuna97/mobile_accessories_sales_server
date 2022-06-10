package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Customer;
import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.enums.ProductStatus;
import com.icbt.ap.mobileaccessoriessales.enums.UserRole;
import com.icbt.ap.mobileaccessoriessales.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomerRepositoryImpl implements CustomerRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Customer findByCustomerName(String name) {
        String sql = "SELECT * FROM customer WHERE customer.name = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new CustomerRepositoryImpl.CustomerRowMapper(), name);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("SELECT * FROM customer",
                new CustomerRepositoryImpl.CustomerRowMapper());
    }

    @Override
    public Optional<Customer> findById(String id) {
        String sql = "SELECT * FROM customer WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new CustomerRepositoryImpl.CustomerRowMapper(),id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Customer customer) {
        jdbcTemplate.update("INSERT INTO customer (id, name, mobile, address, username, password) " + "VALUES (UUID(), ?, ?, ?, ?, ?)",
                customer.getName(), customer.getMobile(), customer.getAddress(), customer.getUsername(), customer.getPassword());
    }

    @Override
    public void update(Customer customer) {
        jdbcTemplate.update("UPDATE customer " + " SET name = ?, mobile = ?, address = ?, username = ?, password = ? " + " WHERE id = ?",
                customer.getName(), customer.getMobile(), customer.getAddress(), customer.getUsername(), customer.getPassword(), customer.getId());
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("DELETE FROM customer WHERE id = ?", id);
    }

    private static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return Customer.builder()
                    .id(resultSet.getString("id"))
                    .name(resultSet.getString("name"))
                    .mobile(resultSet.getString("mobile"))
                    .address(resultSet.getString("address"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .userRole(UserRole.getById(resultSet.getInt("user_role")))
                    .build();
        }
    }
}
