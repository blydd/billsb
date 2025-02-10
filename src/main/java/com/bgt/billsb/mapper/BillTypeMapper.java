package com.bgt.billsb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bgt.billsb.entity.Bill;
import com.bgt.billsb.entity.BillType;
import org.springframework.stereotype.Repository;

@Repository
public interface BillTypeMapper extends BaseMapper<BillType> {
}