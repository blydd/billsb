package com.bgt.billsb.controller;

import com.bgt.billsb.cell.BillCell;
import com.bgt.billsb.service.BillService;
import com.bgt.billsb.service.impl.BillServiceImpl;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class MainController {

    @FXML
    private TabPane tabPane;
    //测试数据
    private List<BillDay> datas = new ArrayList<>();

    private final BillService billService = new BillServiceImpl();

    @FXML
    public void initialize() {
//        Platform.runLater(() -> {
//            loadUI();
//        });


    }

    public void  loadUI(){
        try {
            //加载明细页面
            System.out.println("初始化" + tabPane.getTabs().get(0).getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bgt/bill/bill.fxml"));
            BorderPane billView = loader.load();
            ListView billsListView = (ListView) billView.lookup("#billsListView");
            // 创建一个 ObservableList 并添加集合元素
            ObservableList<BillDay> observableBillList = FXCollections.observableArrayList(datas);
            billsListView.getItems().addAll(observableBillList);
            //设置每行都可选择
            billsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            billsListView.setCellFactory(d -> new BillCell(datas));

            Tab page1Tab = tabPane.getTabs().get(0);
            page1Tab.setContent(billView);


            //加载统计页面
            System.out.println("初始化" + tabPane.getTabs().get(1).getText());
//            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/com/bgt/bill/statistics.fxml"));
            BorderPane statistics = FXMLLoader.load(getClass().getResource("/com/bgt/bill/statistics.fxml"));
//            BorderPane statistics = loader2.load();
            //获取pie统计图
            PieChart pieChart = (PieChart) statistics.lookup("#pieChart");
            //组装数据,按账单类别统计
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            for (BillDay data : datas) {
                Map<String, Double> groupByType = data.getBillDetailList().stream().collect(Collectors.groupingBy(BillDetail::getBillType, Collectors.summingDouble(BillDetail::getMoney)));
                groupByType.forEach((k, v) -> {
                    pieData.add(new PieChart.Data(k, v));
                });

            }
            pieChart.setData(pieData);

            Tab page2Tab = tabPane.getTabs().get(1);
            page2Tab.setContent(statistics);


            // 加载页面 3 的 FXML 文件
            System.out.println("初始化" + tabPane.getTabs().get(2).getText());
            FXMLLoader page3Loader = new FXMLLoader(getClass().getResource("/com/bgt/bill/setup.fxml"));
            BorderPane page3Content = page3Loader.load();
            Tab page3Tab = tabPane.getTabs().get(2);
            page3Tab.setContent(page3Content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}