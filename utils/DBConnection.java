package com.pethotel.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Thông tin cấu hình (Sửa lại password của bạn ở đây)
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=PetHotelDB;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa"; // User mặc định của SQL Server
    private static final String PASS = "123"; // Điền pass SQL của bạn vào đây

    private static Connection connection = null;

    // Hàm lấy kết nối
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Kết nối Database thành công!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Kết nối thất bại: " + e.getMessage());
        }
        return connection;
    }

    // Test thử kết nối (Chạy file này để kiểm tra)
    public static void main(String[] args) {
        getConnection();
    }
}