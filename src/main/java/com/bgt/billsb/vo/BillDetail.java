package com.bgt.billsb.vo;

/**
 * @author: bgt
 * @Date: 2025/2/5 14:52
 * @Desc:
 */
public class BillDetail {
 private String billType;
 private String desc;
 private Double money;
 private String time;

 public String getBillType() {
  return billType;
 }

 public void setBillType(String billType) {
  this.billType = billType;
 }

 public String getDesc() {
  return desc;
 }

 public void setDesc(String desc) {
  this.desc = desc;
 }

 public Double getMoney() {
  return money;
 }

 public void setMoney(Double money) {
  this.money = money;
 }

 public String getTime() {
  return time;
 }

 public void setTime(String time) {
  this.time = time;
 }
}
