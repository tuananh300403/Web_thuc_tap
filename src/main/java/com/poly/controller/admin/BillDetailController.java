package com.poly.controller.admin;

import com.poly.entity.Bill;
import com.poly.entity.BillProduct;
import com.poly.entity.BillStatus;
import com.poly.service.BillService;
import com.poly.service.BillStatusService;
import com.poly.service.DeliveryNotesSevice;
import com.poly.service.PaymentMethodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class BillDetailController {



    @Autowired
    private BillService billService;

    @Autowired
    private BillStatusService billStatusService;

    @Autowired
    private DeliveryNotesSevice deliveryNotesSevice;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/bill/bill_detail/{billCode}")
    public String loadBillById(HttpSession session, Model model,
                               @PathVariable(name = "billCode") Integer idBill) {
        model.addAttribute("deliveryNote", deliveryNotesSevice.getByIdBill(idBill));

        Bill bill = billService.getOneById(idBill);
        model.addAttribute("billDetail", bill);

        List<BillProduct> billProducts = bill.getBillProducts();
        BigDecimal totalPrice = billProducts.stream()
                .map(billProduct -> billProduct.getPrice().multiply(BigDecimal.valueOf(billProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        BigDecimal reduceMoney = billProducts.stream()
//                .map(billProduct -> billProduct.getReducedMoney().multiply(BigDecimal.valueOf(billProduct.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentMethod", paymentMethodService.getAll());
//        model.addAttribute("totalReduceMoney", reduceMoney);
        BigDecimal totalAfter = new BigDecimal(0);
//        if (bill.getVoucher() != null && bill.getVoucher().isReducedForm() == true) {// giảm %
//            totalAfter = totalPrice.subtract(reduceMoney);
//            BigDecimal reduce = totalPrice.multiply(BigDecimal.valueOf(bill.getVoucher().getValue() / 100));
//            if (reduce.compareTo(bill.getVoucher().getMaximumDiscount()) >= 0) {
//                reduce = bill.getVoucher().getMaximumDiscount();
//            }
//            totalAfter = totalAfter.subtract(reduce);
//        }
//        if (bill.getVoucher() != null && bill.getVoucher().isReducedForm() == false) {
//            totalAfter = totalPrice.subtract(reduceMoney).subtract(BigDecimal.valueOf(bill.getVoucher().getValue()));
//        }
//        if (bill.getVoucher() == null) {
//            totalAfter = totalPrice.subtract(reduceMoney);
//        }
        model.addAttribute("totalAfter", totalAfter);
        List<BillStatus> billStatusList = billStatus(billStatusService.getAll(), bill.getBillStatus().getCode());
        model.addAttribute("billStatus", billStatusList);

        session.setAttribute("pageView", "/admin/page/bill/bill_detail.html");
        session.setAttribute("active", "/bill/list_bill");
        return "/admin/layout";
    }

    @PostMapping("/bill/bill_detail/update_status/{id}")
    public String updateBillStatus(@PathVariable("id") Integer id, Model model,
                                   @RequestParam(name = "status", required = false) String status,
                                   @RequestParam(name = "paymentStatus", required = false,defaultValue = "-1") Integer paymentStatus,
                                   @RequestParam(name = "paymentMethod", required = false,defaultValue = "-1") Integer paymentMethod) {
        Bill bill = new Bill();
        bill.setBillStatus(billStatusService.getOneBycode(status));
        bill.setPaymentMethod(paymentMethodService.getOne(paymentMethod));
        bill.setPaymentStatus(paymentStatus);
        billService.update(bill, id);
        return "redirect:/admin/bill/bill_detail/" + id;
    }

    private List<BillStatus> billStatus(List<BillStatus> billStatusList, String code) {
        if (code.equals("WP")) {
            billStatusList.removeIf(s -> s.getCode().equals("WP") &&
                    s.getCode().equals("PG") &&
                    s.getCode().equals("DE") &&
                    s.getCode().equals("CO") &&
                    s.getCode().equals("SC") &&
                    s.getCode().equals("CC")
            );
        }
        if (code.equals("PG")) {
            billStatusList.removeIf(s -> !s.getCode().equals("PG") &&
                    !s.getCode().equals("DE") &&
                    !s.getCode().equals("CO") &&
                    !s.getCode().equals("SC") &&
                    !s.getCode().equals("CC")
            );
        }
        if (code.equals("DE")) {
            billStatusList.removeIf(s -> !s.getCode().equals("DE") &&
                    !s.getCode().equals("DEE") &&
                    !s.getCode().equals("CO") &&
                    !s.getCode().equals("SC") &&
                    !s.getCode().equals("CC")
            );
        }
        if (code.equals("CO")) {
            billStatusList.removeIf(s -> !s.getCode().equals("CO") &&
                    !s.getCode().equals("RR") &&
                    !s.getCode().equals("WR") &&
                    !s.getCode().equals("RE")
            );
        }
        if (code.equals("RR")) {
            billStatusList.removeIf(s -> !s.getCode().equals("RR") &&
                    !s.getCode().equals("CO") &&
                    !s.getCode().equals("WR") &&
                    !s.getCode().equals("RE")
            );
        }
        if (code.equals("WR")) {
            billStatusList.removeIf(s -> !s.getCode().equals("WR") &&
                    !s.getCode().equals("RR") &&
                    !s.getCode().equals("RE")
            );
        }
        if (code.equals("DEE")) {
            billStatusList.removeIf(s -> !s.getCode().equals("DEE") &&
                    !s.getCode().equals("DE") &&
                    !s.getCode().equals("SC") &&
                    !s.getCode().equals("CC")
            );
        }
        if (code.equals("SC")) {
            billStatusList.removeIf(s -> !s.getCode().equals("SC")
            );
        }
        if (code.equals("CC")) {
            billStatusList.removeIf(s -> !s.getCode().equals("CC")
            );
        }
        if (code.equals("RE")) {
            billStatusList.removeIf(s -> !s.getCode().equals("RE") &&
                    !s.getCode().equals("RR")
            );
        }

        return billStatusList;
    }
}
