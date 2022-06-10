package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.entity.query.ProductResult;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<String, Product> {

    Product findByName(String name);

    List<Product> findAllByIdsIn(List<String> productIds);

    List<ProductResult> findAllProductDetails();
}
