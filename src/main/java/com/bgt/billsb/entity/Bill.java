package com.bgt.billsb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_bill")
public class Bill {
    private Integer id;
    private Double money;
    private LocalDateTime billTime;
    private LocalDateTime createTime;
    private Integer billTypeId;
    private Integer payTypeId;
    private String desc;
}