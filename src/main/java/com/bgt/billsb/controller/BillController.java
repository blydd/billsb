package com.bgt.billsb.controller;

import com.bgt.billsb.cell.BillCell;
import com.bgt.billsb.util.TestUtil;
import com.bgt.billsb.vo.BillDay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BillController implements TabController{

    @FXML
    private ListView billsListView;
    //测试数据
    private List<BillDay> datas = new ArrayList<>();
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
        newbillStage.show();
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