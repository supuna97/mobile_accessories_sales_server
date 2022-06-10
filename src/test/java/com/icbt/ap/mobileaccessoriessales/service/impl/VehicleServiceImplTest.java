package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.config.AppConfig;
import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.entity.Vehicle;
import com.icbt.ap.mobileaccessoriessales.repository.BranchRepository;
import com.icbt.ap.mobileaccessoriessales.repository.VehicleRepository;
import com.icbt.ap.mobileaccessoriessales.repository.impl.BranchRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.repository.impl.VehicleRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
import com.icbt.ap.mobileaccessoriessales.service.VehicleService;
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

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@Import(AppConfig.class)
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class VehicleServiceImplTest {

    private VehicleService vehicleService;
    private BranchService branchService;

    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;

    private VehicleServiceImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        vehicleRepository = new VehicleRepositoryImpl(new JdbcTemplate(dataSource));
        branchRepository = new BranchRepositoryImpl(new JdbcTemplate(dataSource));
        branchService = new BranchServiceImpl(branchRepository);
        vehicleService = new VehicleServiceImpl(vehicleRepository, branchService);
    }

    @Test
    void add() {
        String branchId = "323432";
        final Branch branch = branchService.getById(branchId);
        assertTrue(branch != null && branchId.equals(branch.getId()));

        Vehicle vehicle = Vehicle.builder()
                .regNo("JH-1209")
                .driverId("D0101")
                .branchId(branchId)
                .build();
        vehicleService.add(vehicle);
        Vehicle vehicleByNo = vehicleRepository.findByRegNo(vehicle.getRegNo());
        assertEquals(vehicle.getRegNo(), vehicleByNo.getRegNo());
        assertEquals(vehicle.getDriverId(), vehicleByNo.getDriverId());
        assertEquals(vehicle.getBranchId(), vehicleByNo.getBranchId());
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
}