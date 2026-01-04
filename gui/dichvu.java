package com.pethotel.gui;

import javax.swing.*;

public class dichvu {
    private JButton btnMenuPet;
    private JButton btnMenuCage;
    private JButton btnMenuService;
    private JButton btnMenuBooking;
    private JTextField txtPrice;

    public JButton getBtnMenuPet() {
        return btnMenuPet;
    }

    public void setBtnMenuPet(JButton btnMenuPet) {
        this.btnMenuPet = btnMenuPet;
    }

    public JButton getBtnMenuCage() {
        return btnMenuCage;
    }

    public void setBtnMenuCage(JButton btnMenuCage) {
        this.btnMenuCage = btnMenuCage;
    }

    public JButton getBtnMenuService() {
        return btnMenuService;
    }

    public void setBtnMenuService(JButton btnMenuService) {
        this.btnMenuService = btnMenuService;
    }

    public JButton getBtnMenuBooking() {
        return btnMenuBooking;
    }

    public void setBtnMenuBooking(JButton btnMenuBooking) {
        this.btnMenuBooking = btnMenuBooking;
    }

    public JTextField getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(JTextField txtPrice) {
        this.txtPrice = txtPrice;
    }

    public JTextField getTxtServiceID() {
        return txtServiceID;
    }

    public void setTxtServiceID(JTextField txtServiceID) {
        this.txtServiceID = txtServiceID;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public void setBtnAdd(JButton btnAdd) {
        this.btnAdd = btnAdd;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public void setBtnEdit(JButton btnEdit) {
        this.btnEdit = btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(JButton btnDelete) {
        this.btnDelete = btnDelete;
    }

    public JTable getTblService() {
        return tblService;
    }

    public void setTblService(JTable tblService) {
        this.tblService = tblService;
    }

    public JTextField getTxtServiceName() {
        return txtServiceName;
    }

    public void setTxtServiceName(JTextField txtServiceName) {
        this.txtServiceName = txtServiceName;
    }

    public JButton getBtnMenuAccount() {
        return btnMenuAccount;
    }

    public void setBtnAccount(JButton btnAccount) {
        this.btnMenuAccount = btnAccount;
    }

    public JComboBox getCboUnit() {
        return cboUnit;
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public JButton getBtnExit() {
        return btnExit;
    }

    public JButton getBtnMenuCustomer() {
        return btnMenuCustomer;
    }

    public void setBtnExit(JButton btnExit) {
        this.btnExit = btnExit;
    }

    private JTextField txtServiceID;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JTable tblService;
    private JTextField txtServiceName;
    private JButton btnMenuAccount;
    private JButton btnExit;
    private JButton btnMenuCustomer;
    private JPanel MainPanel;
    private JComboBox cboUnit;
}
