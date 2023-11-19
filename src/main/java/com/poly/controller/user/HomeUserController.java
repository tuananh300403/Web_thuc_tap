package com.poly.controller.user;

import com.poly.common.CheckLogin;
import com.poly.dto.ProductDetailDto;
import com.poly.dto.UserDetailDto;
import com.poly.entity.*;
import com.poly.entity.Product;
import com.poly.entity.ProductDetail;
import com.poly.service.CartService;
import com.poly.service.CustomerService;
import com.poly.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeUserController {
    @Autowired
    CartService cartService;

    @Autowired
    private CheckLogin checkLogin;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String loadHome(HttpSession session, Model model) {
        session.setAttribute("pageView", "/user/page/home/home.html");
        model.addAttribute("active", "home");
        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setSort(1);
        productDetailDto.setSize(20);
        productDetailDto.setActive(true);

        productDetailDto.setGroup(1);

        Page<Product> page = productService.findAll(productDetailDto);
        List<ProductDetailDto> listProduct = new ArrayList<>();
        for (Product product : page.getContent()) {
            ProductDetailDto productDetailDto1 = new ProductDetailDto();
            productDetailDto1.setNameProduct(product.getNameProduct() + " " + product.getSku());
            //name
            int temp = 0;
            for (ProductDetail productDetail : product.getProductDetails()) {
                if (productDetail.isActive()) {
                    for (Image image : productDetail.getListImage()) {
                        if (image.isLocation()) {
                            productDetailDto1.setImage(image.getLink());
                            // ảnh
                            break;
                        }
                    }

                    if (productDetail.getCoupon() != null && productDetail.getCoupon().isActive()) {
                        BigDecimal reduceMoney = productDetail.getPriceExport().multiply(BigDecimal.valueOf(Integer.parseInt(productDetail.getCoupon().getValue()))).divide(BigDecimal.valueOf(100));
                        productDetailDto1.setReduceMoney(productDetail.getPriceExport().subtract(reduceMoney));
                        //giá sau giảm
                        productDetailDto1.setPrice(productDetail.getPriceExport());
                        // giá trước giảm
                    } else {
                        productDetailDto1.setPrice(BigDecimal.valueOf(0));
                        productDetailDto1.setReduceMoney(productDetail.getPriceExport());
                    }

                    productDetailDto1.setId(productDetail.getId());
                    productDetailDto1.setPoint(product.getAvgPoint());
                    productDetailDto1.setQuantityEvalute(product.getListEvaluate().size());
                    temp = 1;
                    break;
                }
            }
            if (temp == 1) {
                listProduct.add(productDetailDto1);
            }
        }

        model.addAttribute("listTivi", listProduct);


        productDetailDto.setGroup(2);

        Page<Product> phuKien = productService.findAll(productDetailDto);
        List<ProductDetailDto> listPhuKien = new ArrayList<>();
        for (Product product : phuKien.getContent()) {
            ProductDetailDto productDetailDto1 = new ProductDetailDto();
            productDetailDto1.setNameProduct(product.getNameProduct() + " " + product.getSku());
            //name
            int temp = 0;
            for (ProductDetail productDetail : product.getProductDetails()) {
                if (productDetail.isActive()) {
                    for (Image image : productDetail.getListImage()) {
                        if (image.isLocation()) {
                            productDetailDto1.setImage(image.getLink());
                            // ảnh
                            break;
                        }
                    }

                    if (productDetail.getCoupon() != null && productDetail.getCoupon().isActive()) {
                        BigDecimal reduceMoney = productDetail.getPriceExport().multiply(BigDecimal.valueOf(Integer.parseInt(productDetail.getCoupon().getValue()))).divide(BigDecimal.valueOf(100));
                        productDetailDto1.setReduceMoney(productDetail.getPriceExport().subtract(reduceMoney));
                        //giá sau giảm
                        productDetailDto1.setPrice(productDetail.getPriceExport());
                        // giá trước giảm
                    } else {
                        productDetailDto1.setPrice(BigDecimal.valueOf(0));
                        productDetailDto1.setReduceMoney(productDetail.getPriceExport());
                    }

                    productDetailDto1.setId(productDetail.getId());
                    productDetailDto1.setPoint(product.getAvgPoint());
                    productDetailDto1.setQuantityEvalute(product.getListEvaluate().size());
                    temp = 1;
                    break;
                }
            }
            if (temp == 1) {
                listPhuKien.add(productDetailDto1);
            }
        }
        model.addAttribute("listPhuKien", listPhuKien);

        UserDetailDto userDetailDto = checkLogin.checkLogin();
        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());
            if (cart == null) {
                cart = new Cart();
                Optional<Users> optional = customerService.findById(userDetailDto.getId());
                if (optional.isPresent()) {
                    cart.setCustomer(optional.get());
                    cartService.save(cart);
                }
            }
            session.setAttribute("list", cart.getListCartPro());
            session.setAttribute("user", true);
        } else {
            session.setAttribute("user", false);
        }
        return "/user/index";
    }
}
