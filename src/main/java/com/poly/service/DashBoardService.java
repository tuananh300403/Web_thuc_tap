package com.poly.service;

import com.poly.entity.Bill;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;


@Service
public interface DashBoardService {
   List<Bill> getBillByDate(Date date);
   List<Bill> getBillReturned(Date date);
   List<Bill> getBillByProccesing(Date date);
   List<Bill> getAllBill();
   List<Bill> getAllBillReturn();
   List<Bill> getAllBillProcessing();
   List<Bill> getAllBillDelivering();
}
