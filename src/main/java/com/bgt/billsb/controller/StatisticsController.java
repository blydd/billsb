package com.bgt.billsb.controller;

import com.bgt.billsb.service.BillService;
import com.bgt.billsb.service.impl.BillServiceImpl;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsController implements TabController {

    //测试数据
    private List<BillDay> datas = new ArrayList<>();


    @FXML
    private PieChart pieChart;

    private final BillService billService = new BillServiceImpl();
    /**
     * 初始化方法
     */
    public void initialize() {
        loadDataAsync();
    }

    /**
     * 异步加载数据
     */
    private void loadDataAsync() {
        Service<List<BillDetail>> dataService = new Service<>() {
            @Override
            protected Task<List<BillDetail>> createTask() {
                return new Task<>() {
                    @Override
                    protected List<BillDetail> call() {
                        return billService.getAll();
                    }
                };
            }
        };

        dataService.setOnSucceeded(e -> {
                    //查询完毕后把数据转成页面所需格式
                    List<BillDetail> value = dataService.getValue();

                    Map<String, List<BillDetail>> groupByBilltime = value.stream().collect(Collectors.groupingBy(a -> a.getBillTime()));
                    Iterator<Map.Entry<String, List<BillDetail>>> it = groupByBilltime.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, List<BillDetail>> next = it.next();
                        BillDay billDay = new BillDay();
                        billDay.setDate(next.getKey());
                        List<BillDetail> billDetails = next.getValue();
                        billDay.setBillDetailList(billDetails);

                        billDay.setDayTotalIn(billDetails.stream().filter(a -> a.getMoney() > 0D).mapToDouble(BillDetail::getMoney).sum());
                        billDay.setDayTotalOut(billDetails.stream().filter(a -> a.getMoney() < 0D).mapToDouble(BillDetail::getMoney).sum());

                        datas.add(billDay);
                    }
                    loadData();
                    System.out.println("查询到数据的账单数量 = " + value.size());
                }
        );
        dataService.start();
    }


    @Override
    public void loadData() {
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