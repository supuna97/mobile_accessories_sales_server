package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.config.AppConfig;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.UserLoginRequest;
import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.repository.BranchRepository;
import com.icbt.ap.mobileaccessoriessales.repository.UserRepository;
import com.icbt.ap.mobileaccessoriessales.repository.impl.BranchRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.repository.impl.UserRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
import com.icbt.ap.mobileaccessoriessales.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@Import(AppConfig.class)
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {

    private final UserService userService;

    private final UserRepository userRepository;
    private final BranchService branchService;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    private UserServiceImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        branchRepository = new BranchRepositoryImpl(new JdbcTemplate(dataSource));
        branchService = new BranchServiceImpl(branchRepository);
        userRepository = new UserRepositoryImpl(new JdbcTemplate(dataSource));
        passwordEncoder = new AppConfig().passwordEncoder();
        userService = new UserServiceImpl(userRepository, branchService, passwordEncoder);
    }


    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getById() {
    }

    @Test
    void getAll() {
    }

    @Test
    void authenticate() {
        final UserLoginRequest loginRequest = UserLoginRequest.builder()
                .username("super")
                .password("123")
                .build();
        final User user = userService.authenticate(loginRequest);
        log.info("Logged user: {}", user);
        assertEquals(loginRequest.getUsername(), user.getUsername());
    }
}
