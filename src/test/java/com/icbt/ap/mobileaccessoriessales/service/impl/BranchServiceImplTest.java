package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.config.AppConfig;
import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.enums.BranchStatus;
import com.icbt.ap.mobileaccessoriessales.enums.BranchType;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.BranchRepository;
import com.icbt.ap.mobileaccessoriessales.repository.impl.BranchRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(AppConfig.class)
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BranchServiceImplTest {

    private BranchService branchService;

    private final BranchRepository branchRepository;

    private BranchServiceImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        branchRepository = new BranchRepositoryImpl(new JdbcTemplate(dataSource));
        branchService = new BranchServiceImpl(branchRepository);
    }

    @Test
    void add() {
        Branch branch = Branch.builder()
                .name("Matara Branch")
                .address("Matara")
                .tel("0770114545")
                .type(BranchType.BRANCH).build();
        branchService.add(branch);
        Branch branchByName = branchRepository.findByName(branch.getName());
        assertEquals(branch.getName(), branchByName.getName());
        assertEquals(branch.getAddress(), branchByName.getAddress());
        assertEquals(branch.getTel(), branchByName.getTel());
    }

    @Test
    void update() {
        String id = "323432";
        final Branch branch = branchService.getById(id);
        assertTrue(branch != null && id.equals(branch.getId()));

        branch.setName("JAffna branch");
        branch.setAddress("Jaffna");
        branch.setTel("0787878440");
        branch.setStatus(BranchStatus.INACTIVE);
        branchRepository.update(branch);

        final Branch updatedBranch = branchService.getById(id);
        assertTrue(updatedBranch != null && id.equals(updatedBranch.getId()));
        assertEquals(branch, updatedBranch);
    }

    @Test
    void delete() {
        String id = "323432";
        branchService.delete(id);
        assertThrows(CustomServiceException.class, () -> {
            final Branch branch = branchService.getById(id);
        });
    }

    @Test
    void getById() {
        String id = "323432";
        final Branch branch = branchService.getById(id);
        assertTrue(branch != null && id.equals(branch.getId()));
    }

    @Test
    void getAll() {
        final List<Branch> branches = branchService.getAll();
        assertFalse(branches.isEmpty());
    }
}
