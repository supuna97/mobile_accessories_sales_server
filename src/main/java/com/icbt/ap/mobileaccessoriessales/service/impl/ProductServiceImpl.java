package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Product;
import com.icbt.ap.mobileaccessoriessales.entity.query.ProductResult;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.ProductRepository;
import com.icbt.ap.mobileaccessoriessales.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void add(Product product) {
        /*checks whether the product name already exists*/
        final Product productByName = productRepository.findByName(product.getName());
        if (productByName != null) throwProductAlreadyExistException();
        productRepository.save(product);
    }

    @Override
    public void update(Product product) {
        /*validates the incoming data*/
        final Product productById = getById(product.getId());
        /*checks whether the product name already exists*/
        final Product productByName = productRepository.findByName(product.getName());
        if ((productByName != null) && (!productByName.getId().equals(product.getId())))
            throwProductAlreadyExistException();

        productById.setName(product.getName());
        if (product.getStatus() != null)
            productById.setStatus(product.getStatus());
        productRepository.update(product);
    }

    @Override
    public void delete(String id) {
        final Product product = getById(id);
        productRepository.delete(product.getId());
    }

    @Override
    public Product getById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new CustomServiceException(
                "error.validation.common.not.found.code",
                "error.validation.product.not.found.message"
        ));
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> validateAndGetProductsByIds(List<String> productIds) {
        final List<Product> productListByIds = productRepository.findAllByIdsIn(productIds);

        /*validates whether all the requested ids are available in the result*/
        validateStockReqAndResult(productIds, productListByIds);
        return productListByIds;
    }

    @Override
    public List<ProductResult> getAllProductDetails() {
        return productRepository.findAllProductDetails();
    }


    /*Internal functions below*/

    private void validateStockReqAndResult(List<String> productIdsReq, List<Product> products) {
        productIdsReq.forEach(productId -> {
            if (products.stream().noneMatch(product -> product.getId().equals(productId))) {
                throw new CustomServiceException(
                        "error.validation.common.not.found.code",
                        "error.validation.product.id.not.found.message",
                        new String[]{productId}
                );
            }
        });
    }

    private void throwProductAlreadyExistException() {
        throw new CustomServiceException(
                "error.validation.common.already.exist.code",
                "error.validation.product.name.already.exist.message"
        );

    }
}
