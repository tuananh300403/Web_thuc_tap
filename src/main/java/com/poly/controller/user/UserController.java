package com.poly.controller.user;

import com.poly.common.CheckLogin;
import com.poly.dto.ChangeInforDto;
import com.poly.dto.ReturnDto;
import com.poly.dto.UserDetailDto;
import com.poly.entity.Bill;
import com.poly.service.BillProductService;
import com.poly.service.BillService;
import com.poly.service.BillStatusService;
import com.poly.service.ImageReturnService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    BillService billService;

    @Autowired
    BillStatusService billStatusService;

    @Autowired
    ImageReturnService imageReturnService;

    @Autowired
    BillProductService billProductService;


   @Autowired
    CheckLogin checkLogin;




    @GetMapping("/invoice")
    public String loadInvoice(HttpSession session) {
        session.setAttribute("pageView", "/user/page/invoice/search_invoice.html");
        return "/user/index";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER','STAFF','ADMIN')")
    public String profile(HttpSession session, Model model) {
        session.setAttribute("pageView", "/user/page/profile/profile.html");
        model.addAttribute("changeInfo", new ChangeInforDto());
        return "/user/index";
    }

    @GetMapping("/invoice/invoice_detail/{id}")
    public String loadInvoiceDetail(HttpSession session, Model model, @PathVariable("id") Integer id) {
        Bill bill = this.billService.getOneById(id);
        model.addAttribute("bill", bill);
        model.addAttribute("billProducts", bill.getBillProducts());
        session.setAttribute("pageView", "/user/page/invoice/detail_invoice.html");
        return "/user/index";
    }

    @GetMapping("/order")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','STAFF')")
    public String order(HttpSession session, Model model) {
        session.setAttribute("pageView", "/user/page/profile/order.html");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetailDto customerUserDetail = (UserDetailDto) userDetails;
        List<Bill> billList = this.billService.findAllBillByUser(customerUserDetail.getId());
        Date today = new Date();
        List<Bill> listBillFilter= this.billService.listBillFilter(billList);
        List<Bill> listBillFilterStill= this.billService.listBillFilterStill(billList);
        model.addAttribute("listBillCheck", listBillFilter);
        model.addAttribute("listBillFilterStill", listBillFilterStill);
        model.addAttribute("today", today);
        model.addAttribute("bill", billList);
        return "/user/index";
    }

    @PostMapping("/return/{id}")
    public String returnProduct(HttpSession session,
                                @PathVariable("id") Integer id,
                                @RequestBody List<ReturnDto> returnDto) {
       this.billService.logicBillReturn(id,returnDto);
        if(checkLogin.checkLogin() == null){
         return "redirect:/";
        }
        return "redirect:/order";
    }



    @GetMapping("/search_order")
    public String getSearch(HttpSession session){
        session.setAttribute("pageView", "/user/page/search/search_order.html");
        return "/user/index";
    }

    @PostMapping("/search_order_user")
    public String getSearchOder(@ModelAttribute("search") String search,HttpSession session){
         Bill bill =  this.billService.findByCode(search);
         session.setAttribute("bill",bill);
         session.setAttribute("bool",this.billService.checkBillNoLogin(search));
        return "redirect:/search_order";
    }

}
