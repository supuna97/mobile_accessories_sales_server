package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.StockRequest;
import com.icbt.ap.mobileaccessoriessales.repository.StockRepository;
import com.icbt.ap.mobileaccessoriessales.repository.StockRequestRepository;
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
class StockRequestRepositoryImplTest {

    private final StockRequestRepository stockRequestRepository;
    private final StockRepository stockRepository;

    public StockRequestRepositoryImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        stockRepository = new StockRepositoryImpl(new JdbcTemplate(dataSource));
        stockRequestRepository = new StockRequestRepositoryImpl(new JdbcTemplate(dataSource));
    }

    @Test
    void findAll() {
        final List<StockRequest> stockRequests = stockRequestRepository.findAll();
        log.info("Stocks Requests: {}", stockRequests);
        assertTrue(stockRequests.size() > 0);
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
    void updateStatus() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAllByRequestByBranch() {
    }

    @Test
    void findAllByRequestToBranch() {
    }
}