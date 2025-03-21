package com.bgt.billsb.service;// 服务层接口

import com.bgt.billsb.dto.BillDto;
import com.bgt.billsb.entity.Bill;
import com.bgt.billsb.vo.BillDetail;
import com.bgt.billsb.vo.BillTypeVo;
import com.bgt.billsb.vo.PayTypeVo;

import java.util.List;

public interface BillService {
    List<BillDetail> getAll(BillDto param);

    List<BillTypeVo> getBillTypes(String type);

    List<PayTypeVo> getPayTypes();

    void addBill(Bill newBill);
    void updateBill(Bill newBill);

    void deleteBill(Integer id);
}
