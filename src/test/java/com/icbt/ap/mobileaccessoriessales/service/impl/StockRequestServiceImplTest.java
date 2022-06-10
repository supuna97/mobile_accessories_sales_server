package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.config.AppConfig;
import com.icbt.ap.mobileaccessoriessales.entity.*;
import com.icbt.ap.mobileaccessoriessales.enums.StockRequestStatus;
import com.icbt.ap.mobileaccessoriessales.repository.StockRepository;
import com.icbt.ap.mobileaccessoriessales.repository.StockRequestDetailRepository;
import com.icbt.ap.mobileaccessoriessales.repository.StockRequestRepository;
import com.icbt.ap.mobileaccessoriessales.repository.VehicleRepository;
import com.icbt.ap.mobileaccessoriessales.repository.impl.*;
import com.icbt.ap.mobileaccessoriessales.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import java.util.Arrays;

@Import(AppConfig.class)
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StockRequestServiceImplTest {

    private final StockRequestService stockRequestService;
    private final StockRequestRepository stockRequestRepository;
    private final StockRequestDetailRepository stockRequestDetailRepository;
    private final VehicleRepository vehicleRepository;
    private final StockRepository stockRepository;

    private final StockService stockService;
    private final ProductService productService;
    private final BranchService branchService;
    private VehicleService vehicleService;


    private StockRequestServiceImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        stockRepository = new StockRepositoryImpl(jdbcTemplate);
        final BranchRepositoryImpl branchRepository = new BranchRepositoryImpl(jdbcTemplate);

        stockRequestRepository = new StockRequestRepositoryImpl(jdbcTemplate);
        stockRequestDetailRepository = new StockRequestDetailRepositoryImpl(jdbcTemplate);
        vehicleRepository = new VehicleRepositoryImpl(jdbcTemplate);

        branchService = new BranchServiceImpl(branchRepository);
        vehicleService = new VehicleServiceImpl(vehicleRepository, branchService);
        productService = new ProductServiceImpl(new ProductRepositoryImpl(jdbcTemplate));
        stockService = new StockServiceImpl(stockRepository, productService, branchService);
        stockRequestService = new StockRequestServiceImpl(stockRequestRepository, stockRequestDetailRepository,
                vehicleRepository, branchRepository, stockRepository, branchService, vehicleService, stockService,
                productService);
    }

    @Test
    void add() {
        String byBranchId = "323432";
        String forBranchId = "43242324";
        String vehicleId = "43432";
        String productId = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1";

        StockRequestDetail stockRequestDetail = StockRequestDetail.builder()
                .productId(productId)
                .qty(10)
                .build();
        StockRequestDetail stockRequestDetail2 = StockRequestDetail.builder()
                .productId(productId)
                .qty(20)
                .build();

        StockRequest stockRequest = StockRequest.builder()
                .byBranchId(byBranchId)
                .forBranchId(forBranchId)
                .vehicleId(vehicleId)
                .stockRequestDetails(Arrays.asList(stockRequestDetail, stockRequestDetail2))
                .build();
        stockRequestService.add(stockRequest);
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
    void getAllByRequestedByBranch() {
    }

    @Test
    void getAllByRequestedForBranch() {
    }

    @Test
    void updateStatus() {
        final String stockReqId = "fer324324";
        final StockRequestStatus status = StockRequestStatus.REJECTED;
        stockRequestService.updateStatus(stockReqId, status);
        final StockRequest stockRequest = stockRequestService.getById(stockReqId);
        assertEquals(status, stockRequest.getStatus());
    }
}
