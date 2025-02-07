package com.bgt.billsb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User {
    private Long id;
    private String name;
    private Integer age;
}