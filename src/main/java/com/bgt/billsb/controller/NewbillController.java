package com.bgt.billsb.controller;

import com.bgt.billsb.eenum.InOutEnum;
import com.bgt.billsb.entity.Bill;
import com.bgt.billsb.service.BillService;
import com.bgt.billsb.service.impl.BillServiceImpl;
import com.bgt.billsb.util.ControllerManager;
import com.bgt.billsb.util.DataUtil;
import com.bgt.billsb.vo.BillDetail;
import com.bgt.billsb.vo.BillTypeVo;
import com.bgt.billsb.vo.PayTypeVo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 新建账单 控制器
 */
public class NewbillController implements Initializable {
    private final BillService billService = new BillServiceImpl();
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    //账单类型和支付方式
    private List<PayTypeVo> payTypeList;
    private List<BillTypeVo> billTypeList;

    // 用于记录当前被选中的 VBox 账单类型
    private VBox selectedVBox;
    // 用于记录当前被选中的 VBox 支付方式
    private VBox selectedPayTypeVBox;
    // 用于记录当前被选中的收支类型按钮
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
    @FXML
    private Label payTypeLabel;

    private Bill newBill = new Bill();
    //初始化方法，会在 FXML 加载时自动调用
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerManager.setController("newbill",this);
        //设置账单时间
        hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        min.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        sec.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        //默认账单时间
        billTime.setValue(LocalDate.now());
        hour.getValueFactory().setValue(LocalDateTime.now().getHour());
        min.getValueFactory().setValue(LocalDateTime.now().getMinute());
        sec.getValueFactory().setValue(LocalDateTime.now().getSecond());


        //点击收入或支出按钮
        inBtn.setOnMouseClicked(this::handleInout);
        outBtn.setOnMouseClicked(this::handleInout);
        notBtn.setOnMouseClicked(this::handleInout);

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

    /**
     * 点击收入或支出按钮时修改样式
     * @param mouseEvent
     */
    private void handleInout(MouseEvent mouseEvent) {
        ToggleButton clickedInout = (ToggleButton) mouseEvent.getSource();
        // 取消之前选中的 VBox 的选中状态
        if (selectedInout != null) {
            selectedInout.setStyle("-fx-background-color: transparent;");
        }
        // 设置当前点击的 VBox 为选中状态
        clickedInout.setStyle("-fx-background-color: lightblue;");
        selectedInout = clickedInout;

        if (clickedInout.getText().equals("支出")){
            DataUtil.getBillTypes("支出",true);
        }else if (clickedInout.getText().equals("收入")){
            DataUtil.getBillTypes("收入",true);
        }else {
            DataUtil.getBillTypes("不计入",true);
        }

        loadPayType(FXCollections.observableArrayList(DataUtil.getPayTypes(false)));
    }

    /**
     * 处理支付方式的选中事件
     * @param mouseEvent
     */
    private void handleButtonClick(MouseEvent mouseEvent) {
        VBox clickedBox = (VBox) mouseEvent.getSource();
        if (selectedPayTypeVBox != null) {
            selectedPayTypeVBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
        }
        clickedBox.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-padding: 10;");
        selectedPayTypeVBox = clickedBox;
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



        //收支类型
        if (this.selectedInout == null) {
            //默认支出
            newBill.setInout(1);
        }else {
            newBill.setInout(this.selectedInout.getText().equals("收入") ? 2 : this.selectedInout.getText().equals("支出") ? 1 : 3);
        }
        //支出时必选支付方式
        if (newBill.getInout() == 1 && Objects.isNull(selectedPayTypeVBox)) {
            alert.setContentText("请选择支付方式!");
            alert.show();
            return;
        }else if (newBill.getInout() != 1 && Objects.isNull(selectedPayTypeVBox)) {
            //支出时 支付方式
                Optional<PayTypeVo> firstMatch = this.payTypeList.stream()
                        .filter(pt -> pt.getPayAccountName().equals(this.selectedPayTypeVBox.getChildren().get(0).toString()))
                        .findFirst();
                firstMatch.ifPresent(pt -> newBill.setPayTypeId(pt.getId()));

                firstMatch.ifPresentOrElse(
                        pt -> {
                            String payAccountName = (pt.getPayAccountName() != null) ? pt.getPayAccountName() : "";
                            // 使用 + 操作符优化字符串拼接
                            newBill.setDesc(payAccountName + " " + this.remark.getText());
                        },
                        () -> {
                            // 使用常量替代硬编码
                            newBill.setDesc("储蓄账户 " + this.remark.getText());
                        }
                );
        }else if (newBill.getInout() == 1 && Objects.nonNull(selectedPayTypeVBox)) {
            //支出时 支付方式
                Optional<PayTypeVo> firstMatch = this.payTypeList.stream()
                        .filter(pt -> pt.getPayAccountName().equals(this.selectedPayTypeVBox.getChildren().get(0).toString()))
                        .findFirst();
                firstMatch.ifPresent(pt -> newBill.setPayTypeId(pt.getId()));

                //把支付方式添加到备注中
                firstMatch.ifPresentOrElse(
                        pt -> {
                            // 空值检查，避免 NullPointerException
                            String payAccountName = (pt.getPayAccountName() != null) ? pt.getPayAccountName() : "";
                            // 使用 + 操作符优化字符串拼接
                            newBill.setDesc(payAccountName + " " + this.remark.getText());
                        },
                        () -> {
                            newBill.setDesc("储蓄账户 " + this.remark.getText());
                        }
                );
        }else {
            newBill.setDesc(this.remark.getText());
        }


        //金额
        Double my = Double.valueOf(this.money.getText());
        my = newBill.getInout() == 1 ? my * -1 : my;
        newBill.setMoney(my);
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

        //账单创建时间
        newBill.setBillTime(this.billTime.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+" "+DataUtil.addZero(this.hour.getValue().toString(),2)+":"+ DataUtil.addZero(this.min.getValue().toString(),2) + ":" + DataUtil.addZero(this.sec.getValue().toString(),2));
        newBill.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        if (Objects.nonNull(newBill.getId())){
            billService.updateBill(newBill);
        }else {
            billService.addBill(newBill);
        }


        //保存成功后关闭当前窗口
        Stage window = (Stage) billTime.getScene().getWindow();
        window.close();

    }

