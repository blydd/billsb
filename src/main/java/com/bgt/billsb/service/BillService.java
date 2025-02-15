package com.bgt.billsb.service;// 服务层接口

import com.bgt.billsb.entity.Bill;
import com.bgt.billsb.vo.BillDetail;
import com.bgt.billsb.vo.BillTypeVo;
import com.bgt.billsb.vo.PayTypeVo;

import java.util.List;

public interface BillService {
    List<BillDetail> getAll();

    List<BillTypeVo> getBillTypes();

    List<PayTypeVo> getPayTypes();

    void addBill(Bill newBill);
}
