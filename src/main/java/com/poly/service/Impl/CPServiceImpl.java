//package com.poly.service.Impl;
//
//import com.poly.dto.CartDto;
//import com.poly.entity.CartProduct;
//import com.poly.entity.idClass.CartProductId;
//import com.poly.repository.CartProductRepos;
//import com.poly.service.CartProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CPServiceImpl implements CartProductService {
//    @Autowired
//    CartProductRepos repos;
//
//    @Override
//    public List<CartProduct> getAll() {
//        return repos.findAll();
//    }
//
//    @Override
//    public CartProduct save(CartDto cp) {
//        return null;
//    }
//
//    @Override
//    public void delete(CartProductId id) {
//        repos.deleteById(id);
//    }
//
//    @Override
//    public CartProduct edit(CartProductId id) {
//        return repos.findById(id).get();
//    }
//
//    @Override
//    public Optional<CartProduct> getOne(CartProductId id) {
//        return repos.findById(id);
//    }
//}
