package com.poly.service.Impl;

import com.poly.common.RandomNumber;
import com.poly.dto.Attribute;
import com.poly.dto.ImageDto;
import com.poly.dto.ProductDetailDto;
import com.poly.dto.ProductDetailListDto;
import com.poly.entity.*;
import com.poly.repository.ProductDetailRepo;
import com.poly.service.*;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailImpl implements ProductDetailService {
    @Autowired
    private ProductDetailRepo productDetailRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductFieldValueService productFieldValueService;

    @Autowired
    private ProductDetailFieldService productDetailFieldService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private GroupProductService groupProductService;

    @PersistenceContext
    private EntityManager entityManager;

    public void addProductDiscount(Coupon coupon, Integer id) {
        productDetailRepo.addProductDiscount(coupon, id);
    }

    ;

    public List<ProductDetail> findBySku(String keyword) {
        return productDetailRepo.findbySku(keyword);
    }

    @Override
    public Product saveList(ProductDetailDto dto) {
        String sameProduct = "";
        if (dto.getSameProduct() == null) {
            do {
                sameProduct = "PR" + RandomNumber.generateRandomString(5);
            }
            while (!productService.findSameProduct(sameProduct).isEmpty());
        } else if (dto.getSameProduct() != null && productService.findSameProduct(dto.getSameProduct()).isEmpty()) {
            do {
                sameProduct = "PR" + RandomNumber.generateRandomString(5);
            }
            while (!productService.findSameProduct(sameProduct).isEmpty());
        } else if (dto.getSameProduct() != null && !productService.findSameProduct(dto.getSameProduct()).isEmpty()) {
            sameProduct = dto.getSameProduct();
        }
        Product product = new Product();
        product.setGroupProduct(groupProductService.findById(dto.getGroup()));
        product.setNameProduct(dto.getNameProduct());
        product.setSku(dto.getSku());
        product.setSame(sameProduct);
        if (!dto.getListProduct().isEmpty()) {
            product.setActive(true);
        } else {
            product.setActive(false);
        }
        product.setCreateDate(new Date());
        product.setAvgPoint(0);
        productService.save(product);
        for (int i = 0; i < dto.getProduct().size(); i++) {
            ProductFieldValue productFieldValue = new ProductFieldValue();
            productFieldValue.setField(fieldService.findById(dto.getProduct().get(i).getId()));
            productFieldValue.setValue(dto.getProduct().get(i).getValue());
            productFieldValue.setProduct(product);
            productFieldValueService.save(productFieldValue);
        }

        for (ProductDetailListDto productFieldValue : dto.getListProduct()) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setQuantity(productFieldValue.getQuantity());
            productDetail.setPriceExport(productFieldValue.getPriceExport());
            productDetail.setPriceImport(productFieldValue.getPriceImport());
            productDetail.setActive(productFieldValue.isActive());
            productDetail.setProduct(product);
            productDetail.setCreateDate(new Date());
            productDetail.setSku(productFieldValue.getSku());
            productDetailRepo.save(productDetail);

            for (Attribute attribute : productFieldValue.getListAttributes()) {
                ProductDetailField productDetailField = new ProductDetailField();
                productDetailField.setField(fieldService.findById(attribute.getId()));
                productDetailField.setProductDetail(productDetail);
                productDetailField.setValue(attribute.getValue());
                productDetailFieldService.save(productDetailField);
            }
            for (ImageDto imageDto : productFieldValue.getImage()) {
                Image image = new Image();
                image.setLocation(imageDto.getLocation());
                image.setProduct(productDetail);
                image.setLink(imageDto.getMultipartFile());
                imageService.add(image);
            }
        }
        return product;
    }

    @Override
    public ProductDetail findById(Integer id) {
        Optional<ProductDetail> optional = productDetailRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public Page<ProductDetail> findAll(ProductDetailListDto productDetailListDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductDetail> productCriteriaQuery = criteriaBuilder.createQuery(ProductDetail.class);
        Root<ProductDetail> productDetailRoot = productCriteriaQuery.from(ProductDetail.class);
        List<Predicate> list = new ArrayList<Predicate>();

        if (!productDetailListDto.getSku().isEmpty()) {
            list.add(criteriaBuilder.equal(productDetailRoot.get("sku"), productDetailListDto.getSku()));
        }
        if (productDetailListDto.getSort() == 1) {
            productCriteriaQuery.orderBy(criteriaBuilder.desc(productDetailRoot.get("createDate")));
        } else {
            productCriteriaQuery.orderBy(criteriaBuilder.asc(productDetailRoot.get("createDate")));
        }
        productCriteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
        Pageable pageable = PageRequest.of(productDetailListDto.getPage() - 1, productDetailListDto.getSize());

        List<ProductDetail> result = entityManager.createQuery(productCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        List<ProductDetail> result2 = entityManager.createQuery(productCriteriaQuery).getResultList();
//        if (pageable.getPageSize() == 1) {
//            pageable = PageRequest.of(0, result2.size());
//            result = entityManager.createQuery(productCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
//        }

        Page<ProductDetail> page = new PageImpl<>(result, pageable, result2.size());
        return page;
    }

    @Override
    public List<ProductDetail> findAll() {
        return productDetailRepo.findAll();
    }

    @Override
    public Boolean delete(Integer id) {
        ProductDetail productDetail = findById(id);
        if (productDetail != null) {
            productDetailRepo.delete(productDetail);
            return true;
        }
        return false;
    }

    @Override
    public ProductDetail update(ProductDetailListDto productDetailListDto) {
        ProductDetail productDetail = findById(productDetailListDto.getId());
        if (productDetail != null) {
            productDetail.setActive(productDetailListDto.isActive());
            if (productDetailListDto.getPriceExport() != null) {
                productDetail.setPriceExport(productDetailListDto.getPriceExport());
            }
            if (productDetailListDto.getPriceImport() != null) {
                productDetail.setPriceExport(productDetailListDto.getPriceImport());
            }
            if (productDetailListDto.getQuantity() != null) {
                productDetail.setQuantity(productDetailListDto.getQuantity());
            }
            if (productDetailListDto.getSku().trim().length() != 0) {
                productDetail.setSku(productDetailListDto.getSku());
            }
            if (productDetailListDto.getImage() != null) {
                for (ImageDto imageDto : productDetailListDto.getImage()) {
                    if (imageDto.getMultipartFile().trim().length() != 0) {
                        Image image = imageService.findById(imageDto.getId());
                        image.setLink(imageDto.getMultipartFile());
                        imageService.add(image);
                    }
                }
            }
            productDetailRepo.save(productDetail);
        }
        return productDetail;
    }

    public void deleteProductDiscount(Integer id) {
        productDetailRepo.deleteProductDiscount(id);
    }
}
