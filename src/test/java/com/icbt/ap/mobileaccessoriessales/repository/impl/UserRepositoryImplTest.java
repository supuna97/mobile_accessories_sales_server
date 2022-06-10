package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserRepositoryImplTest {

    private final UserRepository userRepository;

    public UserRepositoryImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        userRepository = new UserRepositoryImpl(new JdbcTemplate(dataSource));
    }

    @Test
    void findAll() {
        final List<User> users = userRepository.findAll();
        log.info("Users: {}", users);
        assertTrue(users.size() > 0);
    }

    @Test
    void findById() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findByUserName() {
        String username = "super";
        final User user = userRepository.findByUserName(username);
        assertTrue(user != null && username.equals(user.getUsername()));
    }
}
