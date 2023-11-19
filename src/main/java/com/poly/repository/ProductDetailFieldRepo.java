package com.poly.repository;


import com.poly.entity.ProductDetailField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailFieldRepo extends JpaRepository<ProductDetailField, Integer> {
}
