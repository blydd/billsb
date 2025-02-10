package com.bgt.billsb.service.impl;
import com.bgt.billsb.entity.Bill;
import com.bgt.billsb.mapper.BillMapper;
import com.bgt.billsb.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillMapper billMapper;

    @Override
    public List<Bill> getAll() {
        return billMapper.selectList(null);
    }
}