    /**
     * 渲染支付方式
     * @param value
     */
    public void loadPayType(ObservableList<PayTypeVo> value) {
        payTypeList = value;
        payTypePane.getChildren().clear();
        int col = 0;
        int row = 0;
        for (PayTypeVo payTypeVo : value) {
            VBox vBox = new VBox(10); // 增加垂直间距
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefWidth(80); // 减小宽度
            vBox.setPrefHeight(80); // 减小高度
            vBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
            vBox.setCursor(Cursor.HAND);
            
            Label label = new Label(payTypeVo.getPayAccountName());
            label.setStyle("-fx-text-fill: #3b7cac; -fx-font-size: 12;");
            label.setAlignment(Pos.CENTER);
            
            vBox.getChildren().add(label);
            
            vBox.setOnMouseClicked(event -> {
                if (selectedPayTypeVBox != null) {
                    selectedPayTypeVBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
                }
                vBox.setStyle("-fx-background-color:rgb(13, 84, 155); -fx-background-radius: 8;");
                selectedPayTypeVBox = vBox;
                newBill.setPayTypeId(payTypeVo.getId());
            });
            
            payTypePane.add(vBox, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * 渲染账单类型
     * @param value
     */
    public void loadBillType(ObservableList<BillTypeVo> value) {
        billTypeList = value;
        billTypePane.getChildren().clear();
        int col = 0;
        int row = 0;
        for (BillTypeVo billTypeVo : value) {
            VBox vBox = new VBox(10); // 增加垂直间距
            vBox.setAlignment(Pos.CENTER);
            vBox.setPrefWidth(80); // 减小宽度
            vBox.setPrefHeight(80); // 减小高度
            vBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
            vBox.setCursor(Cursor.HAND);
            
            ImageView imageView = new ImageView(new Image(getClass().getResource("/img/" + billTypeVo.getIcon() + ".png").toExternalForm()));
            imageView.setFitWidth(32); // 减小图标大小
            imageView.setFitHeight(32);
            
            Label label = new Label(billTypeVo.getBillType());
            label.setStyle("-fx-text-fill: #3b7cac; -fx-font-size: 12;");
            
            vBox.getChildren().addAll(imageView, label);
            
            vBox.setOnMouseClicked(event -> {
                if (selectedVBox != null) {
                    selectedVBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
                }
                vBox.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8;");
                selectedVBox = vBox;
                newBill.setBillTypeId(billTypeVo.getId());
            });
            
            billTypePane.add(vBox, col, row);
            col++;
            if (col == 6) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * 修改账单渲染数据
     * @param data
     */
    public void loadData(List<BillDetail> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        
        BillDetail billDetail = data.get(0);
        newBill.setId(billDetail.getId());
        money.setText(String.valueOf(billDetail.getMoney()).replace("-",""));
        remark.setText(billDetail.getDesc());
        billTime.setValue(LocalDate.parse(billDetail.getBillTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        hour.getValueFactory().setValue(LocalDateTime.parse(billDetail.getBillTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).getHour());
        min.getValueFactory().setValue(LocalDateTime.parse(billDetail.getBillTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).getMinute());
        sec.getValueFactory().setValue(LocalDateTime.parse(billDetail.getBillTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).getSecond());
        
        // 设置收支类型
        if (billDetail.getInout() == 1) {
            outBtn.setSelected(true);
            outBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
            selectedInout = outBtn;
            inBtn.setSelected(false);
            inBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
            notBtn.setSelected(false);
            notBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
        } else if (billDetail.getInout() == 2) {
            inBtn.setSelected(true);
            inBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
            selectedInout = inBtn;
            outBtn.setSelected(false);
            outBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
            notBtn.setSelected(false);
            notBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
        } else {
            notBtn.setSelected(true);
            notBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
            selectedInout = notBtn;
            outBtn.setSelected(false);
            outBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
            inBtn.setSelected(false);
            inBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 4;");
        }
        
        // 加载账单类型和支付方式
        if (billDetail.getBillType() != null) {
            billTypeList = DataUtil.getBillTypes(InOutEnum.getName(billDetail.getInout()), false);
            loadBillType(FXCollections.observableArrayList(billTypeList));
            
            // 设置选中的账单类型
            for (Node child : billTypePane.getChildren()) {
                VBox vBox = (VBox) child;
                Label label = (Label) vBox.getChildren().get(1);
                if (label.getText().equals(billDetail.getBillType())) {
                    vBox.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-padding: 10;");
                    selectedVBox = vBox;
                } else {
                    vBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
                }
            }
        }
        
        if (billDetail.getPayAccountName() != null) {
            payTypeList = DataUtil.getPayTypes(false);
            loadPayType(FXCollections.observableArrayList(payTypeList));
            
            // 设置选中的支付方式
            for (Node child : payTypePane.getChildren()) {
                VBox vBox = (VBox) child;
                Label label = (Label) vBox.getChildren().get(0);
                if (label.getText().equals(billDetail.getPayAccountName())) {
                    vBox.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-padding: 10;");
                    selectedPayTypeVBox = vBox;
                } else {
                    vBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
                }
            }
        }
    }

    private void createBillTypeButton(BillTypeVo billType, int col, int row) {
        VBox buttonBox = new VBox(10); // 增加垂直间距
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPrefWidth(80); // 减小宽度
        buttonBox.setPrefHeight(80); // 减小高度
        buttonBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
        buttonBox.setCursor(Cursor.HAND);

        ImageView imageView = new ImageView(new Image(getClass().getResource("/img/" + billType.getIcon() + ".png").toExternalForm()));
        imageView.setFitWidth(32);
        imageView.setFitHeight(32);

        Label textLabel = new Label(billType.getBillType());
        textLabel.setStyle("-fx-text-fill: #3b7cac; -fx-font-size: 12;");
        textLabel.setAlignment(Pos.CENTER);

        buttonBox.getChildren().addAll(imageView, textLabel);

        buttonBox.setOnMouseClicked(event -> {
            if (selectedVBox != null) {
                selectedVBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
            }
            buttonBox.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-padding: 10;");
            selectedVBox = buttonBox;
            newBill.setBillTypeId(billType.getId());
        });

        billTypePane.add(buttonBox, col, row);
    }

    private void createPayTypeButton(PayTypeVo payType, int col, int row) {
        VBox buttonBox = new VBox(10); // 增加垂直间距
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPrefWidth(80); // 减小宽度
        buttonBox.setPrefHeight(80); // 减小高度
        buttonBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
        buttonBox.setCursor(Cursor.HAND);

        Label textLabel = new Label(payType.getPayAccountName());
        textLabel.setStyle("-fx-text-fill: #3b7cac; -fx-font-size: 12;");
        textLabel.setAlignment(Pos.CENTER);

        buttonBox.getChildren().add(textLabel);

        buttonBox.setOnMouseClicked(event -> {
            if (selectedPayTypeVBox != null) {
                selectedPayTypeVBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
            }
            buttonBox.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-padding: 10;");
            selectedPayTypeVBox = buttonBox;
            newBill.setPayTypeId(payType.getId());
        });

        payTypePane.add(buttonBox, col, row);
    }

    @FXML
    private void handlePayTypeClick(javafx.scene.input.MouseEvent event) {
        VBox clickedBox = (VBox) event.getSource();
        if (selectedPayTypeVBox != null) {
            selectedPayTypeVBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 10;");
        }
        clickedBox.setStyle("-fx-background-color: #e9ecef; -fx-background-radius: 8; -fx-padding: 10;");
        selectedPayTypeVBox = clickedBox;
    }
}