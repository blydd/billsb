package com.bgt.billsb.controller;

import com.bgt.billsb.cell.BillCell;
import com.bgt.billsb.service.BillService;
import com.bgt.billsb.service.impl.BillServiceImpl;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BillController implements TabController {

    @FXML
    private ListView billsListView;
    //账单数据
    public static List<BillDay> datas = new ArrayList<>();


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

                    Map<String, List<BillDetail>> groupByBilltime = value.stream().map(bi->{
                        bi.setBillDay(bi.getBillTime().split(" ")[0]);
                        return bi;
                    }).collect(Collectors.groupingBy(a -> a.getBillDay()));
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


    /**
     * 点击"记一笔"按钮
     */
    @FXML
    private void addBillAction() throws IOException {
        Stage newbillStage = new Stage();
        newbillStage.setTitle("记一笔");

        newbillStage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/bgt/billsb/newbill.fxml"));

        newbillStage.setScene(new Scene(fxmlLoader.load()));
        newbillStage.initOwner(billsListView.getScene().getWindow());
        newbillStage.show();

        newbillStage.setOnHidden(event -> {
            System.out.println("新增账单窗口隐藏了.....");

        });
    }

    @Override
    public void loadData() {
        // 创建一个 ObservableList 并添加集合元素
        ObservableList<BillDay> observableBillList = FXCollections.observableArrayList(datas);
        billsListView.getItems().addAll(observableBillList);
        //设置每行都可选择
        billsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        billsListView.setCellFactory(d -> new BillCell(datas));
    }
}