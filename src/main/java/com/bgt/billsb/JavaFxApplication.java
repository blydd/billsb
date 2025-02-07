package com.bgt.billsb;

import com.bgt.billsb.controller.TabController;
import com.bgt.billsb.entity.User;
import com.bgt.billsb.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * javafx主类
 */
public class  JavaFxApplication extends Application{

    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {


        this.stage = stage;
        // 创建 TabPane
        TabPane tabPane = new TabPane();

        // 创建三个 Tab 并添加到 TabPane 中
        Tab tab1 = createTab("明细", "/com/bgt/billsb/bill.fxml");
        Tab tab2 = createTab("统计", "/com/bgt/billsb/statistics.fxml");
        Tab tab3 = createTab("设置", "/com/bgt/billsb/setup.fxml");

        tabPane.getTabs().addAll(tab1, tab2, tab3);

        Scene scene = new Scene(tabPane, 600, 900);
        stage.setTitle("记账本!");
        stage.setScene(scene);
        stage.show();
    }
    private Tab createTab(String tabTitle, String fxmlPath) throws IOException {
        Tab tab = new Tab(tabTitle);
        // 加载 FXML 文件并设置为 Tab 的内容
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        tab.setContent((Node) loader.load());

        // 获取控制器实例并调用加载数据的方法
        Object controller = loader.getController();
        if (controller instanceof TabController) {
            ((TabController) controller).loadData();
        }
        return tab;
    }

    public static void main(String[] args) {
        launch(args);
    }


}