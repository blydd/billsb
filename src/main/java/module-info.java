/**
 * @author: bgt
 * @Date: 2025/2/11 13:22
 * @Desc:
 */
open module billSB {
    // 导入java.sql模块，用于数据库操作
    requires java.sql;
    // 导入javafx.fxml模块，用于处理FXML文件
    requires javafx.fxml;
    // 导入javafx.controls模块，用于处理JavaFX控件
    requires javafx.controls;
    // 导入javafx.graphics模块，用于处理JavaFX图形
    requires javafx.graphics;
    requires java.desktop;
    requires cn.hutool;
    requires com.google.common;
    
    exports com.bgt.billsb;
    exports com.bgt.billsb.controller;
    exports com.bgt.billsb.eenum;
    exports com.bgt.billsb.util;
}