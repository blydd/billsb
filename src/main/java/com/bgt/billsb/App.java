package com.bgt.billsb;

import com.bgt.billsb.util.DataUtil;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * javafx主类
 */
public class App extends Application{


    @Override
    public void start(Stage stage) throws IOException {
        // 创建 TabPane
        TabPane tabPane = new TabPane();
        
        // 禁用标签关闭按钮
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // 创建三个 Tab 并添加到 TabPane 中
        Tab tab1 = createTab("明细", "/com/bgt/billsb/bill.fxml");
        Tab tab2 = createTab("统计", "/com/bgt/billsb/statistics.fxml");
        Tab tab3 = createTab("设置", "/com/bgt/billsb/setup.fxml");
        
        // 设置Tab的宽度和高度
        tabPane.setTabMinWidth(333);  
        tabPane.setTabMaxWidth(333);  
        tabPane.setTabMinHeight(50);   // 设置标签高度
        
        // 优化样式
        tabPane.setStyle(
            ".tab-pane .tab-header-area .tab-header-background {" +
            "    -fx-background-color: #f4f4f4;" +
            "    -fx-border-width: 0 0 1 0;" +    
            "    -fx-border-color: #ddd;" +       
            "}" +
            ".tab-pane .tab {" +
            "    -fx-background-color: #f8f9fa;" +
            "    -fx-background-insets: 0;" +
            "    -fx-background-radius: 0;" +
            "    -fx-padding: 0 20;" +            
            "    -fx-text-fill: #666666;" +       
            "}" +
            ".tab-pane .tab .tab-label {" +       // 直接针对标签文本
            "    -fx-text-fill: #666666;" +
            "    -fx-alignment: CENTER;" +
            "}" +
            ".tab-pane .tab:hover .tab-label {" + // 悬停时的文本样式
            "    -fx-text-fill: #3b7cac;" +
            "}" +
            ".tab-pane .tab:selected {" +
            "    -fx-background-color: white;" +
            "}" +
            ".tab-pane .tab:selected .tab-label {" + // 选中时的文本样式
            "    -fx-text-fill: #3b7cac;" +
            "}" +
            ".tab-pane .focus-indicator {" +
            "    -fx-border-width: 0;" +
            "}"
        );

        tabPane.getTabs().addAll(tab1, tab2, tab3);
        Scene scene = new Scene(tabPane, 1000, 900);
        
        // 监听窗口宽度变化
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double tabWidth = width / 3.0;
            tabPane.setTabMinWidth(tabWidth);
            tabPane.setTabMaxWidth(tabWidth);
        });

        stage.setTitle("记账本!");
        stage.setScene(scene);
        stage.show();

        DataUtil.queryData(null);
    }
    private Tab createTab(String tabTitle, String fxmlPath) throws IOException {
        Tab tab = new Tab(tabTitle);
        // 加载 FXML 文件并设置为 Tab 的内容
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        tab.setContent((Node) loader.load());
        
        // 设置标签样式类
        tab.setStyle("-fx-font-family: 'Microsoft YaHei'; -fx-font-size: 16px; -fx-font-weight: bold;");
        return tab;
    }
    private void reloadTab(Tab tab, String fxmlPath) throws IOException {
        // 加载 FXML 文件并设置为 Tab 的内容
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        tab.setContent((Node) loader.load());
    }

    public static void main(String[] args) {
        launch(args);
    }


}