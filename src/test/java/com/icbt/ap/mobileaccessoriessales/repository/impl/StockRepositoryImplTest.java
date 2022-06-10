package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.entity.Stock;
import com.icbt.ap.mobileaccessoriessales.entity.query.StockResult;
import com.icbt.ap.mobileaccessoriessales.repository.BranchRepository;
import com.icbt.ap.mobileaccessoriessales.repository.ProductRepository;
import com.icbt.ap.mobileaccessoriessales.repository.StockRepository;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StockRepositoryImplTest {

    private final StockRepository stockRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public StockRepositoryImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        stockRepository = new StockRepositoryImpl(new JdbcTemplate(dataSource));
        branchRepository = new BranchRepositoryImpl(new JdbcTemplate(dataSource));
        productRepository = new ProductRepositoryImpl(new JdbcTemplate(dataSource));
    }

    @Test
    void findAll() {
        final List<Stock> stocks = stockRepository.findAll();
        log.info("Stocks: {}", stocks);
        assertTrue(stocks.size() > 0);
    }

    @Test
    void findById() {
        String id = "643344fregt4t";
        final Optional<Stock> optionalStock = stockRepository.findById(id);
        assertTrue(optionalStock.isPresent() && id.equals(optionalStock.get().getId()));
    }

    @Test
    void save() {
        String branchId = "323432";
        final Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        assertTrue(optionalBranch.isPresent() && branchId.equals(optionalBranch.get().getId()));

        String productId = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1";
        final Optional<Product> optionalProduct = productRepository.findById(productId);
        assertTrue(optionalProduct.isPresent() && productId.equals(optionalProduct.get().getId()));

        Stock stock = Stock.builder()
                .description("my stock")
                .qty(50)
                .price(BigDecimal.TEN.multiply(BigDecimal.TEN))
                .productId(optionalProduct.get().getId())
                .branchId(optionalBranch.get().getId())
                .build();

        stockRepository.save(stock);
    }

    @Test
    void update() {
        String id = "643344fregt4t";

        String branchId = "323432";
        final Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        assertTrue(optionalBranch.isPresent() && branchId.equals(optionalBranch.get().getId()));

        String productId = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1";
        final Optional<Product> optionalProduct = productRepository.findById(productId);
        assertTrue(optionalProduct.isPresent() && productId.equals(optionalProduct.get().getId()));


        final Optional<Stock> optionalStock = stockRepository.findById(id);
        assertTrue(optionalStock.isPresent() && id.equals(optionalStock.get().getId()));
        final Stock stock = optionalStock.get();
        stock.setDescription("test desc");
        stock.setQty(12);
        stock.setPrice(new BigDecimal("160.00"));
        stock.setProductId(optionalProduct.get().getId());
        stock.setBranchId(optionalBranch.get().getId());
        stockRepository.update(stock);

        final Optional<Stock> optionalUpdatedStock = stockRepository.findById(id);
        assertTrue(optionalUpdatedStock.isPresent() && id.equals(optionalUpdatedStock.get().getId()));
        final Stock updatedStock = optionalUpdatedStock.get();
        assertEquals(stock, updatedStock);
    }

    @Test
    void delete() {

    }

    @Test
    void findAllByBranch() {

        String branchId = "323432";
        final Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        assertTrue(optionalBranch.isPresent() && branchId.equals(optionalBranch.get().getId()));

        final List<StockResult> stocks = stockRepository.findAllByBranch(optionalBranch.get().getId());
        assertTrue(stocks.stream().allMatch(stock -> stock.getBranchId().equals(branchId)));
    }

    @Test
    void findAllByProduct() {

        String productId = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1";
        final Optional<Product> optionalProduct = productRepository.findById(productId);
        assertTrue(optionalProduct.isPresent() && productId.equals(optionalProduct.get().getId()));

        final List<StockResult> stocks = stockRepository.findAllByProduct(optionalProduct.get().getId());
        assertTrue(stocks.stream().allMatch(stock -> stock.getProductId().equals(productId)));
    }
}