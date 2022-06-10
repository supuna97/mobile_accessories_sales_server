package com.icbt.ap.mobileaccessoriessales.repository.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.enums.ProductStatus;
import com.icbt.ap.mobileaccessoriessales.repository.ProductRepository;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductRepositoryImplTest {

    private final ProductRepository productRepository;

    private ProductRepositoryImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        productRepository = new ProductRepositoryImpl(new JdbcTemplate(dataSource));
    }

    @Test
    void findAll() {
        final List<Product> products = productRepository.findAll();
        log.info("Products: {}", products);
    }

    @Test
    void findById() {
        String id = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1";
        final Optional<Product> optionalProduct = productRepository.findById(id);
        assertTrue(optionalProduct.isPresent() && id.equals(optionalProduct.get().getId()));
    }

    @Test
    void save() {
        Product product = Product.builder().name("Pineapple").status(ProductStatus.ACTIVE).build();
        productRepository.save(product);
        product = productRepository.findByName(product.getName());
        assertEquals("Pineapple", product.getName());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void setDataSource() {
    }

    @Test
    void findByName() {
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }
}