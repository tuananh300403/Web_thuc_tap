package com.poly.service.Impl;

import com.poly.dto.Attribute;
import com.poly.dto.ProductDetailDto;
import com.poly.entity.Product;
import com.poly.entity.ProductDetail;
import com.poly.entity.ProductFieldValue;
import com.poly.repository.CouponRepository;
import com.poly.repository.ProductDetailRepo;
import com.poly.repository.ProductRepository;
import com.poly.service.ProductFieldValueService;
import com.poly.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    private ProductDetailRepo productDetailRepo;

    @Autowired
    private ProductFieldValueService productFieldValueService;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Integer id) {
        productRepository.delete(productRepository.findById(id).get());
    }

    @Override
    public Page<Product> findAll(ProductDetailDto productDetailDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> productCriteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = productCriteriaQuery.from(Product.class);
        List<Predicate> list = new ArrayList<Predicate>();
        if (productDetailDto.getSku() != "") {
            list.add(criteriaBuilder.equal(productRoot.get("sku"), productDetailDto.getSku()));
        }
        if (productDetailDto.getGroup() != 0) {
            list.add(criteriaBuilder.equal(productRoot.get("groupProduct").get("id"), productDetailDto.getGroup()));
        }
        if (productDetailDto.isActive() == true) {
            list.add(criteriaBuilder.equal(productRoot.get("active"), true));
        }
        if (productDetailDto.getSort() == 1) {
            productCriteriaQuery.orderBy(criteriaBuilder.desc(productRoot.get("createDate")));
        } else {
            productCriteriaQuery.orderBy(criteriaBuilder.asc(productRoot.get("createDate")));
        }
        productCriteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
        Pageable pageable = PageRequest.of(productDetailDto.getPage() - 1, productDetailDto.getSize());
        List<Product> result = entityManager.createQuery(productCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        List<Product> result1 = entityManager.createQuery(productCriteriaQuery).getResultList();
        Page<Product> page = new PageImpl<>(result, pageable, result1.size());
        return page;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).get();
    }

    @Override
    public Product update(Integer id, ProductDetailDto product) {
        Product product1 = findById(id);
        if (findById(id) != null) {
            if (product.isActive() == false) {
                product1.setActive(product.isActive());
                for (ProductDetail productDetail : product1.getProductDetails()) {
                    productDetail.setActive(false);
                    productDetailRepo.save(productDetail);
                }
            } else {
                product1.setActive(product.isActive());
            }
            if (product.getProduct()!= null) {
                List<ProductFieldValue> productFieldValue = productFieldValueService.findByProduct(product1);
                if (!productFieldValue.isEmpty()) {
                    for (int i = 0; i < productFieldValue.size(); i++) {
                        for (Attribute attribute : product.getProduct()) {
                            if (attribute.getId() == productFieldValue.get(i).getField().getId()) {
                                productFieldValue.get(i).setValue(attribute.getValue());
                                productFieldValueService.save(productFieldValue.get(i));
                            }
                        }
                    }
                }
            }
            productRepository.save(product1);
        }
        return null;
    }

    @Override
    public List<Product> findSameProduct(String same) {
        return productRepository.findBySameProduct(same);
    }


}
