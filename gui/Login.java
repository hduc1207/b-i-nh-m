package com.pethotel.gui;

import javax.swing.*;

public class Login extends JFrame {
    private JPanel MainPanel;
    private JTextField txtUserName;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnExit;
    public Login() {
        setContentPane(MainPanel);
        setTitle("Đăng nhập hệ thống");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public JTextField getTxtUserName() {
        return txtUserName;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnExit() {
        return btnExit;
    }
}
