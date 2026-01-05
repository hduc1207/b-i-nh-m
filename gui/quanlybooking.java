package com.pethotel.gui;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;

public class quanlybooking extends javax.swing.JFrame {
    private JButton btnMenuPet;
    private JButton btnMenuCage;
    private JButton btnMenuService;
    private JButton btnMenuBooking;
    private JButton btnMenuCustomer;
    private JButton btnMenuAccount;
    private JButton btnExit;

    private JComboBox cboUserID;
    private JComboBox cboPetID;
    private JComboBox cboCageID;
    private JTextArea txtTotalPrice;
    private JComboBox cboStatus;
    private JComboBox cboPaymentStatus;

    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnServiceAdd;
    private JButton btnThongKe;

    private JTable table1;
    private JPanel MainPanel;
    private JPanel plnCheckOutDate;
    private JPanel plnCheckinDate;
    private JDateChooser dateChooserCheckIn;
    private JDateChooser dateChooserCheckOut;
    public quanlybooking() {
        setupCalendars();

        setContentPane(MainPanel);
        setTitle("Quản Lý Đặt Phòng");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void setupCalendars() {
        // 1. Setup Lịch Check-in
        plnCheckinDate.setLayout(new BorderLayout());
        dateChooserCheckIn = new JDateChooser();
        dateChooserCheckIn.setDateFormatString("dd-MM-yyyy");
        plnCheckinDate.add(dateChooserCheckIn, BorderLayout.CENTER);

        // 2. Setup Lịch Check-out
        plnCheckOutDate.setLayout(new BorderLayout());
        dateChooserCheckOut = new JDateChooser();
        dateChooserCheckOut.setDateFormatString("dd-MM-yyyy");
        plnCheckOutDate.add(dateChooserCheckOut, BorderLayout.CENTER);

        this.validate();
    }

    public JDateChooser getPlnCheckinDate() {
        return dateChooserCheckIn;
    }

    public JDateChooser getPlnCheckOutDate() {
        return dateChooserCheckOut;
    }
    public JTextArea getTxtTotalPrice() { return txtTotalPrice; }
    public JButton getBtnServiceAdd() { return btnServiceAdd; }
    public JButton getBtnThongKe() { return btnThongKe; }
    public JButton getBtnMenuPet() { return btnMenuPet; }
    public JButton getBtnMenuCage() { return btnMenuCage; }
    public JButton getBtnMenuService() { return btnMenuService; }
    public JButton getBtnMenuBooking() { return btnMenuBooking; }
    public JComboBox getCboUserID() { return cboUserID; }
    public JComboBox getCboPetID() { return cboPetID; }
    public JComboBox getCboCageID() { return cboCageID; }
    public JComboBox getCboStatus() { return cboStatus; }
    public JComboBox getCboPaymentStatus() { return cboPaymentStatus; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnEdit() { return btnEdit; }
    public JTable getTable1() { return table1; }
    public JButton getBtnMenuAccount() { return btnMenuAccount; }
    public JButton getBtnExit() { return btnExit; }
    public JPanel getMainPanel() { return MainPanel; }
    public JButton getBtnMenuCustomer() { return btnMenuCustomer; }
    private void createUIComponents() {
    }
}