package com.bgt.billsb.util;//package com.bgt.bill.util;
//
//import org.h2.tools.Server;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//import java.sql.SQLException;
//
//public class H2ServerStarter {
//    private static final String DB_URL = "jdbc:h2:./bill";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "111111";
//
//    public static void main(String[] args) {
//        try {
//            // 启动 TCP 服务器
//            Server tcpServer = Server.createTcpServer("-tcpAllowOthers").start();
//            // 启动 Web 服务器
//            Server webServer = Server.createWebServer("-webAllowOthers").start();
//            System.out.println("H2 数据库服务已启动");
//
//            // 创建数据库连接
//            try (Connection conn = DriverManager.getConnection(DB_URL, "sa", "");
//                 Statement stmt = conn.createStatement()) {
//                // 创建用户并设置密码
//                String createUserSql = "CREATE USER IF NOT EXISTS " + DB_USER + " PASSWORD '" + DB_PASSWORD + "'";
//                stmt.executeUpdate(createUserSql);
//                System.out.println("用户创建成功");
//
//                // 授予用户权限（这里授予 ALL 权限，可根据实际需求调整）
//                String grantPrivilegesSql = "GRANT ALL ON SCHEMA PUBLIC TO " + DB_USER;
//                stmt.executeUpdate(grantPrivilegesSql);
//                System.out.println("权限授予成功");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}