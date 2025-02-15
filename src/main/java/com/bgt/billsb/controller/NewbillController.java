package com.bgt.billsb.controller;

import com.bgt.billsb.entity.Bill;
import com.bgt.billsb.service.BillService;
import com.bgt.billsb.service.impl.BillServiceImpl;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillTypeVo;
import com.bgt.billsb.vo.PayTypeVo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class NewbillController{
    private final BillService billService = new BillServiceImpl();
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    //账单类型和支付方式
    private List<PayTypeVo> payTypeList;
    private List<BillTypeVo> billTypeList;

    // 用于记录当前被选中的 VBox 账单类型
    private VBox selectedVBox;
    // 用于记录当前被选中的 button 支付方式
    private Button selectedButton;
    private ToggleButton selectedInout;
    //分类:支出
    @FXML
    private ToggleButton outBtn;
    //分类:收入
    @FXML
    private ToggleButton inBtn;
    //分类:不计入收支
    @FXML
    private ToggleButton notBtn;
    //账单时间
    @FXML
    private DatePicker billTime;
    @FXML
    private Spinner hour;
    @FXML
    private Spinner min;
    @FXML
    private Spinner sec;
    //账单金额
    @FXML
    private TextField money;
    //账单类型pane
    @FXML
    private GridPane billTypePane;
    //支付方式pane
    @FXML
    private GridPane payTypePane;
    //备注
    @FXML
    private TextArea remark;
    //保存按钮
    @FXML
    private Button saveBtn;
    //初始化方法，会在 FXML 加载时自动调用
    public void initialize() {
        //查询最新账单类别及支付类别
        loadDataAsync();

        hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        min.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        sec.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));


        inBtn.setOnMouseClicked(this::handleInout);

        //监听金额只能输入数字和小数点
        this.money.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //判断newValue只能是数字或小数点
                if (!newValue.matches("\\d*|\\d+\\.\\d*")) {
                    money.setText(oldValue);
                }
            }
        });
    }

    private void handleInout(MouseEvent mouseEvent) {
        ToggleButton clickedInout = (ToggleButton) mouseEvent.getSource();
        // 取消之前选中的 VBox 的选中状态
        if (selectedInout != null) {
            selectedInout.setStyle("-fx-background-color: transparent;");
        }
        // 设置当前点击的 VBox 为选中状态
        clickedInout.setStyle("-fx-background-color: lightblue;");
        selectedInout = clickedInout;
    }


    //测试数据
    private List<BillDay> datas = new ArrayList<>();

    /**
     * 异步加载数据
     */
    private void loadDataAsync() {
        //加载账单类型
        Service<List<BillTypeVo>> dataService = new Service<>() {
            @Override
            protected Task<List<BillTypeVo>> createTask() {
                return new Task<>() {
                    @Override
                    protected List<BillTypeVo> call() {
                        return billService.getBillTypes();
                    }
                };
            }
        };
        dataService.setOnSucceeded(e -> {
                //查询完毕后把数据转成页面所需格式
                List<BillTypeVo> value = dataService.getValue();
                this.billTypeList = value;
                //塞入账单类型pane
                billTypePane.getChildren().clear();
                int colIndex = 0 ;
                int rowIndex = 0 ;
            for (BillTypeVo billTypeVo : value) {
                VBox vBox = new VBox();
                System.out.println("billTypeVo.getIcon():"+billTypeVo.getIcon());
                ImageView iconView = new ImageView(new Image(getClass().getResource("/img/" + billTypeVo.getIcon() + ".png").toExternalForm()));
                iconView.setFitWidth(60);
                iconView.setFitHeight(60);
                vBox.getChildren().add(iconView);

                Label label = new Label(billTypeVo.getBillType());
                label.setFont(Font.font(17));
                label.setPrefHeight(17);
                label.setMinWidth(15);
                vBox.getChildren().add(label);

                vBox.setAlignment(Pos.CENTER);

                //给每个vbox添加点击事件
                vBox.setOnMouseClicked(this::handleVboxClick);

                billTypePane.add(vBox,colIndex,rowIndex);

                colIndex++;
                if (colIndex == 6) {
                    colIndex = 0;
                    rowIndex++;
                }
            }

                System.out.println("查询到数据的账单数量 = " + value.size());
            }
        );
        dataService.start();


        //加载支付类型
        Service<List<PayTypeVo>> dataService2 = new Service<>() {
            @Override
            protected Task<List<PayTypeVo>> createTask() {
                return new Task<>() {
                    @Override
                    protected List<PayTypeVo> call() {
                        return billService.getPayTypes();
                    }
                };
            }
        };
        dataService2.setOnSucceeded(e -> {
                //查询完毕后把数据转成页面所需格式
                List<PayTypeVo> value = dataService2.getValue();
                this.payTypeList = value;
                //塞入账单类型pane
                payTypePane.getChildren().clear();
                int colIndex = 0 ;
                int rowIndex = 0 ;
            for (PayTypeVo billTypeVo : value) {
                Button button = new Button(billTypeVo.getPayAccountName());
                button.setPrefWidth(100);
                button.setMaxWidth(Double.MAX_VALUE);
                button.setStyle("-fx-wrap-text: true");
                //给每个button添加点击事件
                button.setOnMouseClicked(this::handleButtonClick);

                payTypePane.add(button,colIndex,rowIndex);

                colIndex++;
                if (colIndex == 3) {
                    colIndex = 0;
                    rowIndex++;
                }
            }

                System.out.println("查询到数据的账单数量 = " + value.size());
            }
        );
        dataService2.start();
    }

    /**
     * 处理支付方式的选中事件
     * @param mouseEvent
     */
    private void handleButtonClick(MouseEvent mouseEvent) {
        Button clickedButton = (Button) mouseEvent.getSource();
        // 取消之前选中的 VBox 的选中状态
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: transparent;");
        }
        // 设置当前点击的 VBox 为选中状态
        clickedButton.setStyle("-fx-background-color: lightblue;");
        selectedButton = clickedButton;
    }

    /**
     * 处理账单类型的选中事件
     * @param mouseEvent
     */
    private void handleVboxClick(MouseEvent mouseEvent) {
        VBox clickedVBox = (VBox) mouseEvent.getSource();
        // 取消之前选中的 VBox 的选中状态
        if (selectedVBox != null) {
            selectedVBox.setStyle("-fx-background-color: transparent;");
        }
        // 设置当前点击的 VBox 为选中状态
        clickedVBox.setStyle("-fx-background-color: lightblue;");
        selectedVBox = clickedVBox;
    }

    /**
     * 点击保存按钮
     */
    @FXML
    private void saveAction(){
        //非空校验 金额
        String money = this.money.getText();
        if (money == null || money.isBlank()) {
            alert.setContentText("请输入金额!");
            alert.show();
            return;
        }

        if (Objects.isNull(selectedVBox)){
            alert.setContentText("请选择账单类型!");
            alert.show();
            return;
        }

        if (Objects.isNull(selectedButton)){
            alert.setContentText("请选择支付方式!");
            alert.show();
            return;
        }

        Bill newBill = new Bill();
        //金额
        newBill.setMoney(Double.valueOf(this.money.getText()));
        //支付方式
        if (this.selectedButton != null && this.selectedButton.getText() != null && this.payTypeList != null && !this.payTypeList.isEmpty()) {
            Optional<PayTypeVo> firstMatch = this.payTypeList.stream()
                    .filter(pt -> pt.getPayAccountName().equals(this.selectedButton.getText()))
                    .findFirst();
            firstMatch.ifPresent(pt -> newBill.setPayTypeId(pt.getId()));
        }
        //账单类型
        if (this.selectedVBox != null && this.selectedVBox.getChildren() != null && !this.selectedVBox.getChildren().isEmpty()) {
            Optional<BillTypeVo> firstMatch = this.billTypeList.stream()
                    .filter(bt -> bt.getBillType().equals(((Label) this.selectedVBox.getChildren().get(1)).getText()))
                    .findFirst();
            firstMatch.ifPresent(bt -> newBill.setBillTypeId(bt.getId()));
        }
        //账单日期
        LocalDate billTimeValue = this.billTime.getValue();
        if (Objects.isNull(billTimeValue)){
            alert.setContentText("请选择账单时间!");
            alert.show();
            return;
        }
        //账单时间
        if (Objects.isNull(this.hour.getValue()) || Integer.valueOf(this.hour.getValue().toString())==0){
            //获取系统时间
            this.hour.getValueFactory().setValue(LocalDateTime.now().getHour());
            this.min.getValueFactory().setValue(LocalDateTime.now().getMinute());
            this.sec.getValueFactory().setValue(LocalDateTime.now().getSecond());
        }
        //收支类型
        if (this.selectedInout == null) {
            //默认支出
            newBill.setInout(1);
        }else {
            newBill.setInout(this.selectedInout.getText().equals("入账") ? 2 : this.selectedInout.getText().equals("支出") ? 1 : 3);
        }
        //账单创建时间
        newBill.setBillTime(this.billTime.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" "+this.hour.getValue().toString()+":"+ this.min.getValue().toString() + ":" + this.sec.getValue().toString());
        newBill.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        newBill.setDesc(this.remark.getText());
        billService.addBill(newBill);

        //保存成功后关闭当前窗口
        Stage window = (Stage) billTime.getScene().getWindow();
        window.close();

    }
}