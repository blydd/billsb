package com.bgt.billsb.controller;

import com.bgt.billsb.dto.BillDto;
import com.bgt.billsb.util.DataUtil;
import com.bgt.billsb.util.EventBusUtil;
import com.bgt.billsb.util.MonthPicker;
import com.bgt.billsb.vo.BillDay;
import com.bgt.billsb.vo.BillDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class StatisticsController {
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    private PieChart pieChartOut;
    @FXML
    private PieChart pieChartIn;
    //月份日期控件
    @FXML
    private MonthPicker billMonth;
    @FXML
    private Label totalOutLabel;
    @FXML
    private Label totalInLabel;

    @FXML
    private Button preMonth;
    @FXML
    private Button nextMonth;

    //账单月份
    private String billlMonthStr;
    // 创建一个 Label 用于显示数值
    private Label caption = new Label("");


    //查询的账单数据
    private ObservableList<BillDay> datas = FXCollections.observableArrayList();

    /**
     * 初始化方法
     */
    public void initialize() {
        //注册事件总线
        EventBusUtil.getDefaut().register(this);

        //初始化时默认查询当月数据
        datas = DataUtil.queryData(null);
        loadData();

        //处理日期组件
        handleDatePicker();

        preMonth.setOnAction(event -> {
            billMonth.previousMonth();
        });
        nextMonth.setOnAction(event -> {
            billMonth.nextMonth();
        });
    }


    /**
     * 加载统计图数据
     */
    public void loadData() {
        System.out.println("StatisticsController.loadData..........加载图表数据");
        /**组装支出数据,按账单类别统计*/
        Map<String, Double> groupByTypeOut = datas.stream()
                .flatMap(b -> b.getBillDetailList().stream().filter(b1 -> Objects.equals(1, b1.getInout())))
                .collect(Collectors.toList())
                .stream().collect(Collectors.groupingBy(BillDetail::getBillType, Collectors.summingDouble(a->Math.abs(a.getMoney()))));
        //图表数据:key-类别,value-金额
        ObservableList<PieChart.Data> pieDataOut = FXCollections.observableArrayList();
        groupByTypeOut.forEach((k, v) -> {
            pieDataOut.add(new PieChart.Data(k, v));
        });
        pieChartOut.setData(pieDataOut);
        //设置图例在左侧
        pieChartOut.setLegendSide(Side.LEFT);
        //展示总支出
        totalOutLabel.setText(String.valueOf(groupByTypeOut.values().stream().mapToDouble(Double::doubleValue).sum()));


        /**组装入账数据,按账单类别统计*/
        Map<String, Double> groupByTypeIn = datas.stream().flatMap(b -> b.getBillDetailList().stream().filter(b1 -> Objects.equals(2, b1.getInout()))).collect(Collectors.toList())
                .stream().collect(Collectors.groupingBy(BillDetail::getBillType, Collectors.summingDouble(a->Math.abs(a.getMoney()))));
        //图表数据:key-类别,value-金额
        ObservableList<PieChart.Data> pieDataIn = FXCollections.observableArrayList();
        groupByTypeIn.forEach((k, v) -> {
            pieDataIn.add(new PieChart.Data(k, v));
        });
        pieChartIn.setData(pieDataIn);
        //设置图例在左侧
        pieChartIn.setLegendSide(Side.LEFT);
        //展示总入账
        totalInLabel.setText(String.valueOf(groupByTypeIn.values().stream().mapToDouble(Double::doubleValue).sum()));

        //悬浮展示具体数值
        handleMouseEnter(pieChartOut);
        handleMouseEnter(pieChartIn);
    }

    /**
     * 鼠标悬浮展示饼图数据
     * @param pieChart
     */
    private void handleMouseEnter(PieChart pieChart) {
        for (PieChart.Data data : pieChart.getData()) {
            data.getNode().setOnMouseClicked(event -> {
                alert.setTitle(data.getName());
                alert.setContentText(String.valueOf(data.getPieValue()));
                alert.setAlertType(Alert.AlertType.NONE);
                // 添加关闭按钮
                ButtonType closeButton = new ButtonType("关闭");
                alert.getButtonTypes().setAll(closeButton);
                // 显示 Alert 对话框并等待用户操作
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == closeButton) {
                    // 当用户点击关闭按钮时，关闭对话框
                    alert.close();
                }
            });
            //悬浮放大
            data.getNode().setOnMouseEntered(event -> {
                Node source = (Node) event.getSource();
                source.setScaleX(1.3);
                source.setScaleY(1.3);
            });
            //移出时缩小
            data.getNode().setOnMouseExited(event -> {
                Node source = (Node) event.getSource();
                source.setScaleX(1.0);
                source.setScaleY(1.0);
                alert.hide();
            });
        }
    }


    /**
     * 处理月份变化
     */
    private void handleDatePicker() {
        // 监听选择事件
        billMonth.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Selected: " + billMonth.getSelectedYearMonth());
            BillDto param = new BillDto();
            param.setBillMonth(billMonth.getSelectedYearMonth().toString());
            datas = DataUtil.queryData(param);
            //渲染页面
            loadData();

            billlMonthStr =billMonth.getSelectedYearMonth().toString();
        });

    }
    /**
     * 处理日期组件
     * 显示具体日期 暂弃用
     */
//    private void handleDatePicker(){
//        /**月份组件处理-begin*/
//        //上面月份默认显示当月
//        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
//            @Override
//            public String toString(LocalDate date) {
//                if (date != null) {
//                    return DataUtil.DTF_MONTH.format(date);
//                }
//                return "";
//            }
//
//            @Override
//            public LocalDate fromString(String string) {
//                if (string != null && !string.isEmpty()) {
//                    return LocalDate.parse(string, DataUtil.DTF_MONTH);
//                }
//                return null;
//            }
//        };
//        //billMonth展示为yyyyy-MM格式
//        billMonth.setConverter(converter);
//        billMonth.setPromptText(DataUtil.getCurrentMonth());
//        billlMonthStr = billMonth.getPromptText();
//        // 只允许选择每个月的第一天
//        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
//            @Override
//            public DateCell call(final DatePicker datePicker) {
//                return new DateCell() {
//                    @Override
//                    public void updateItem(LocalDate item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (item.getDayOfMonth() != 1) {
//                            setDisable(true);
//                            setStyle("-fx-background-color: #EEEEEE;");
//                        }
//                    }
//                };
//            }
//        };
//        // 设置日期单元格工厂
//        billMonth.setDayCellFactory(dayCellFactory);
//        //监听billMonth的选择值
//        billMonth.valueProperty().addListener((observable, oldValue, newValue) -> {
//            BillDto param = new BillDto();
//            param.setBillMonth(newValue.toString().substring(0, newValue.toString().lastIndexOf("-")));
//            datas = DataUtil.queryData(param);
//            //渲染页面
//            loadData();
//
//            billlMonthStr = param.getBillMonth();
//        });
//        /**月份组件处理-end*/
//    }
}