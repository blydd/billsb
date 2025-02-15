package com.bgt.billsb.vo;

/**
 * @author: bgt
 * @Date: 2025/2/5 14:52
 * @Desc:
 */
public class BillDetail {
 private Integer id;
 private Double money;
 //账单时间
 private String billTime;
 //账单创建时间
 private String createTime;
 // 备注
 private String desc;
 //账单类型 例如:衣食住行
 private String billType;
 //账单类型图标,例如:yi
 private String icon;
 //支付类型 储蓄账户 信贷账户
 private String payType;
 private Integer inout;
 //支付账户 例如:建设信用卡
 private String payAccountName;
 //账单日期
 private String billDay;
 //账单日期
 private String billMonth;

 public BillDetail(Integer id, Double money, String billTime, String createTime, String desc, String billType, String icon, String payType, Integer inout, String payAccountName) {
  this.id = id;
  this.money = money;
  this.billTime = billTime;
  this.createTime = createTime;
  this.desc = desc;
  this.billType = billType;
  this.icon = icon;
  this.payType = payType;
  this.inout = inout;
  this.payAccountName = payAccountName;
 }

 public String getBillDay() {
  return billDay;
 }

 public void setBillDay(String billDay) {
  this.billDay = billDay;
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

 public String getDesc() {
  return desc;
 }

 public void setDesc(String desc) {
  this.desc = desc;
 }

 public String getBillType() {
  return billType;
 }

 public void setBillType(String billType) {
  this.billType = billType;
 }

 public String getIcon() {
  return icon;
 }

 public void setIcon(String icon) {
  this.icon = icon;
 }

 public String getPayType() {
  return payType;
 }

 public void setPayType(String payType) {
  this.payType = payType;
 }

 public String getPayAccountName() {
  return payAccountName;
 }

 public void setPayAccountName(String payAccountName) {
  this.payAccountName = payAccountName;
 }

}
