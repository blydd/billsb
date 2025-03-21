package com.bgt.billsb.dto;


/**
 * 账单表查询类
 */
public class BillDto {
    //账单月份
    private String billMonth;
    //支出 收入 不计入
    private Integer inout;
    //账单类型
    private String billType;
    //支付方式
    private String payType;
    //账单id
    private Integer id;

    //是否推送事件总线
    private boolean isPushEvent = true;

    public boolean isPushEvent() {
        return isPushEvent;
    }

    public void setPushEvent(boolean pushEvent) {
        isPushEvent = pushEvent;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public Integer getInout() {
        return inout;
    }

    public void setInout(Integer inout) {
        this.inout = inout;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}