package com.pethotel.gui;

import javax.swing.*;

public class Taikhoanuser extends JFrame {
    private JButton btnMenuPet;
    private JButton btnMenuCage;
    private JButton btnMenuService;
    private JButton btnMenuBooking;
    private JButton btnMenuAccount;
    private JTextField txtUserName;
    private JTextField txtPassword;
    private JComboBox cboRole;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnEdit;
    private JTable tblAccount;
    private JButton btnExit;
    private JPanel MainPanel;
    private JButton btnMenuCustomer;
    private JTextField txtFullName;


    public JButton getBtnMenuPet() {
        return btnMenuPet;
    }

    public JButton getBtnMenuCage() {
        return btnMenuCage;
    }

    public JButton getBtnMenuService() {
        return btnMenuService;
    }

    public JButton getBtnMenuBooking() {
        return btnMenuBooking;
    }

    public JButton getBtnMenuAccount() {
        return btnMenuAccount;
    }

    public JTextField getTxtUserName() {
        return txtUserName;
    }

    public JTextField getTxtPassword() {
        return txtPassword;
    }

    public JComboBox getCboRole() {
        return cboRole;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JTable getTblAccount() {
        return tblAccount;
    }

    public JButton getBtnExit() {
        return btnExit;
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public JButton getBtnMenuCustomer() {
        return btnMenuCustomer;
    }
    public JTextField getTxtFullName() {
        return txtFullName;
    }

}
