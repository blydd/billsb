package com.bgt.billsb.entity;


/**
 * 账单表对应实体类
 */
public class Bill {
    private Integer id;
    private Double money;
    private String billTime;
    private String createTime;
    private Integer billTypeId;
    private Integer payTypeId;
    /**
     * 1 支出,2 收入,3 不计入
     */
    private Integer inout;
    private String desc;

    public Bill() {
    }
    public Bill(Integer id, Double money, String billTime, String createTime, Integer billTypeId, Integer payTypeId, Integer inout, String desc) {
        this.id = id;
        this.money = money;
        this.billTime = billTime;
        this.createTime = createTime;
        this.billTypeId = billTypeId;
        this.payTypeId = payTypeId;
        this.inout = inout;
        this.desc = desc;
    }

    public Integer getInout() {
        return inout;
    }

    public void setInout(Integer inout) {
        this.inout = inout;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getBillTypeId() {
        return billTypeId;
    }

    public void setBillTypeId(Integer billTypeId) {
        this.billTypeId = billTypeId;
    }

    public Integer getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(Integer payTypeId) {
        this.payTypeId = payTypeId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}