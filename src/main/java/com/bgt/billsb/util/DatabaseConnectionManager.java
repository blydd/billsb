package com.bgt.billsb.util;//package com.bgt.bill.util;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class DatabaseConnectionManager {
//    private static HikariDataSource dataSource;
//
//    static {
//        HikariConfig config = new HikariConfig();
//        // 设置数据库连接 URL，这里使用嵌入式模式
//        config.setJdbcUrl("jdbc:h2:./bill;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE");
//        config.setUsername("root");
//        config.setPassword("111111");
//        // 设置连接池的一些参数
//        config.setMaximumPoolSize(10);
//        config.setMinimumIdle(2);
//        config.setConnectionTimeout(30000);
//        config.setIdleTimeout(600000);
//        config.setMaxLifetime(1800000);
//
//        dataSource = new HikariDataSource(config);
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return dataSource.getConnection();
//    }
//}