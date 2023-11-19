package com.poly.service.Impl;

import com.poly.entity.ProductDetailField;
import com.poly.repository.ProductDetailFieldRepo;
import com.poly.service.ProductDetailFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailFieldImpl implements ProductDetailFieldService {

    @Autowired
    private ProductDetailFieldRepo productDetailFieldRepo;

    @Override
    public ProductDetailField save(ProductDetailField productDetailField) {
        return productDetailFieldRepo.save(productDetailField);
    }
}
