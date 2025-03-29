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
       // 创建三个 Tab 并添加到 TabPane 中
       Tab tab1 = createTab("明细", "/com/bgt/billsb/bill.fxml");
       Tab tab2 = createTab("统计", "/com/bgt/billsb/statistics.fxml");
       Tab tab3 = createTab("设置", "/com/bgt/billsb/setup.fxml");
       tabPane.getTabs().addAll(tab1, tab2, tab3);
       Scene scene = new Scene(tabPane, 1000, 900);


      

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