package com.bgt.billsb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:bill.db";
    
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (Exception e) {
//            throw new RuntimeException("Database 初始化失败", e);
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() throws SQLException {
//        try (Connection conn = getConnection();
//             Statement stmt = conn.createStatement()) {
//            stmt.execute("CREATE TABLE IF NOT EXISTS users("
//                + "id INTEGER PRIMARY KEY,"
//                + "name TEXT NOT NULL,"
//                + "email TEXT NOT NULL)");
//        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
