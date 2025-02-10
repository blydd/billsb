package com.bgt.billsb.service.impl;
import com.bgt.billsb.mapper.BillTypeMapper;
import com.bgt.billsb.service.BillTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BillTypeServiceImpl implements BillTypeService {

    @Autowired
    private BillTypeMapper billTypeMapper;

}