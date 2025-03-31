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
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.Group;

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
    @FXML
    private BarChart barChart;

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
        System.out.println("开始加载数据...");
        
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


        // 按支付方式统计支出金额
        Map<String, Double> groupByPayTypeOut = datas.stream()
                .filter(b -> b != null && b.getBillDetailList() != null)
                .flatMap(b -> b.getBillDetailList().stream())
                .filter(b1 -> b1 != null && Objects.equals(1, b1.getInout()))
                .filter(b1 -> b1.getMoney() != null)
                .collect(Collectors.groupingBy(BillDetail::getPayAccountName, Collectors.summingDouble(b1 -> Math.abs(b1.getMoney()))));

        //实现根据value倒序排序
        List<Map.Entry<String, Double>> collect = groupByPayTypeOut.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed()).collect(Collectors.toList());
        // 创建 x 轴和 y 轴
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("支付方式");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("金额");

        // 设置柱状图样式
        barChart.setStyle(
            "-fx-background-color: transparent;" + 
            "-fx-padding: 10px;"
        );
        
        // 清空并设置数据
        barChart.setData(FXCollections.observableArrayList());
        barChart.setTitle("支付方式统计");

        // 创建数据系列
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName(billlMonthStr);

        if (groupByPayTypeOut != null) {
            System.out.println("支付方式数据: " + groupByPayTypeOut);
            
            ObservableList<XYChart.Data<String, Double>> dataSeries = FXCollections.observableArrayList();
            // 定义不同的颜色
            String[] colors = {
                "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", 
                "#FFEEAD", "#D4A5A5", "#9B59B6", "#3498DB",
                "#E67E22", "#2ECC71"
            };
            
            int colorIndex = 0;
            for (Map.Entry<String, Double> entry : collect) {
                System.out.println("处理数据: " + entry.getKey() + " = " + entry.getValue());
                XYChart.Data<String, Double> d = new XYChart.Data<>(entry.getKey(), entry.getValue());
                dataSeries.add(d);
                
                // 设置每个柱子的颜色和数值标签
                final int currentIndex = colorIndex; // 创建 final 变量以在 lambda 中使用
                d.nodeProperty().addListener((ov, oldNode, node) -> {
                    if (node != null) {
                        // 设置柱子颜色
                        String color = colors[currentIndex % colors.length];
                        node.setStyle("-fx-bar-fill: " + color + ";");
                        
                        // 创建数值标签
                        Label label = new Label(String.format("%.2f", d.getYValue()));
                        label.setStyle(
                            "-fx-font-size: 11px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #666666;"
                        );
                        
                        try {
                            // 将标签添加到柱子上方
                            if (node.getParent() instanceof Group) {
                                ((Group) node.getParent()).getChildren().add(label);
                                System.out.println("成功添加标签到柱子: " + d.getXValue());
                            } else {
                                System.out.println("节点父类型不是 Group: " + node.getParent().getClass());
                            }
                        } catch (Exception e) {
                            e.printStackTrace(); // 打印完整的堆栈跟踪
                            System.err.println("添加标签失败: " + e.getMessage() + 
                                             "\n节点类型: " + (node != null ? node.getClass() : "null") +
                                             "\n父节点类型: " + (node != null && node.getParent() != null ? 
                                                             node.getParent().getClass() : "null"));
                        }
                    }
                });
                colorIndex++;
            }
            series.setData(dataSeries);
        }
        
        // 将系列添加到图表
        barChart.getData().add(series);
        
        // 移除系列的默认颜色
        barChart.setLegendVisible(false);
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
}