package com.poly.service;

import com.poly.dto.BillProRes;
import com.poly.dto.ReturnDto;
import com.poly.dto.SearchBillDto;
import com.poly.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillService {

    Page<Bill> loadData(SearchBillDto searchBillDto, Pageable pageable);

    Bill add(Bill bill);

     Bill add(BillProRes bill);

    public void addBillPro(Bill bill, BillProRes billProRes);

    Bill update(Bill bill, Integer id);

    Boolean delete(Integer id);

    Bill getOneById(Integer id);

    List<Bill> findAllBillByUser(Integer idInteger);

    List<Bill> findBillReturnByStatus(String code);

    Bill findByCode(String code);

    List<Bill> listBillFilter(List<Bill> billList);

    List<Bill> listBillFilterStill(List<Bill> billList);

    void logicBillReturn(Integer id, List<ReturnDto> returnDto);

    Boolean checkBillNoLogin(String code);
}
