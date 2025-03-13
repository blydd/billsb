package com.bgt.billsb.service.impl;
import com.bgt.billsb.dao.BillDao;
import com.bgt.billsb.entity.Bill;
import com.bgt.billsb.service.BillService;
import com.bgt.billsb.vo.BillDetail;
import com.bgt.billsb.vo.BillTypeVo;
import com.bgt.billsb.vo.PayTypeVo;

import java.util.List;


public class BillServiceImpl implements BillService {

    private final BillDao billDao = new BillDao();

    @Override
    public List<BillDetail> getAll() {
        return billDao.getAllBills();
    }

    @Override
    public List<BillTypeVo> getBillTypes() {
        return billDao.getBillTypes();
    }

    @Override
    public List<PayTypeVo> getPayTypes() {
        return billDao.getPayTypes();
    }

    @Override
    public void addBill(Bill newBill) {
        billDao.addBill(newBill);
    }

    @Override
    public void deleteBill(Integer id) {
        billDao.deleteBill(id);
    }
}