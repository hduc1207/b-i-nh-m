package com.pethotel.utils;

import com.pethotel.controller.*;
import com.pethotel.gui.*;

import javax.swing.*;
import java.awt.*;

public class NavigationHelper {

    /**
     * Gắn sự kiện cho các nút Menu.
     * Khi bấm nút, thay vì đóng cửa sổ cũ, ta sẽ thay "ruột" (ContentPane) của cửa sổ hiện tại.
     */
    public static void attachMenuEvents(JButton btnBooking, JButton btnPet, JButton btnCage,
                                        JButton btnService, JButton btnCustomer, JButton btnUser,
                                        JComponent currentPanel) {

        if (btnBooking != null) {
            btnBooking.addActionListener(e -> openBooking(currentPanel));
        }

        if (btnPet != null) {
            btnPet.addActionListener(e -> openPet(currentPanel));
        }

        if (btnCage != null) {
            btnCage.addActionListener(e -> openCage(currentPanel));
        }

        if (btnService != null) {
            btnService.addActionListener(e -> openService(currentPanel));
        }

        if (btnCustomer != null) {
            btnCustomer.addActionListener(e -> openCustomer(currentPanel));
        }

        if (btnUser != null) {
            btnUser.addActionListener(e -> openUser(currentPanel));
        }
    }

    // --- HÀM XỬ LÝ CHUYỂN ĐỔI MÀN HÌNH ---
    private static void switchScreen(JComponent currentSource, String title, JComponent newContent) {
        // 1. Tìm cửa sổ cha hiện tại
        Window window = SwingUtilities.getWindowAncestor(currentSource);

        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;

            // 2. Thay thế nội dung cũ bằng nội dung mới
            frame.setContentPane(newContent);
            frame.setTitle(title);
            frame.revalidate();
            frame.repaint();

        } else {
            createAndShowFrame(title, newContent);
        }
    }

    // --- CÁC HÀM KHỞI TẠO CONTROLLER & VIEW MỚI ---
    private static void openBooking(JComponent currentSource) {
        quanlybooking view = new quanlybooking();
        new BookingController(view);
        switchScreen(currentSource, "Quản lý Đặt phòng", view.getMainPanel());
    }

    private static void openPet(JComponent currentSource) {
        quanlythucung view = new quanlythucung();
        new PetController(view);
        switchScreen(currentSource, "Quản lý Thú cưng", view.getMainPanel());
    }

    private static void openCage(JComponent currentSource) {
        quanlychuong view = new quanlychuong();
        new CageController(view);
        switchScreen(currentSource, "Quản lý Chuồng", view.getMainPanel());
    }

    private static void openService(JComponent currentSource) {
        dichvu view = new dichvu();
        new ServiceController(view);
        switchScreen(currentSource, "Quản lý Dịch vụ", view.getMainPanel());
    }

    private static void openCustomer(JComponent currentSource) {
        quanlykhachhang view = new quanlykhachhang();
        new CustomerController(view);
        switchScreen(currentSource, "Quản lý Khách hàng", view.getMainPanel());
    }

    private static void openUser(JComponent currentSource) {
        Taikhoanuser view = new Taikhoanuser();
        new UserController(view);
        switchScreen(currentSource, "Quản lý Tài khoản", view.getMainPanel());
    }

    // Hàm tạo cửa sổ mới
    private static void createAndShowFrame(String title, JComponent content) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}