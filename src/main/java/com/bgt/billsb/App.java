package com.bgt.billsb;

import com.bgt.billsb.util.DataUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

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

        // 监听 TabPane 的选择变化,每次切换都重新加载
//        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                try {
//                    if (newValue == tab1) {
//                       reloadTab(tab1,"/com/bgt/billsb/bill.fxml");
//                    } else if (newValue == tab2) {
//                        reloadTab(tab2,"/com/bgt/billsb/statistics.fxml");
//                    } else if (newValue == tab3) {
//                        reloadTab(tab3,"/com/bgt/billsb/setup.fxml");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

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

        // 获取控制器实例并调用加载数据的方法
//        Object controller = loader.getController();
//        if (controller instanceof TabController) {
//            ((TabController) controller).loadData();
//        }
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