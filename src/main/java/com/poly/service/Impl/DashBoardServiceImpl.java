package com.poly.service.Impl;

import com.poly.entity.Bill;
import com.poly.repository.BillRepos;
import com.poly.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    BillRepos billRepos;


    @Override
    public List<Bill> getBillByDate(Date date) {
        List<Bill> orders = billRepos.findBillByDate(date);
        return  orders ;
    }

    @Override
    public List<Bill> getBillReturned(Date date) {
        List<Bill> orders = billRepos.findBillByDate(date);
        List<Bill> listBillReturn = new ArrayList<>();
        for(Bill bill : orders){
            if(bill.getBillStatus().getStatus().equals("RR")){
                listBillReturn.add(bill);
            }
        }
        return listBillReturn;
    }
    @Override
    public List<Bill> getBillByProccesing(Date date) {
        List<Bill> orders = billRepos.findBillByDate(date);
        List<Bill> listBillPro = new ArrayList<>();
        for(Bill bill : orders){
            if(bill.getBillStatus().getStatus().equals("WP")){
                listBillPro.add(bill);
            }
        }
        return listBillPro;
    }

    @Override
    public List<Bill> getAllBill() {
        return this.billRepos.findAll();
    }

    @Override
    public List<Bill> getAllBillReturn() {
        List<Bill> orders = this.billRepos.findAll();
        List<Bill> listBillReturn = new ArrayList<>();
        for(Bill bill : orders){
            if(bill.getBillStatus().getCode().equals("RR")){
                listBillReturn.add(bill);
            }
        }
        return listBillReturn;
    }
    @Override
    public List<Bill> getAllBillProcessing() {
        List<Bill> orders = this.billRepos.findAll();
        List<Bill> listBillPro = new ArrayList<>();
        for(Bill bill : orders){
            if(bill.getBillStatus().getCode().equals("WP")){
                listBillPro.add(bill);
            }
        }
        return listBillPro;
    }
    @Override
    public List<Bill> getAllBillDelivering() {
        List<Bill> orders = this.billRepos.findAll();
        List<Bill> listBillPro = new ArrayList<>();
        for(Bill bill : orders){
            if(bill.getBillStatus().getCode().equals("DE")){
                listBillPro.add(bill);
            }
        }
        return listBillPro;
    }
}
