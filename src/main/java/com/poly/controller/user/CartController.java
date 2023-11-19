package com.poly.controller.user;

import com.poly.common.CheckLogin;
import com.poly.dto.BillProRes;
import com.poly.dto.UserDetailDto;
import com.poly.entity.*;
import com.poly.entity.idClass.CartProductId;
import com.poly.repository.BillStatusRepos;
import com.poly.repository.CartRepos;
import com.poly.service.*;
import com.poly.service.Impl.DeliveryNotesImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/user")
public class CartController {

    @Autowired
    HttpSession session;

    @Autowired
    CartService cartService;

    @Autowired
    CustomerService customerService;

    @Autowired
    BillService billService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private VoucherCustomerService voucherCustomerService;

    @Autowired
    private CheckLogin checkLogin;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private DeliveryNotesImpl deliveryNotes;

    @Autowired
    private BillStatusRepos billStatusRepos;


    @GetMapping("/pay")
    public String pay(Model model) {
        session.setAttribute("pageView", "/user/page/product/pay.html");
        model.addAttribute("billProduct", new BillProRes());
        List<BigDecimal> listRedu = new ArrayList<>();
        BigDecimal reduceMoney = BigDecimal.valueOf(0);

        UserDetailDto userDetailDto = checkLogin.checkLogin();
        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());
            BigDecimal total = new BigDecimal(0);
            if (cart.getListCartPro().isEmpty()) {
                session.setAttribute("pageView", "/user/page/product/cart_null.html");
                session.setAttribute("list", null);
                return "user/index";
            }
            for (CartProduct product : cart.getListCartPro()) {
                if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive()) {
                    reduceMoney = product.getProduct().getPriceExport().subtract(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100))));
                    total = total.add(reduceMoney.multiply(BigDecimal.valueOf(product.getQuantity())));
                    listRedu.add(reduceMoney);
                } else {
                    reduceMoney = product.getProduct().getPriceExport();
                    total = total.add(product.getProduct().getPriceExport().multiply(BigDecimal.valueOf(product.getQuantity())));
                    listRedu.add(reduceMoney);
                }
            }
            List<VoucherCustomer> voucherCustomer = voucherCustomerService.findByUser(userDetailDto.getId());
            model.addAttribute("listVoucher", voucherCustomer);
            model.addAttribute("reduceMoney", listRedu);
            model.addAttribute("total", total);

            session.setAttribute("list", cart.getListCartPro());
            return "user/index";
        }
        List<CartProduct> listCart = (List<CartProduct>) session.getAttribute("list");
        if (cartService.getTotal() == 0) {
            session.setAttribute("pageView", "/user/page/product/cart_null.html");
            return "user/index";
        }
        for (CartProduct product : listCart) {
            if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive()) {
                reduceMoney = product.getProduct().getPriceExport().subtract(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100))));
                listRedu.add(reduceMoney);
            } else {
                reduceMoney = product.getProduct().getPriceExport();
                listRedu.add(reduceMoney);
            }
        }
        model.addAttribute("reduceMoney", listRedu);
        BigDecimal total = cartService.getAmount();
        model.addAttribute("total", total);
        return "user/index";
    }

    @GetMapping("/confirm")
    public String con(Model model) {
        session.setAttribute("pageView", "/user/page/product/confirm.html");
        return "user/index";
    }


    @PostMapping("/purchase")
    public String addBill(HttpServletRequest request,
                          Model model,
                          Integer id,
                          @ModelAttribute(value = "billProduct") BillProRes billProRes) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Users checkEmail = customerService.findByEmail(billProRes.getEmail());
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        List<CartProduct> listCart = new ArrayList<>();
        BigDecimal reduceMoney = BigDecimal.valueOf(0);
        listCart = (List<CartProduct>) session.getAttribute("list");

        List<BigDecimal> listRedu = new ArrayList<>();
        List<Integer> listPro = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        for (CartProduct product : listCart) {
            if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive()) {
                reduceMoney = product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100)));
                listRedu.add(reduceMoney);
                total = total.add(product.getProduct().getPriceExport().subtract(reduceMoney).multiply(BigDecimal.valueOf(product.getQuantity())));
            } else {
                total = total.add(product.getProduct().getPriceExport().multiply(BigDecimal.valueOf(product.getQuantity())));
                listRedu.add(BigDecimal.valueOf(0));
            }
            quantity.add(product.getQuantity());
            listPro.add(product.getProduct().getId());
        }
        billProRes.setTotalPrice(total);// lấy tổng tiền

        if (userDetailDto == null) {
            if (checkEmail == null) {
                checkEmail = customerService.add(billProRes);
                billProRes.setCustomer(checkEmail);
            } else if (checkEmail != null && checkEmail.getRoles() == null) {
                billProRes.setCustomer(checkEmail);
            } else if (checkEmail != null && checkEmail.getRoles().equals("USER")) {

                return "";// thông báo email đã dùng đăng ký tài khoản
            }
            total = cartService.getAmount();
            billProRes.setTotalPrice(total);// lấy tổng tiền

        } else {
            if (userDetailDto.getEmail().trim().equals(billProRes.getEmail().trim())) {
//                if (checkEmail != null && checkEmail.getRoles() != null) {
//                    return "user/index";
//                }
                // thông báo lỗi email của người khác k thể dùng để mua hàng
                billProRes.setCustomer(checkEmail);
            } else {
                if (checkEmail != null && checkEmail.getRoles() != null) {
                    return "user/index";
                }
                billProRes.setCustomer(checkEmail);
            }
        }

        Bill bill1 = billService.add(billProRes);// tạo hóa đơn mới
        billProRes.setBill(bill1);

        DeliveryNotes notes = deliveryNotes.save(billProRes);// tạo phiếu giao hàng
        billProRes.setQuantity(quantity);
        billProRes.setReducedMoney(listRedu);// lấy danh sách giảm giá
        billProRes.setProduct(listPro);// lấy danh sách sản phẩm

        if (billProRes.getPaymentMethod() == 2) {
            //VNPAY
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(bill1.getTotalPrice(), bill1.getCode(), baseUrl);
//            billProRes.setProduct(Collections.singletonList(id));
            billService.addBillPro(bill1, billProRes);

            if (userDetailDto != null) {
                Cart cart = cartService.getOneByUser(userDetailDto.getId());
                for (CartProduct cartProduct : cart.getListCartPro()) {
                    CartProductId cartProductId = new CartProductId();
                    cartProductId.setProduct(cartProduct.getProduct());
                    cartProductId.setCart(cart);
                    boolean check = cartProductService.delete(cartProductId);
                }
                session.setAttribute("list", null);
            }
            cartService.clear();
            return "redirect:" + vnpayUrl;
        } else {
            billService.addBillPro(bill1, billProRes);
            if (userDetailDto != null) {
                Cart cart = cartService.getOneByUser(userDetailDto.getId());
                for (CartProduct cartProduct : cart.getListCartPro()) {
                    CartProductId cartProductId = new CartProductId();
                    cartProductId.setProduct(cartProduct.getProduct());
                    cartProductId.setCart(cart);
                    boolean check = cartProductService.delete(cartProductId);
                }
                session.setAttribute("list", null);
            }

            cartService.clear();
            return "redirect:/confirm";
        }
    }

    @GetMapping("/cart")
    public String index(Model model) {
        session.setAttribute("pageView", "/user/page/product/pro_cart.html");
        List<BigDecimal> listRedu = new ArrayList<>();
        BigDecimal reduceMoney = new BigDecimal(0);
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());
            if (cart.getListCartPro().size() == 0) {
                session.setAttribute("pageView", "/user/page/product/cart_null.html");
                session.setAttribute("list", null);
                return "user/index";
            }
            BigDecimal total = new BigDecimal(0);
            for (CartProduct product : cart.getListCartPro()) {
                if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive()) {
                    reduceMoney = product.getProduct().getPriceExport().subtract(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100))));
                    total = total.add(reduceMoney.multiply(BigDecimal.valueOf(product.getQuantity())));
                    listRedu.add(reduceMoney);
                } else {
                    reduceMoney = product.getProduct().getPriceExport();
                    total = total.add(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getQuantity())));
                    listRedu.add(reduceMoney);
                }
            }
            model.addAttribute("reduceMoney", listRedu);
            model.addAttribute("total", total);
            session.setAttribute("list", cart.getListCartPro());
            return "user/index";
        }
        List<CartProduct> listCart = (List<CartProduct>) session.getAttribute("list");

        if (cartService.getTotal() == 0) {
            session.setAttribute("pageView", "/user/page/product/cart_null.html");
            return "user/index";
        }
        for (CartProduct product : listCart) {
            if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive()) {
                reduceMoney = product.getProduct().getPriceExport().subtract(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100))));
                listRedu.add(reduceMoney);
            } else {
                reduceMoney = product.getProduct().getPriceExport();
                listRedu.add(reduceMoney);
            }
        }
        model.addAttribute("reduceMoney", listRedu);
        BigDecimal total = cartService.getAmount();
        model.addAttribute("total", total);

        return "user/index";
    }

    @RequestMapping("/cart/remove/{id}")
    public String delete(@PathVariable List<Integer> id, RedirectAttributes redirectAttributes) {
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());
            session.setAttribute("list", cart.getListCartPro());
            ProductDetail productDetail = productDetailService.findById(id.get(0));
            CartProductId cartProductId = new CartProductId();
            if (productDetail != null) {
                cartProductId.setProduct(productDetail);
                cartProductId.setCart(cart);
            }
            boolean check = cartProductService.delete(cartProductId);
        } else {
            List<CartProduct> list = new ArrayList<>();
            for (int i = 0; i < id.size(); i++) {
                list = cartService.delete(id.get(i));
            }
            session.setAttribute("list", list);
        }
        redirectAttributes.addFlashAttribute("message", "xoathanhcong");
        return "redirect:/cart";
    }


    @PostMapping("/cart/update")
    public String update(@RequestParam(value = "id", required = false) List<Integer> id, @RequestParam("qty") List<Integer> qty, Model model, RedirectAttributes redirectAttributes) {

        UserDetailDto userDetailDto = checkLogin.checkLogin();

        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());

            List<CartProduct> list = new ArrayList<>();

            for (int i = 0; i < id.size(); i++) {
                ProductDetail productDetail = productDetailService.findById(id.get(i));
                CartProductId cartProductId = new CartProductId();
                cartProductId.setCart(cart);
                cartProductId.setProduct(productDetail);
                Optional<CartProduct> optional = cartProductService.getOne(cartProductId);
                if (optional.isPresent()) {
                    CartProduct cartProduct = optional.get();
                    cartProduct.setQuantity(qty.get(i));
                    list.add(cartProductService.update(cartProduct));
                }
            }
            session.setAttribute("list", list);
        } else {
            List<CartProduct> list = new ArrayList<>();
            for (int i = 0; i < id.size(); i++) {
                list = cartService.update(id.get(i), qty.get(i));
            }
            session.setAttribute("list", list);
        }
        redirectAttributes.addFlashAttribute("message", "update-success");
        return "redirect:/cart";
    }
}
