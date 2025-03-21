package com.bgt.billsb.vo;

/**
 * @author: bgt
 * @Date: 2025/2/5 14:52
 * @Desc: 账单类别
 */
public class BillTypeVo {
 private Integer id;
 /**
  * 衣食住行等
  */
 private String billType;
 private String icon;
 private Integer sort;
 /**
  * 支出 / 收入
  */
 private String type;

 public BillTypeVo(Integer id, String billType, String icon, Integer sort) {
  this.id = id;
  this.billType = billType;
  this.icon = icon;
  this.sort = sort;
 }

 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
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

 public Integer getSort() {
  return sort;
 }

 public void setSort(Integer sort) {
  this.sort = sort;
 }
}
