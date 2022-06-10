package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.enums.ProductStatus;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.ProductRepository;
import com.icbt.ap.mobileaccessoriessales.repository.impl.ProductRepositoryImpl;
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

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceImplTest {

    private ProductServiceImpl productService;

    private final ProductRepository productRepository;

    private ProductServiceImplTest() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test/sales_db_test.sql")
                .build();
        productRepository = new ProductRepositoryImpl(new JdbcTemplate(dataSource));
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void add() {
        String name = "Sugar";
        final Product product = Product.builder().name(name).status(ProductStatus.ACTIVE).build();
        productService.add(product);
        final Product productByName = productRepository.findByName(name);
        assertEquals(product.getName(), productByName.getName());
        assertEquals(product.getStatus(), productByName.getStatus());
    }

    @Test
    void update() {
        String id = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba1";
        final Optional<Product> optionalProduct = productRepository.findById(id);
        assertTrue(optionalProduct.isPresent());
        final Product product = optionalProduct.get();
        product.setName("Tea");
        product.setStatus(ProductStatus.INACTIVE);
        productService.update(product);

        final Optional<Product> updatedOptionalProduct = productRepository.findById(id);
        assertTrue(updatedOptionalProduct.isPresent());
        assertEquals(product, updatedOptionalProduct.get());
    }

    @Test
    void delete() {
        String id = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba";
        productService.delete(id);
        assertThrows(CustomServiceException.class, () -> {
            final Product product = productService.getById(id);
        });
    }

    @Test
    void getById() {
        String id = "12cbc2ca-69d8-11eb-8f8a-a81e849e9ba";
        final Product product = productService.getById(id);
        assertEquals(id, product.getId());
    }

    @Test
    void getAll() {
        final List<Product> products = productService.getAll();
        assertFalse(products.isEmpty());
    }
}