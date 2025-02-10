package com.bgt.billsb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_pay_type")
public class PayType {
    private Integer id;
    /**
     * 支付类别:1-储蓄账户,2-信贷账户
     */
    private String payType;
    /**
     * 支付账户编码,例如:wxpay
     */
    private String payAccountCode;
    /**
     * 支付账户名称 例如:微信,广发信用卡
     */
    private String payAccountName;
}