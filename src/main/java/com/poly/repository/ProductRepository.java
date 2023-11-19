package com.poly.repository;

import com.poly.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    //        @Query(value = "SELECT product.nametv, product.id_size,product.id_resolution,product.price_export,product.id_origin,product.id_manufacture,coupon.[value],image_product.name_image FROM image_product INNER JOIN product ON image_product.id_product = product.id INNER JOIN coupon_product ON product.id = coupon_product.id_product INNER JOIN coupon ON coupon_product.id_coupon = coupon.id",nativeQuery = true)
//        List<ProductDto> findAllByDTO();
    @Query(value = "select p from Product p where p.same=?1")
    List<Product> findBySameProduct(String sameProduct);


}
