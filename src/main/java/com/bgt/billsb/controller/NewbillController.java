package com.bgt.billsb.controller;

import com.bgt.billsb.vo.BillDay;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class NewbillController{

    @FXML
    private GridPane inputMoneyPane;

    //初始化方法，会在 FXML 加载时自动调用
    public void initialize() {

    }


    //测试数据
    private List<BillDay> datas = new ArrayList<>();


}