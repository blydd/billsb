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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * 账单 控制器
 */
public class BillController {

    @FXML
    private ListView billsListView;



    /**
     * 初始化方法
     */
    public void initialize() throws IOException {
        //注册事件总线
        EventBusUtil.getDefaut().register(this);

        ControllerManager.setController("bill",this);
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
    public void loadData(ObservableList<BillDay> datas) {
        System.out.println("BillController.loadData........................");
        // 设置每行都可选择
        billsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        billsListView.getItems().clear();
        billsListView.setItems(datas);

        billsListView.setCellFactory(param -> new ListCell<BillDay>() {
            private final VBox mainRoot = new VBox();

            @Override
            protected void updateItem(BillDay billDay, boolean empty) {
                FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/com/bgt/billsb/billListView.fxml"));
                BorderPane dayView = null;
                try {
                    dayView = loader1.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ListView<BillDetail> billsDetail = (ListView<BillDetail>) dayView.lookup("#billsDetail");
                super.updateItem(billDay, empty);
                if (billDay == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        //账单日期、日总支出和日总收入
                        ((Label) dayView.lookup("#date")).setText(billDay.getDate());
                        ((Label) dayView.lookup("#dayTotalOut")).setText(String.valueOf(billDay.getDayTotalOut()));
                        ((Label) dayView.lookup("#dayTotalIn")).setText(String.valueOf(billDay.getDayTotalIn()));

                        // 更新每日账单列表
                        ObservableList<BillDetail> observableBillList = FXCollections.observableArrayList(billDay.getBillDetailList());
                        if (observableBillList != null && !observableBillList.isEmpty()) {
                            billsDetail.setItems(observableBillList);
                            billsDetail.setCellFactory(d -> new BillDetailCell(observableBillList));
                        } else {
                            billsDetail.setItems(FXCollections.emptyObservableList());
                        }


                        mainRoot.getChildren().addAll(dayView,billsDetail);
                        setGraphic(mainRoot);
                    } catch (Exception e) {
                        throw new RuntimeException("Error updating list item", e);
                    }
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
                this.loadData((ObservableList<BillDay>)event.getData());
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