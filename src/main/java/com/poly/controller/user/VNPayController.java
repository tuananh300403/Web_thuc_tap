package com.poly.controller.user;

import com.poly.dto.BillProRes;
import com.poly.entity.Bill;
import com.poly.repository.BillStatusRepos;
import com.poly.repository.PaymentMethodRepos;
import com.poly.service.BillService;
import com.poly.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Controller
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    BillService billService;
    @Autowired
    private BillStatusRepos billStatusRepos;
    @Autowired
    private PaymentMethodRepos paymentMethodRepos;

    @GetMapping("/vnpay")
    public String home() {
        return "/user/page/product/vnpay.html";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") BigDecimal orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request,
                             HttpServletResponse response, Model model, @ModelAttribute(value = "billProduct") BillProRes billProRes) throws IOException {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String responseCode = request.getParameter("vnp_ResponseCode");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);
        model.addAttribute("vnp_ResponseCode", responseCode);
        Bill bill = billService.findByCode(orderInfo);
        if ("00".equals(responseCode)) {
            // Giao dịch thành công
            // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
//                Contract contract = contractRepository.findById(Integer.parseInt(queryParams.get("contractId")))
//                        .orElseThrow(() -> new NotFoundException("Không tồn tại hợp đồng này của sinh viên"));
//                contract.setStatus(1);
//                contractRepository.save(contract);
            bill.setPaymentStatus(1);
            bill.setPaymentMethod(paymentMethodRepos.findById(2).get());
            billService.update(bill, bill.getId());
            return paymentStatus == 1 ? "/user/page/product/ordersuccsess" : "/user/page/product/orderfail";
        }

        return paymentStatus == 1 ? "/user/page/product/ordersuccsess" : "/user/page/product/orderfail";
    }

    @GetMapping("/payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String contractId = queryParams.get("contractId");
        String registerServiceId = queryParams.get("registerServiceId");
        if (contractId != null && !contractId.equals("")) {
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
//                Contract contract = contractRepository.findById(Integer.parseInt(queryParams.get("contractId")))
//                        .orElseThrow(() -> new NotFoundException("Không tồn tại hợp đồng này của sinh viên"));
//                contract.setStatus(1);
//                contractRepository.save(contract);
                response.sendRedirect("http://localhost:4200/info-student");
            } else {
                // Giao dịch thất bại
                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
                response.sendRedirect("http://localhost:4200/payment-failed");

            }
        }
        if (registerServiceId != null && !registerServiceId.equals("")) {
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
//                RegisterServices registerServices = registerServicesRepository.findById(Integer.parseInt(queryParams.get("registerServiceId")))
//                        .orElseThrow(() -> new NotFoundException("Không tồn tại dịch vụ này của sinh viên"));
//                registerServices.setStatus(1);
//                registerServicesRepository.save(registerServices);
                response.sendRedirect("http://localhost:8080/info-student");
            } else {
                // Giao dịch thất bại
                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
                response.sendRedirect("http://localhost:8080/payment-failed");

            }
        }
    }
}