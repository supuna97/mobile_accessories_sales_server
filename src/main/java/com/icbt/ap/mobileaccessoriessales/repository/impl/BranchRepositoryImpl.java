package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.enums.BranchStatus;
import com.icbt.ap.mobileaccessoriessales.enums.BranchType;
import com.icbt.ap.mobileaccessoriessales.repository.BranchRepository;
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
public class BranchRepositoryImpl implements BranchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Branch> findAll() {
        return jdbcTemplate.query("SELECT * FROM branch WHERE status <> ?",
                new BranchRowMapper(), BranchStatus.DELETED.getId());
    }

    @Override
    public Optional<Branch> findById(String id) {

        String sql = "SELECT * FROM branch WHERE id = ? AND status <> ?";

        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(sql, new BranchRowMapper(), id, BranchStatus.DELETED.getId()));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Branch branch) {
        jdbcTemplate.update("INSERT INTO branch (id, name, address, tel, type) "
                        + "VALUES (UUID(), ?, ?, ?, ?)",
                branch.getName(), branch.getAddress(), branch.getTel(), branch.getType().getId());
    }

    @Override
    public void update(Branch branch) {
        jdbcTemplate.update("UPDATE branch " + " SET name = ?, address = ? , tel = ? , status = ? " + " WHERE id = ?",
                branch.getName(), branch.getAddress(), branch.getTel(), branch.getStatus().getId(), branch.getId());
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("UPDATE branch " + " SET status = ? " + " WHERE id = ?",
                BranchStatus.DELETED.getId(), id);
    }

    @Override
    public Branch findByName(String name) {

        String sql = "SELECT * FROM branch WHERE status <> ? AND name = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new BranchRowMapper(),
                    BranchStatus.DELETED.getId(), name);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Branch findByTel(String tel) {

        String sql = "SELECT * FROM branch WHERE status <> ? AND tel = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new BranchRowMapper(),
                    BranchStatus.DELETED.getId(), tel);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private static class BranchRowMapper implements RowMapper<Branch> {
        @Override
        public Branch mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return Branch.builder()
                    .id(resultSet.getString("id"))
                    .name(resultSet.getString("name"))
                    .address(resultSet.getString("address"))
                    .tel(resultSet.getString("tel"))
                    .type(BranchType.getById(resultSet.getInt("type")))
                    .status(BranchStatus.getById(resultSet.getInt("status")))
                    .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    }

}
