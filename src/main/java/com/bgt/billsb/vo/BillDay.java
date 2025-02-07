package com.bgt.billsb.vo;

import java.util.List;

/**
 * @author: bgt
 * @Date: 2025/2/5 14:52
 * @Desc:
 */
public class BillDay {
 private String date;
 private Double dayTotalOut;
 private Double dayTotalIn;
 private List<BillDetail> billDetailList;

 public String getDate() {
  return date;
 }

 public void setDate(String date) {
  this.date = date;
 }

 public Double getDayTotalOut() {
  return dayTotalOut;
 }

 public void setDayTotalOut(Double dayTotalOut) {
  this.dayTotalOut = dayTotalOut;
 }

 public Double getDayTotalIn() {
  return dayTotalIn;
 }

 public void setDayTotalIn(Double dayTotalIn) {
  this.dayTotalIn = dayTotalIn;
 }

 public List<BillDetail> getBillDetailList() {
  return billDetailList;
 }

 public void setBillDetailList(List<BillDetail> billDetailList) {
  this.billDetailList = billDetailList;
 }
}
