package com.bgt.billsb;

import com.bgt.billsb.entity.User;
import com.bgt.billsb.service.UserService;
import javafx.application.Application;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * springboot启动类
 */
@SpringBootApplication(scanBasePackages = {"com.bgt.billsb"})
@MapperScan("com.bgt.billsb.mapper")
public class BillSbApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BillSbApplication.class, args);


        // 调用JavaFxApplication类的Application.launch方法启动应用，args为启动参数
        Application.launch(JavaFxApplication.class, args);
    }

    @Autowired
    private UserService userService;
    @Override
    public void run(String... args) throws Exception {
        System.out.println("StatisticsController.run");


        List<User> allUsers = userService.getAllUsers();
        System.out.println("allUsers = " + allUsers.size());
    }
}
