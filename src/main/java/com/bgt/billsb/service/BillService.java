package com.bgt.billsb.service;// 服务层接口

import com.bgt.billsb.entity.Bill;

import java.util.List;

public interface BillService {
    List<Bill> getAll();

}
