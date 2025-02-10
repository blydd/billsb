package com.bgt.billsb.service.impl;
import com.bgt.billsb.mapper.PayTypeMapper;
import com.bgt.billsb.service.PayTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PayTypeServiceImpl implements PayTypeService {

    @Autowired
    private PayTypeMapper payTypeMapper;

}