package com.bgt.billsb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_bill_type")
public class BillType {
    private Integer id;
    /**
     * 账单类别,例如:衣食住行
     */
    private String billType;
    /**
     * 图标,例如:yi
     */
    private String icon;
    /**
     * 排序
     */
    private Integer sort;
}