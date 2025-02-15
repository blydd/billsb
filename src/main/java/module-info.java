/**
 * @author: bgt
 * @Date: 2025/2/11 13:22
 * @Desc:
 */
module billSB {
    // 导入java.sql模块，用于数据库操作
    requires java.sql;
    // 导入javafx.fxml模块，用于处理FXML文件
    requires javafx.fxml;
    // 导入javafx.controls模块，用于处理JavaFX控件
    requires javafx.controls;
    // 导入javafx.graphics模块，用于处理JavaFX图形
    requires javafx.graphics;
    requires java.desktop;
    // 导出com.bgt.billsb包到javafx.graphics模块，允许javafx.graphics模块使用com.bgt.billsb包中的类
    exports com.bgt.billsb to javafx.graphics;
    // 导出com.bgt.billsb.controller包到javafx.fxml模块，允许javafx.fxml模块使用com.bgt.billsb.controller包中的类
    exports com.bgt.billsb.controller to javafx.fxml;
    // 打开com.bgt.billsb.controller包到javafx.fxml模块，允许javafx.fxml模块使用com.bgt.billsb.controller包中的类
    opens com.bgt.billsb.controller to javafx.fxml;

}