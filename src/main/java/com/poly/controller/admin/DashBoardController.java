package com.poly.controller.admin;

import com.poly.entity.Bill;
import com.poly.service.DashBoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashBoardController {
    @Autowired
    DashBoardService dashBoardService;

    @GetMapping("dashboard/list")
    public ResponseEntity<List<Bill>> getRevenueByDay(Model model,@RequestParam("date") Date date) {
    List<Bill> billList =this.dashBoardService.getBillByDate(date);
        return ResponseEntity.ok(billList);
    }
    @GetMapping("dashboard/listReturn")
    public ResponseEntity<List<Bill>> getBillReturn(@RequestParam("date") Date date) {
        List<Bill> billList =this.dashBoardService.getBillReturned(date);
        return ResponseEntity.ok(billList);
    }
    @GetMapping("dashboard/listProcessing")
    public ResponseEntity<List<Bill>> getBillProcessing(@RequestParam("date") Date date) {
        List<Bill> billList =this.dashBoardService.getBillByProccesing(date);
        return ResponseEntity.ok(billList);
    }


}
