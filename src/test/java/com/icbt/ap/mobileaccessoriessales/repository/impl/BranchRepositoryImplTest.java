package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.enums.BranchStatus;
import com.icbt.ap.mobileaccessoriessales.enums.BranchType;
import com.icbt.ap.mobileaccessoriessales.repository.BranchRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BranchRepositoryImplTest {

    private final BranchRepository branchRepository;

    public BranchRepositoryImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        branchRepository = new BranchRepositoryImpl(new JdbcTemplate(dataSource));
    }

    @Test
    void findAll() {
        final List<Branch> branches = branchRepository.findAll();
        log.info("Branches: {}", branches);
        assertTrue(branches.size() > 0);
    }

    @Test
    void findById() {
        String id = "323432";
        final Optional<Branch> optionalBranch = branchRepository.findById(id);
        assertTrue(optionalBranch.isPresent() && id.equals(optionalBranch.get().getId()));
    }

    @Test
    void save() {
        Branch branch = Branch.builder()
                .name("Matara Branch")
                .address("Matara")
                .tel("0770114545")
                .type(BranchType.BRANCH).build();
        branchRepository.save(branch);
        Branch branchByName = branchRepository.findByName(branch.getName());
        assertEquals(branch.getName(), branchByName.getName());
        assertEquals(branch.getAddress(), branchByName.getAddress());
        assertEquals(branch.getTel(), branchByName.getTel());
    }

    @Test
    void update() {
        String id = "323432";
        final Optional<Branch> optionalBranch = branchRepository.findById(id);
        assertTrue(optionalBranch.isPresent() && id.equals(optionalBranch.get().getId()));
        final Branch branch = optionalBranch.get();
        branch.setName("JAffna branch");
        branch.setAddress("Jaffna");
        branch.setTel("0787878440");
        branch.setStatus(BranchStatus.INACTIVE);
        branchRepository.update(branch);

        final Optional<Branch> optionalUpdatedBranch = branchRepository.findById(id);
        assertTrue(optionalUpdatedBranch.isPresent() && id.equals(optionalUpdatedBranch.get().getId()));
        final Branch updatedBranch = optionalUpdatedBranch.get();
        assertEquals(branch, updatedBranch);
    }

    @Test
    void delete() {
        String id = "323432";
        final Optional<Branch> optionalBranch = branchRepository.findById(id);
        assertTrue(optionalBranch.isPresent() && id.equals(optionalBranch.get().getId()));
        final Branch branch = optionalBranch.get();
        branchRepository.delete(branch.getId());

        final Optional<Branch> optionalDeletedBranch = branchRepository.findById(id);
        assertFalse(optionalDeletedBranch.isPresent() && id.equals(optionalDeletedBranch.get().getId()));
    }

    @Test
    void findByName() {
        String name = "Colombo Branch";
        final Branch branch = branchRepository.findByName(name);
        assertTrue(branch != null && name.equals(branch.getName()));
    }

    @Test
    void findByTel() {
        String tel = "0774935895";
        final Branch branch = branchRepository.findByTel(tel);
        assertTrue(branch != null && tel.equals(branch.getTel()));
    }
}
