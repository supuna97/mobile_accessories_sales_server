package com.icbt.ap.mobileaccessoriessales.service;

import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.entity.query.ProductResult;
import com.icbt.ap.mobileaccessoriessales.service.main.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService extends CrudService<String, Product> {

    List<Product> validateAndGetProductsByIds(List<String> productIds);

    List<ProductResult> getAllProductDetails();
}
