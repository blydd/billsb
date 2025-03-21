package com.bgt.billsb.controller;

import cn.hutool.core.util.StrUtil;
import com.bgt.billsb.dto.BillDto;
import com.bgt.billsb.dto.ResultEvent;
import com.bgt.billsb.eenum.InOutEnum;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.sound.sampled.Line;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 账单 控制器
 */
public class BillController {
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    private ListView billsListView;
    //月份日期控件
    @FXML
    private DatePicker billMonth;
    @FXML
    private BorderPane borderPane;
    //总支出
    @FXML
    private Label totalOut;
    //总入账
    @FXML
    private Label totalIn;

    //账单类型:衣食住行
    @FXML
    private ChoiceBox billTypeView;
    //支付方式:微信 支付宝
    @FXML
    private ChoiceBox payTypeView;

    private ObservableList<BillDay> datas;
    //账单类型和支付方式
    private ObservableList<PayTypeVo> payTypeList;
    private ObservableList<BillTypeVo> billTypeList;


    //账单月份
    private String billlMonthStr;
    /**
     * 初始化方法
     */
    public void initialize() throws IOException {
        //注册事件总线
        EventBusUtil.getDefaut().register(this);

        ControllerManager.setController("bill",this);

        //上面月份默认显示当月
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return DataUtil.DTF_MONTH.format(date);
                }
                return "";
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, DataUtil.DTF_MONTH);
                }
                return null;
            }
        };
        //billMonth展示为yyyyy-MM格式
        billMonth.setConverter(converter);
        billMonth.setPromptText(DataUtil.getCurrentMonth());
        billlMonthStr = billMonth.getPromptText();
        // 只允许选择每个月的第一天
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.getDayOfMonth() != 1) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                };
            }
        };
        // 设置日期单元格工厂
        billMonth.setDayCellFactory(dayCellFactory);
        //监听billMonth的选择值
        billMonth.valueProperty().addListener((observable, oldValue, newValue) -> {
            BillDto param = new BillDto();
            param.setBillMonth(newValue.toString().substring(0,newValue.toString().lastIndexOf("-")));
            DataUtil.queryData(param);

            billlMonthStr = param.getBillMonth();
        });

        //监听账单类型值变化 payTypeView
        billTypeView.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                return;
            }
            BillDto param = new BillDto();
            param.setBillMonth(billlMonthStr);
            param.setBillType(newValue.toString());
            param.setPayType(Objects.isNull(payTypeView.getValue()) ? "" : payTypeView.getValue().toString());
            DataUtil.queryData(param);
        });

        //监听支付方式值变化
        payTypeView.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                return;
            }
            BillDto param = new BillDto();
            param.setBillMonth(billlMonthStr);
            param.setPayType(newValue.toString());
            param.setBillType(Objects.isNull(billTypeView.getValue()) ? "" : billTypeView.getValue().toString());
            DataUtil.queryData(param);
        });
        //查询账单数据
        DataUtil.queryData(null);
        //查询支付方式
        DataUtil.getPayTypes(true);
        //查询账单类型
        DataUtil.getBillTypes("",true);

    }


    /**
     * 点击"记一笔"按钮
     */
    @FXML
    private void addBillAction() throws IOException {
        billsListView = new ListView();
        borderPane.setCenter(billsListView);

        Stage newbillStage = new Stage();
        newbillStage.setTitle("记一笔");

        newbillStage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/bgt/billsb/newbill.fxml"));

        newbillStage.setScene(new Scene(fxmlLoader.load()));
        newbillStage.initOwner(billsListView.getScene().getWindow());
        newbillStage.show();

        //查询账单类型和支付方式 渲染到当前页面
        DataUtil.getBillTypes("支出",true);
        DataUtil.getPayTypes(true);

        newbillStage.setOnHidden(event -> {
            System.out.println("新增账单窗口关闭了.....");
            //重新查询账单
            DataUtil.queryData(null);

        });
    }

    /**
     * 点击编辑按钮
     * @throws IOException
     */
    private void updateBillAction(BillDetail billDetail) throws IOException {
        billsListView = new ListView();
        borderPane.setCenter(billsListView);

        Stage newbillStage = new Stage();
        newbillStage.setTitle("修改账单");

        newbillStage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/bgt/billsb/newbill.fxml"));

        newbillStage.setScene(new Scene(fxmlLoader.load()));
        newbillStage.initOwner(billsListView.getScene().getWindow());
        newbillStage.show();

        //页面渲染后查询数据,把数据渲染到页面
        BillDto param = new BillDto();
        param.setId(billDetail.getId());
        DataUtil.queryData(param);


        newbillStage.setOnHidden(event -> {
            System.out.println("编辑账单窗口关闭了.....");
            //重新查询账单
            DataUtil.queryData(null);

        });
    }

    /**
     * 点击"重置"按钮 清除账单类型和支付方式查询条件
     */
    @FXML
    private void resetQueryAction() throws IOException {
        payTypeView.setValue("");
        billTypeView.setValue("");
        BillDto param = new BillDto();
        param.setBillMonth(billlMonthStr);
        DataUtil.queryData(param);
    }

    /**
     * 渲染账单数据
     */
    public void loadData() {
        System.out.println("BillController.loadData........................");
        if (datas == null || datas.size() == 0){
            borderPane.setCenter(new Label("暂无数据!"));
            return;
        }else {
            billsListView = new ListView();
            borderPane.setCenter(billsListView);
        }
        // 设置每行都可选择
        billsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        billsListView.getItems().clear();
        billsListView.setItems(datas);

        //展示总支出 总入账
        totalIn.setText(datas.stream().mapToDouble(BillDay::getDayTotalIn).sum() + "");
        totalOut.setText(datas.stream().mapToDouble(BillDay::getDayTotalOut).sum() + "");

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

                    Label billDate = new Label(billDay.getDate());
                    //设置粗体及字号及颜色
                    billDate.setTextFill(javafx.scene.paint.Color.RED);
                    billDate.setStyle("-fx-font-weight: bold;-fx-font-size: 16px;");
                    dayTotal.getChildren().add(billDate);
                    dayTotal.getChildren().add(new Label("                     "));
                    Label outDayTotal = new Label("出：" + String.valueOf(billDay.getDayTotalOut()));
                    //设置粗体及字号及颜色
                    outDayTotal.setStyle("-fx-font-weight: bold;-fx-font-size: 16px;");
                    outDayTotal.setTextFill(javafx.scene.paint.Color.RED);
                    dayTotal.getChildren().add(outDayTotal);
                    dayTotal.getChildren().add(new Label("                     "));
                    Label inDayTotal = new Label("入：" + String.valueOf(billDay.getDayTotalIn()));
                    //设置粗体及字号及颜色
                    inDayTotal.setStyle("-fx-font-weight: bold;-fx-font-size: 16px;");
                    inDayTotal.setTextFill(javafx.scene.paint.Color.RED);
                    dayTotal.getChildren().add(inDayTotal);

                    borderPane.setTop(dayTotal);

                    ListView<BorderPane> billList = new ListView<>();
                    //账单列表
                    ObservableList<BillDetail> observableBillList = FXCollections.observableArrayList(billDay.getBillDetailList());
                    for (BillDetail billDetail : observableBillList) {
                        //每一行账单
                        BorderPane billView = new BorderPane();
                        //图标
                        javafx.scene.image.ImageView iconView = new ImageView(new Image(getClass().getResource("/img/" + billDetail.getIcon() + ".png").toExternalForm()));
                        iconView.setFitWidth(50);
                        iconView.setFitHeight(50);
                        billView.setLeft(iconView);
                        //账单类型 时间 备注(含支付类型)
                        VBox billType = new VBox();
                        billType.getChildren().add(new Label(billDetail.getBillType().concat("   ").concat(billDetail.getBillTime())));
                        billType.getChildren().add(new Label(billDetail.getDesc()));
                        billView.setCenter(billType);
                        //金额 按钮
                        HBox money = new HBox();
                        Double my = billDetail.getMoney();
                        Label billMoney = new Label(String.valueOf(billDetail.getMoney()));
                        money.getChildren().add(billMoney);
                        money.getChildren().add(new Label("     "));
                        Label btn = new Label("编辑");
                        //编辑按钮添加按钮样式
                        btn.setStyle("-fx-background-color: #FF0000;-fx-text-fill: #FFFFFF;-fx-font-size: 12px;-fx-padding: 5px 10px;");
                        btn.setOnMouseClicked(event -> {
                            try {
                                updateBillAction(billDetail);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        money.getChildren().add(btn);
                        Label btn2 = new Label("删除");
                        //删除按钮添加按钮样式
                        btn2.setStyle("-fx-background-color: #FF0000;-fx-text-fill: #FFFFFF;-fx-font-size: 12px;-fx-padding: 5px 10px;");
                        btn2.setOnMouseClicked(event -> {
                            System.out.println("删除账单："+billDetail.getId());
                            DataUtil.deleteBill(billDetail.getId());
                            DataUtil.queryData(null);
                        });
                        money.getChildren().add(btn2);
                        billView.setRight(money);

                        billList.getItems().add(billView);
                    }
                    //设置每天账单区域高度
                    billList.setMaxHeight(billList.getItems().size()*57.5);
                    billList.setMinHeight(billList.getItems().size()*57.5);
                    borderPane.setCenter(billList);


                    setGraphic(borderPane);

                }
            }
        });
    }


    /**
     * 渲染支付方式
     * @param value
     */
    public void loadPayType(ObservableList<PayTypeVo> value) {
        this.payTypeList = value;
        payTypeView.getItems().clear();
        for (PayTypeVo billTypeVo : value) {
            payTypeView.getItems().add(billTypeVo.getPayAccountName());
        }
    }

    /**
     * 渲染账单类型
     * @param value
     */
    public void loadBillType(ObservableList<BillTypeVo> value) {
        this.billTypeList = value;
        billTypeView.getItems().clear();
        for (BillTypeVo billTypeVo : value) {
            billTypeView.getItems().add(billTypeVo.getBillType());
        }
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
                if (Objects.nonNull(newbill)){
                    //账单页查询支付方式后,新增账单还没加载,此时其初始化方法未执行,故可能为null
                    newbill.loadPayType((FXCollections.observableArrayList((List<PayTypeVo>)event.getData())));
                }


                this.loadPayType(FXCollections.observableArrayList((List<PayTypeVo>)event.getData()));
                break;
            case BILLTYPE:
                //渲染账单类型
                NewbillController newbill2 = (NewbillController) ControllerManager.getControoler("newbill");
                if (Objects.nonNull(newbill2)){
                    //账单页查询支付方式后,新增账单还没加载,此时其初始化方法未执行,故可能为null
                    newbill2.loadBillType((FXCollections.observableArrayList((List<BillTypeVo>)event.getData())));
                }

                this.loadBillType(FXCollections.observableArrayList((List<BillTypeVo>)event.getData()));
                break;
            case ONEBILL:
                //单条账单,说明是编辑或详情
                NewbillController editbill = (NewbillController) ControllerManager.getControoler("newbill");
                if (Objects.nonNull(editbill)){
                    //账单页查询支付方式后,新增账单还没加载,此时其初始化方法未执行,故可能为null
                    editbill.loadData((List<BillDetail>)event.getData());
                }
                break;
            default:
                break;
        }
    }
}