package com.bgt.billsb.controller;

import com.bgt.billsb.util.DataUtil;
import com.bgt.billsb.util.EventBusUtil;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class StatisticsController/* implements TabController*/ {

    @FXML
    private PieChart pieChart;

    /**
     * 初始化方法
     */
    public void initialize() {
        //注册事件总线
        EventBusUtil.getDefaut().register(this);
        DataUtil.queryData();
    }


//    @Override
//    public void loadData() {
//        System.out.println("StatisticsController.loadData.......................");
//        //组装数据,按账单类别统计
//        //把datas中的BillDetailList都提取到一个集合中
//        List<BillDetail> billDetails = new ArrayList<>();
//        for (BillDay data : datas) {
//            billDetails.addAll(data.getBillDetailList());
//        }
//
//        Map<String, Double> groupByType = billDetails.stream().collect(Collectors.groupingBy(BillDetail::getBillType, Collectors.summingDouble(BillDetail::getMoney)));
//        groupByType.forEach((k, v) -> {
//            pieData.add(new PieChart.Data(k, v));
//        });
//
//        pieChart.setData(pieData);
//        //设置图例
//        pieChart.setLegendSide(Side.LEFT);
//        //悬浮展示具体数值
//    }


}