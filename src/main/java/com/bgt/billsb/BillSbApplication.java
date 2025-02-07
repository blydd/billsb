package com.bgt.billsb;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot启动类
 */
@SpringBootApplication
public class BillSbApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillSbApplication.class, args);

        // 调用JavaFxApplication类的Application.launch方法启动应用，args为启动参数
        Application.launch(JavaFxApplication.class, args);
    }

}
