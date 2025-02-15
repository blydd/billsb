package com.bgt.billsb.vo;

/**
 * @author: bgt
 * @Date: 2025/2/5 14:52
 * @Desc: 账单类别
 */
public class PayTypeVo {
 private Integer id;
 /**
  * 支付类别:1-储蓄账户,2-信贷账户
  */
 private String payType;
 /**
  * 支付账户编码 例如:guangfa
  */
 private String payAccountCode;
 /**
  * 支付账户名称 例如:广发信用卡
  */
 private String payAccountName;

 public PayTypeVo(Integer id, String payType, String payAccountCode, String payAccountName) {
  this.id = id;
  this.payType = payType;
  this.payAccountCode = payAccountCode;
  this.payAccountName = payAccountName;
 }

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

 public String getPayType() {
  return payType;
 }

 public void setPayType(String payType) {
  this.payType = payType;
 }

 public String getPayAccountCode() {
  return payAccountCode;
 }

 public void setPayAccountCode(String payAccountCode) {
  this.payAccountCode = payAccountCode;
 }

 public String getPayAccountName() {
  return payAccountName;
 }

 public void setPayAccountName(String payAccountName) {
  this.payAccountName = payAccountName;
 }
}
