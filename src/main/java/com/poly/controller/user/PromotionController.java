package com.poly.controller.user;

import com.poly.dto.UserDetailDto;
import com.poly.dto.VoucherCustomerRes;
import com.poly.entity.Voucher;
import com.poly.entity.VoucherCustomer;
import com.poly.service.Impl.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    VoucherServiceImpl voucherService;
    @Autowired
    VoucherCustomerServiceImpl voucherCustomerService;
    @Autowired
    CouponServiceImpl couponService;
    @Autowired
    ProductServiceImpl productService;
    LocalDate localDate = LocalDate.now();
    Date date = (Date) Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    @GetMapping()
    public String index(HttpSession session, Model model, @RequestParam(defaultValue = "0") int p) {

        model.addAttribute("listcoupon", couponService.getAllByActive());
        model.addAttribute("listvoucher", voucherService.findAllByDate(date));
        session.setAttribute("pageView", "/user/page/promotion/promotions.html");
        return "/user/index";
    }

    @GetMapping("/coupondetail/{id}")
    public String coupondetail(HttpSession session, Model model, @PathVariable("id") Integer id) {
        Integer giam = Integer.parseInt(couponService.findById(id).get().getValue());
        model.addAttribute("giam",giam);
        model.addAttribute("discount",couponService.findById(id).get());
        model.addAttribute("listproduct", couponService.findById(id).get().getProductDetailList());
        couponService.findById(id).get().getProductDetailList().get(0).getPriceExport().intValue();
        session.setAttribute("pageView", "/user/page/promotion/coupondetail.html");
        return "/user/index";
    }

    @GetMapping("/voucherdetail/{id}")
    public String voucherdetail(HttpSession session, Model model, @PathVariable("id") Integer id) {
        Voucher voucher = new Voucher();
        for (Voucher x : voucherService.findAllList()) {
            if (x.getId() == id) {
                voucher = x;
            }
        }
        model.addAttribute("voucher", voucher);
        LocalDate today = LocalDate.now();
        int soluong = voucherCustomerService.findAllByVoucher(id).size();
        boolean check = false;
        boolean check2 = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDetailDto customerUserDetail = (UserDetailDto) userDetails;
            for (VoucherCustomer x : voucherCustomerService.findAllByVoucher(id)) {
                if (x.getCustomer().getId() == customerUserDetail.getId()) {
                    check2 = true;
                    model.addAttribute("check", check);
                    model.addAttribute("check2", check2);
                    model.addAttribute("thongbao", "Voucher này chỉ được giảm cho một hóa đơn duy nhất, hẹn quý khách ở các chương trình khuyến mại sau!");
                    session.setAttribute("pageView", "/user/page/promotion/voucherdetail.html");
                    return "/user/index";
                }
            }
            if (soluong <= voucher.getQuantity()) {
                check = true;
                model.addAttribute("check", check);
                model.addAttribute("check2", check2);
                //add vouchercustomer;
                VoucherCustomerRes voucherCustomer = new VoucherCustomerRes();
                voucherCustomer.setCustomer(customerUserDetail.getId());
                voucherCustomer.setVoucher(id);
                voucherCustomer.setActive(voucher.getActive());
                voucherCustomerService.save(voucherCustomer);
            } else {
                check = false;
                model.addAttribute("check", check);
                model.addAttribute("check2", check2);
                model.addAttribute("thongbao", "Số lượng voucher đã hết, hẹn quý khách ở các chương trình khuyến mại sau!");
            }
        } else {
            model.addAttribute("check", check);
            model.addAttribute("check2", check2);
            model.addAttribute("thongbao", "Bạn cần đăng nhập để nhận voucher!");
        }
        session.setAttribute("pageView", "/user/page/promotion/voucherdetail.html");
        return "/user/index";
    }

    @Autowired
    UserServiceImpl customerService;
}
