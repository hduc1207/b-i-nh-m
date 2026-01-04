package com.pethotel.controller;

import com.pethotel.bus.UserBUS;
import com.pethotel.dao.UserDAO;
import com.pethotel.dto.UserDTO;
import com.pethotel.gui.*;

import javax.swing.*;

public class LoginController {
    private Login view;
    private UserDAO userDAO;

    public LoginController(Login view) {
        this.view = view;
        this.userDAO = new UserDAO();
        initEvents();
    }

    public void showLoginView() {
        view.setVisible(true);
    }

    private void initEvents() {
        view.getBtnLogin().addActionListener(e -> login());
        view.getBtnExit().addActionListener(e -> System.exit(0));
        view.getTxtPassword().addActionListener(e -> login());
    }

    private void login() {
        String username = view.getTxtUserName().getText();
        String password = new String(view.getTxtPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Gọi hàm login từ DAO
        UserDTO user = userDAO.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(view, "Đăng nhập thành công!\nXin chào " + user.getFullName());
            view.dispose();

            // Mở màn hình chính
            openMainScreen();
        } else {
            JOptionPane.showMessageDialog(view, "Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

    private void openMainScreen() {
        // Khởi tạo màn hình chính
        quanlybooking bookingView = new quanlybooking();
        BookingController controller = new BookingController(bookingView);

        // Cấu hình hiển thị full màn hình hoặc kích thước cố định
        JFrame frame = new JFrame("Quản lý Booking");
        frame.setContentPane(bookingView.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}