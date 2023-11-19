package com.poly.service;

import com.poly.dto.ProductDetailDto;
import com.poly.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ProductService {

    Product save(Product product);

    void delete(Integer id);

    Page<Product> findAll(ProductDetailDto productDetailDto);

    List<Product> findAll();

    Product findById(Integer id);

    Product update(Integer id,ProductDetailDto product);

    List<Product> findSameProduct(String same);

}
