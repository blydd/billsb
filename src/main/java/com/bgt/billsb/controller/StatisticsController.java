package com.bgt.billsb.controller;

import com.bgt.billsb.util.TestUtil;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsController implements TabController {

    //测试数据
    private List<BillDay> datas = new ArrayList<>();


    @FXML
    private PieChart pieChart;

    @Override
    public void loadData() {
        datas = TestUtil.getDatas();

        //组装数据,按账单类别统计
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        //把datas中的BillDetailList都提取到一个集合中
        List<BillDetail> billDetails = new ArrayList<>();
        for (BillDay data : datas) {
            billDetails.addAll(data.getBillDetailList());
        }

        Map<String, Double> groupByType = billDetails.stream().collect(Collectors.groupingBy(BillDetail::getBillType, Collectors.summingDouble(BillDetail::getMoney)));
        groupByType.forEach((k, v) -> {
            pieData.add(new PieChart.Data(k, v));
        });

        pieChart.setData(pieData);
        //设置图例
        pieChart.setLegendSide(Side.LEFT);
        //悬浮展示具体数值
    }
}