package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.controller.CommonController;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.ProductResultResponse;
import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import com.icbt.ap.mobileaccessoriessales.dto.ContentResponseDTO;
import com.icbt.ap.mobileaccessoriessales.entity.query.ProductResult;
import com.icbt.ap.mobileaccessoriessales.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = ApiConstant.VERSION + "/product_list")
@Slf4j
@RequiredArgsConstructor
public class ProductResultController implements CommonController {

    private final ProductService productService;

    private final MessageSource messageSource;

    @GetMapping(path = "")
    public ResponseEntity<ContentResponseDTO<List<ProductResultResponse>>> getProductResults() {
        log.info("Get all products details");
        return getAllProductDetails();
    }

    /*Internal functions*/

    private ResponseEntity<ContentResponseDTO<List<ProductResultResponse>>> getAllProductDetails() {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getProductResultResponseList(productService.getAllProductDetails())));
    }

    private List<ProductResultResponse> getProductResultResponseList(List<ProductResult> productResults) {
        return productResults
                .stream()
                .map(this::getProductResultResponse)
                .collect(Collectors.toList());
    }

    private ProductResultResponse getProductResultResponse(ProductResult productResult) {
        return ProductResultResponse.builder()
                .id(productResult.getId())
                .name(productResult.getName())
                .status(productResult.getStatus())
                .qty(productResult.getQty())
                .price(productResult.getPrice())
                .description(productResult.getDescription())
                .build();
    }

    private String getFormattedDateTime(LocalDateTime dateTime) {
        return ApiConstant.DATE_TIME_FORMATTER.format(dateTime);
    }

    private String getCode(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }
}
