package com.poly.controller.admin;

import com.poly.dto.formReturnDto;
import com.poly.entity.Bill;
import com.poly.entity.BillProduct;
import com.poly.entity.BillStatus;
import com.poly.service.BillProductService;
import com.poly.service.BillService;
import com.poly.service.BillStatusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class BillReturnController {

    @Autowired
    BillService billService;

    @Autowired
    BillProductService billProductService;

    @Autowired
    BillStatusService billStatusService;

    @GetMapping("/bill/list_invoice_return")
    public String getListInvoice(HttpSession session, Model model){
        session.setAttribute("pageView", "/admin/page/bill/invoice_return.html");
        session.setAttribute("active", "/bill/invoice_return");
        List<Bill> ListB =this.billService.findBillReturnByStatus("RR");
        model.addAttribute("listBill",ListB);
        model.addAttribute("formReturnDto", new formReturnDto());
        return "admin/layout";
    }
    @GetMapping("/invoice_return/{id}")
    public String getInvoiceReturn(HttpSession session, Model model, @PathVariable("id") Integer id){
        session.setAttribute("pageView", "/admin/page/bill/billProduct_return.html");
        session.setAttribute("active", "/bill/invoice_return");
        List<BillProduct> product=this.billProductService.findBillProductReturn(1,id);
        if(product.size()==0){
            Bill bill = this.billService.getOneById(id);
            BillStatus  billStatus =this.billStatusService.getOneBycode("CO ");
            bill.setBillStatus(billStatus);
            this.billService.add(bill);
            return "redirect:/admin/bill/list_invoice_return";
        }
        model.addAttribute("listBillReturn",product);
        return "admin/layout";
    }

    @RequestMapping("/refuse/{id}")
    public String getRefuse(HttpSession session,Model model,
                            @PathVariable("id") Integer id,
                            @ModelAttribute("formReturnDto") formReturnDto dto
                            ){
        BillProduct billProduct =this.billProductService.edit(id);
        billProduct.setNote(dto.getNote());
        billProduct.setStatus(2); // tu choi tra hang
        this.billProductService.save(billProduct);
        return "redirect:/admin/invoice_return/"+billProduct.getBill().getId();
    }

    @RequestMapping("/agree/{id}")
    public String getAgree(HttpSession session,Model model,
                            @PathVariable("id") Integer id,
                            @ModelAttribute("formReturnDto") formReturnDto dto
    ){
        BillProduct billProduct =this.billProductService.edit(id);
        billProduct.setNote(dto.getNote());
        billProduct.setQuantityAcceptReturn(Integer.parseInt(dto.getQuantity()));
        billProduct.setStatus(3); //dong y
        this.billProductService.save(billProduct);
        return "redirect:/admin/invoice_return/"+billProduct.getBill().getId();
    }
    @GetMapping("/agree")
    public String getViewAgree(HttpSession session,Model model){
        session.setAttribute("pageView", "/admin/page/bill/agree_bill_return.html");
        model.addAttribute("listAgree",this.billProductService.findBillByStatus(3));
        return "admin/layout";
    }
    @GetMapping("/refuse")
    public String getViewRefuse(HttpSession session,Model model){
        session.setAttribute("pageView", "/admin/page/bill/refuse_bill_return.html");
        model.addAttribute("listRefuse",this.billProductService.findBillByStatus(2));
        return "admin/layout";
    }

}
