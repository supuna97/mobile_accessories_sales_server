package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.StockRequest;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockRequestResult;
import com.icbt.ap.mobileaccessoriessales.enums.BranchStatus;
import com.icbt.ap.mobileaccessoriessales.enums.StockRequestStatus;
import com.icbt.ap.mobileaccessoriessales.repository.StockRequestRepository;
import com.icbt.ap.mobileaccessoriessales.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StockRequestRepositoryImpl implements StockRequestRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String STOCK_REQUEST_STOCK_BRANCH = "SELECT sr.*, v.`reg_no` AS vehicle_reg, " +
            "bb.`name` AS by_branch_name, fb.`name` AS for_branch_name " +
            "FROM stock_request sr " +
            "INNER JOIN branch bb on sr.by_branch_id = bb.id AND bb.status = " + BranchStatus.ACTIVE.getId() + " " +
            "INNER JOIN branch fb on sr.for_branch_id = fb.id AND fb.status = " + BranchStatus.ACTIVE.getId() + " " +
            "LEFT JOIN vehicle v on sr.vehicle_id = v.id ";

    @Override
    public List<StockRequest> findAll() {
        return jdbcTemplate.query(STOCK_REQUEST_STOCK_BRANCH, new StockRequestRowMapper());
    }

    @Override
    public Optional<StockRequest> findById(String id) {

        String sql = STOCK_REQUEST_STOCK_BRANCH + " WHERE sr.id = ? ";

        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(sql, new StockRequestResultRowMapper(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(StockRequest stockRequest) {
        jdbcTemplate.update("INSERT INTO stock_request (`id`, `by_branch_id`, `for_branch_id`, `vehicle_id`) "
                        + "VALUES (UUID(), ?, ?, ?)",
                stockRequest.getByBranchId(), stockRequest.getForBranchId(), stockRequest.getVehicleId());
    }

    @Override
    public String saveAndGetId(StockRequest stockRequest) {
        final String insertId = UUID.randomUUID().toString();
        jdbcTemplate.update(connection -> {

            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO stock_request (`id`, `by_branch_id`, `for_branch_id`, `vehicle_id`) "
                            + "VALUES (?, ?, ?, ?)", new String[]{"id"});

            preparedStatement.setString(1, insertId);
            preparedStatement.setString(2, stockRequest.getByBranchId());
            preparedStatement.setString(3, stockRequest.getForBranchId());
            preparedStatement.setString(4, StringUtil.isNotBlank(stockRequest.getVehicleId()) ?
                    stockRequest.getVehicleId() : null);

            return preparedStatement;

        });
        return insertId;
    }

    @Override
    public void update(StockRequest stockRequest) {
        jdbcTemplate.update("UPDATE stock_request " + " SET by_branch_id = ?, for_branch_id = ?, vehicle_id = ? " +
                        " WHERE id = ?",
                stockRequest.getByBranchId(), stockRequest.getForBranchId(), stockRequest.getVehicleId(),
                stockRequest.getId());
    }

    @Override
    public void updateStatus(String id, StockRequestStatus status) {
        jdbcTemplate.update("UPDATE stock_request " + " SET status = ? WHERE id = ?",
                status.getId(), id);
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("UPDATE stock_request " + " SET status = ? WHERE id = ?",
                StockRequestStatus.REJECTED.getId(), id);
    }

    @Override
    public List<StockRequestResult> findAllByRequestByBranch(String byBranchId) {

        String sql = STOCK_REQUEST_STOCK_BRANCH + " WHERE sr.by_branch_id = ?";

        return jdbcTemplate.query(sql, new StockRequestResultRowMapper(), byBranchId);
    }

    @Override
    public List<StockRequestResult> findAllByRequestToBranch(String toBranchId) {

        String sql = STOCK_REQUEST_STOCK_BRANCH + " WHERE sr.for_branch_id = ?";

        return jdbcTemplate.query(sql, new StockRequestResultRowMapper(), toBranchId);
    }

    private static class StockRequestRowMapper implements RowMapper<StockRequest> {
        @Override
        public StockRequest mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return StockRequest.builder()
                    .id(resultSet.getString("id"))
                    .status(StockRequestStatus.getById(resultSet.getInt("status")))
                    .byBranchId(resultSet.getString("by_branch_id"))
                    .forBranchId(resultSet.getString("for_branch_id"))
                    .vehicleId(resultSet.getString("vehicle_id"))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt((resultSet.getTimestamp("updated_at") != null) ?
                            resultSet.getTimestamp("updated_at").toLocalDateTime() : null)
                    .build();
        }
    }

    private static class StockRequestResultRowMapper implements RowMapper<StockRequestResult> {
        @Override
        public StockRequestResult mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return StockRequestResult.builder()
                    .id(resultSet.getString("id"))
                    .status(StockRequestStatus.getById(resultSet.getInt("status")))
                    .byBranchId(resultSet.getString("by_branch_id"))
                    .forBranchId(resultSet.getString("for_branch_id"))
                    .vehicleId(resultSet.getString("vehicle_id"))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt((resultSet.getTimestamp("updated_at") != null) ?
                            resultSet.getTimestamp("updated_at").toLocalDateTime() : null)
                    .byBranchName(resultSet.getString("by_branch_name"))
                    .forBranchName(resultSet.getString("for_branch_name"))
                    .vehicleReg(resultSet.getString("vehicle_reg"))
                    .build();
        }
    }
}
