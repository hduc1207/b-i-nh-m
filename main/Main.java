package com.pethotel.main;

import com.pethotel.controller.LoginController;
import com.pethotel.gui.Login;
import com.pethotel.utils.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        // 1. THIẾT LẬP GIAO DIỆN (NIMBUS)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Không thể thiết lập giao diện Nimbus: " + e.getMessage());
        }

        // 2. TỰ ĐỘNG TẠO TÀI KHOẢN ADMIN
        checkAndCreateDefaultAdmin();

        // 3. CHẠY MÀN HÌNH ĐĂNG NHẬP
        SwingUtilities.invokeLater(() -> {
            Login loginView = new Login();
            LoginController loginController = new LoginController(loginView);
            loginController.showLoginView();
        });
    }

    // Hàm kiểm tra và tạo admin mặc định
    private static void checkAndCreateDefaultAdmin() {
        String checkSql = "SELECT COUNT(*) FROM Users WHERE Username = 'admin'";
        String insertSql = "INSERT INTO Users (Username, Password, Role, FullName) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Lỗi kết nối CSDL! Kiểm tra lại file DBConnection.");
                return;
            }

            // Kiểm tra xem đã có admin chưa
            PreparedStatement pstCheck = conn.prepareStatement(checkSql);
            ResultSet rs = pstCheck.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                PreparedStatement pstInsert = conn.prepareStatement(insertSql);
                pstInsert.setString(1, "admin");
                pstInsert.setString(2, "123456");
                pstInsert.setString(3, "Admin");
                pstInsert.setString(4, "Administrator");

                pstInsert.executeUpdate();
                System.out.println(">>> Đã khởi tạo tài khoản Admin mặc định: admin / 123456");
            } else {
                System.out.println(">>> Tài khoản Admin đã tồn tại. Sẵn sàng đăng nhập.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi kiểm tra/tạo admin: " + e.getMessage());
        }
    }
}