package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.config.AppConfig;
import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.entity.Stock;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockResult;
import com.icbt.ap.mobileaccessoriessales.repository.StockRepository;
import com.icbt.ap.mobileaccessoriessales.repository.impl.BranchRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.repository.impl.ProductRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.repository.impl.StockRepositoryImpl;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
import com.icbt.ap.mobileaccessoriessales.service.ProductService;
import com.icbt.ap.mobileaccessoriessales.service.StockService;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@Import(AppConfig.class)
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StockServiceImplTest {

    private final StockService stockService;

    private final ProductService productService;
    private final BranchService branchService;

    private final StockRepository stockRepository;

    private StockServiceImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        stockRepository = new StockRepositoryImpl(new JdbcTemplate(dataSource));

        productService = new ProductServiceImpl(new ProductRepositoryImpl(new JdbcTemplate(dataSource)));
        branchService = new BranchServiceImpl(new BranchRepositoryImpl(new JdbcTemplate(dataSource)));
        stockService = new StockServiceImpl(stockRepository, productService, branchService);
    }

    @Test
    void add() {
        String branchId = "323432";
        final Branch branch = branchService.getById(branchId);
        assertTrue(branch != null && branchId.equals(branch.getId()));

        String productId = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1";
        final Product product = productService.getById(productId);
        assertTrue(product != null && productId.equals(product.getId()));

        Stock stock = Stock.builder()
                .description("my stock")
                .qty(50)
                .price(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .productId(product.getId())
                .branchId(branch.getId())
                .build();
        stockService.add(stock);
    }

    @Test
    void update() {
        String id = "643344fregt4t1";
        final Stock stock = stockService.getById(id);
        stock.setPrice(BigDecimal.TEN);
        stock.setQty(30);
        stock.setDescription("Desc");
        stock.setBranchId("323432");
        stock.setProductId("12cbc2ca-69d8-11eb-8f8a-a81e849e9ba2");
        stockService.update(stock);

        final Optional<Stock> optionalStock = stockRepository.findById(id);
        assertTrue(optionalStock.isPresent());
        assertEquals(optionalStock.get().getId(), stock.getId());
    }

    @Test
    void delete() {
    }

    @Test
    void getById() {
    }

    @Test
    void getAll() {
        final List<Stock> stocks = stockService.getAll();
        assertFalse(stocks.isEmpty());
    }

    @Test
    void getAllByBranch() {
        final List<StockResult> stockResults = stockService.getAllByBranch("43242324");
        assertFalse(stockResults.isEmpty());
    }

    @Test
    void getAllByProduct() {
    }
}