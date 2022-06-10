package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.enums.UserRole;
import com.icbt.ap.mobileaccessoriessales.repository.UserRepository;
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
@Slf4j //Causes lombok to generate a logger field
//lombok -  java library that automatically plugs into your editor and build tools, spicing up your java.
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM user", new UserRowMapper());
    }

    @Override
    public Optional<User> findById(String id) {

        String sql = "SELECT * FROM user WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate
                    .queryForObject(sql, new UserRowMapper(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("INSERT INTO user (id, username, password, user_role, branch_id) "
                        + "VALUES (UUID(), ?, ?, ?, ?)",
                user.getUsername(), user.getPassword(), user.getUserRole().getId(), user.getBranchId());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("UPDATE user " + " SET username = ?, password = ? , user_role = ? , branch_id = ? " + " WHERE id = ?",
                user.getUsername(), user.getPassword(), user.getUserRole().getId(), user.getBranchId(), user.getId());
    }

    @Override
    public void delete(String id) {
        jdbcTemplate.update("UPDATE user " + " SET username = null " + " WHERE id = ?", id);
    }

    @Override
    public User findByUserName(String username) {

        String sql = "SELECT * FROM user WHERE user.username = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return User.builder()
                    .id(resultSet.getString("id"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .userRole(UserRole.getById(resultSet.getInt("user_role")))
                    .branchId(resultSet.getString("branch_id"))
                    .build();
        }
    }

}
