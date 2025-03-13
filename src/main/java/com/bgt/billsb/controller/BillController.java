package com.bgt.billsb.controller;

import com.bgt.billsb.cell.BillDetailCell;
import com.bgt.billsb.dto.ResultEvent;
import com.bgt.billsb.util.ControllerManager;
import com.bgt.billsb.util.DataUtil;
import com.bgt.billsb.util.EventBusUtil;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import com.bgt.billsb.vo.BillTypeVo;
import com.bgt.billsb.vo.PayTypeVo;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * 账单 控制器
 */
public class BillController {

    @FXML
    private ListView billsListView;

    private ObservableList<BillDay> datas;


    /**
     * 初始化方法
     */
    public void initialize() throws IOException {
        //注册事件总线
        EventBusUtil.getDefaut().register(this);

        ControllerManager.setController("bill",this);

        DataUtil.queryData();
        loadData();

//        // 监听列表项的变化
        datas.addListener((ListChangeListener.Change<? extends BillDay> change) -> {
            // 根据列表项数量动态设置ListView的高度
            billsListView.setMaxHeight(datas.size() * 25);
        });

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

        //查询账单类型和支付方式 渲染到当前页面
        DataUtil.getBillTypes();
        DataUtil.getPayTypes();

        newbillStage.setOnHidden(event -> {
            System.out.println("新增账单窗口关闭了.....");
            //重新查询账单
            DataUtil.queryData();

        });
    }

    /**
     * 渲染账单数据
     * @param datas
     */
//    public void loadData(ObservableList<BillDay> datas) {
    public void loadData() {
        System.out.println("BillController.loadData........................");
        // 设置每行都可选择
        billsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        billsListView.getItems().clear();
        billsListView.setItems(datas);

        if (datas == null || datas.size() == 0){
            return;
        }

        billsListView.setCellFactory(param -> new ListCell<BillDay>() {
            @Override
            protected void updateItem(BillDay billDay, boolean empty) {
                super.updateItem(billDay, empty);
                if (billDay == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    BorderPane borderPane = new BorderPane();
                    //日合计
                    HBox dayTotal = new HBox();
                    dayTotal.getChildren().add(new Label(billDay.getDate()));
                    dayTotal.getChildren().add(new Label("                     "));
                    dayTotal.getChildren().add(new Label("出："+String.valueOf(billDay.getDayTotalOut())));
                    dayTotal.getChildren().add(new Label("                     "));
                    dayTotal.getChildren().add(new Label("入："+String.valueOf(billDay.getDayTotalIn())));

                    borderPane.setTop(dayTotal);

                    ListView<BorderPane> billList = new ListView<>();
                    //账单列表
                    ObservableList<BillDetail> observableBillList = FXCollections.observableArrayList(billDay.getBillDetailList());
                    for (BillDetail billDetail : observableBillList) {
                        BorderPane billView = new BorderPane();
                        //图标
                        javafx.scene.image.ImageView iconView = new ImageView(new Image(getClass().getResource("/img/" + billDetail.getIcon() + ".png").toExternalForm()));
                        iconView.setFitWidth(60);
                        iconView.setFitHeight(60);
                        billView.setLeft(iconView);
                        //账单类型 时间
                        VBox billType = new VBox();
                        billType.getChildren().add(new Label(billDetail.getBillType()));
                        billType.getChildren().add(new Label(billDetail.getBillTime()));
                        billView.setCenter(billType);
                        //金额 按钮
                        HBox money = new HBox();
                        Double my = billDetail.getMoney();
                        Label billMoney = new Label(String.valueOf(billDetail.getMoney()));
                        money.getChildren().add(billMoney);
                        money.getChildren().add(new Label("     "));
                        Label btn = new Label("删除");
                        btn.setOnMouseClicked(event -> {
                            System.out.println("删除账单："+billDetail.getId());
                            DataUtil.deleteBill(billDetail.getId());
                            DataUtil.queryData();
                        });
                        money.getChildren().add(btn);
                        billView.setRight(money);

                        billList.getItems().add(billView);
                    }
                    borderPane.setCenter(billList);
                    setGraphic(borderPane);

                }
            }
        });
    }



    /**
     * 监听数据更新
     * @param event
     */
    @Subscribe
    private void handleResultEvent(ResultEvent event) {
        System.out.println("BillController.handleResultEvent:监听到数据更新."+event.getDataType());
        switch (event.getDataType()){
            case BILLDATA:
                //渲染账单
//                this.loadData((ObservableList<BillDay>)event.getData());
               this.datas= (ObservableList<BillDay>)event.getData();
               this.loadData();
                break;
            case PAYTYPE:
                //渲染支付方式
                NewbillController newbill = (NewbillController) ControllerManager.getControoler("newbill");
                newbill.loadPayType((FXCollections.observableArrayList((List<PayTypeVo>)event.getData())));
                break;
            case BILLTYPE:
                //渲染账单类型
                NewbillController newbill2 = (NewbillController) ControllerManager.getControoler("newbill");
                newbill2.loadBillType((FXCollections.observableArrayList((List<BillTypeVo>)event.getData())));
                break;
            default:
                break;
        }
    }
}