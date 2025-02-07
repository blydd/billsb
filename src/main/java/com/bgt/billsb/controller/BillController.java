package com.bgt.billsb.controller;

import com.bgt.billsb.cell.BillCell;
import com.bgt.billsb.util.TestUtil;
import com.bgt.billsb.vo.BillDay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.List;

public class BillController implements TabController{

//    private UserDao userDao = new UserDao();
    @FXML
    private ListView billsListView;
    //测试数据
    private List<BillDay> datas = new ArrayList<>();
    @FXML
    private void addBillAction() {

    }

    @Override
    public void loadData() {
        datas = TestUtil.getDatas();
        // 创建一个 ObservableList 并添加集合元素
        ObservableList<BillDay> observableBillList = FXCollections.observableArrayList(datas);
        billsListView.getItems().addAll(observableBillList);
        //设置每行都可选择
        billsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        billsListView.setCellFactory(d -> new BillCell(datas));
    }
}