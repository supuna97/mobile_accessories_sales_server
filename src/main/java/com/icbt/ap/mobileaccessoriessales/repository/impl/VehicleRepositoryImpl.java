package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Vehicle;
import com.icbt.ap.mobileaccessoriessales.repository.VehicleRepository;
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
public class VehicleRepositoryImpl implements VehicleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Vehicle> findAll() {
        return jdbcTemplate.query("SELECT * FROM vehicle ",
                new VehicleRowMapper());
    }

    @Override
    public Optional<Vehicle> findById(String id) {

        String sql = "SELECT * FROM vehicle WHERE id = ? ";

        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(sql, new VehicleRowMapper(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Vehicle vehicle) {
        jdbcTemplate.update("INSERT INTO vehicle (id, reg_no, driver_id, branch_id) "
                        + "VALUES (UUID(), ?, ?, ?)",
                vehicle.getRegNo(), vehicle.getDriverId(), vehicle.getBranchId());
    }

    @Override
    public void update(Vehicle vehicle) {
        jdbcTemplate.update("UPDATE vehicle " + " SET reg_no = ?, driver_id = ? , branch_id = ? " + " WHERE id = ?",
                vehicle.getRegNo(), vehicle.getDriverId(), vehicle.getBranchId(), vehicle.getId());
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("UPDATE vehicle " + " SET reg_no = ? " + " WHERE id = ?",
                null, id);
    }

    @Override
    public Vehicle findByRegNo(String regNo) {

        String sql = "SELECT * FROM vehicle WHERE reg_no = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new VehicleRowMapper(), regNo);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }


    private static class VehicleRowMapper implements RowMapper<Vehicle> {
        @Override
        public Vehicle mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return Vehicle.builder()
                    .id(resultSet.getString("id"))
                    .regNo(resultSet.getString("reg_no"))
                    .driverId(resultSet.getString("driver_id"))
                    .branchId(resultSet.getString("branch_id"))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }

}
