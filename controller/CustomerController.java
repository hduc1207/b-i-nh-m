package com.pethotel.controller;

import com.pethotel.bus.CustomerBUS;
import com.pethotel.dto.CustomerDTO;
import com.pethotel.gui.quanlykhachhang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerController {
    private quanlykhachhang view;
    private CustomerBUS bus;
    private DefaultTableModel tableModel;

    public CustomerController(quanlykhachhang view) {
        this.view = view;
        this.bus = new CustomerBUS();

        initTable();
        loadData();
        initEvents();
    }

    private void initTable() {
        String[] headers = {"ID", "Tên khách hàng", "SĐT", "Email", "Địa chỉ"};
        tableModel = new DefaultTableModel(headers, 0);
        view.getTblCustomer().setModel(tableModel); // Nhớ tạo Getter trong GUI
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<CustomerDTO> list = bus.getAllCustomers();
        for (CustomerDTO c : list) {
            tableModel.addRow(new Object[]{
                    c.getCustomerCode(), c.getFullName(), c.getPhoneNumber(), c.getEmail(), c.getAddress()
            });
        }
    }

    private void initEvents() {
        // Sự kiện click bảng
        view.getTblCustomer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblCustomer().getSelectedRow();
                if (row >= 0) {
                    // 1. Lấy ID
                    Object idObj = tableModel.getValueAt(row, 0); // Lấy ID từ bảng
                    if (idObj != null) {
                        view.getTxtCustomerID().setText(idObj.toString());
                    }

                    // 2. Lấy Tên
                    Object nameObj = tableModel.getValueAt(row, 1);
                    view.getTxtCustomerName().setText(nameObj != null ? nameObj.toString() : "");

                    // 3. Lấy SĐT
                    Object phoneObj = tableModel.getValueAt(row, 2);
                    view.getTxtPhoneNumber().setText(phoneObj != null ? phoneObj.toString() : "");

                    // 4. Lấy Email (Thường hay bị Null)
                    Object emailObj = tableModel.getValueAt(row, 3);
                    view.getTxtEmail().setText(emailObj != null ? emailObj.toString() : "");

                    // 5. Lấy Địa chỉ (Thường hay bị Null)
                    Object addressObj = tableModel.getValueAt(row, 4);
                    view.getTxtAddress().setText(addressObj != null ? addressObj.toString() : "");

                    // 6. Lấy Ghi chú (Nếu có)
                    Object noteObj = tableModel.getValueAt(row, 5);
                    view.getTxtNote().setText(noteObj != null ? noteObj.toString() : "");
                }
            }
        });
        view.getBtnAdd().addActionListener(e -> {
            CustomerDTO c = new CustomerDTO();
            c.setFullName(view.getTxtCustomerName().getText());
            c.setPhoneNumber(view.getTxtPhoneNumber().getText());
            c.setEmail(view.getTxtEmail().getText());
            c.setAddress(view.getTxtAddress().getText());
            c.setNote(view.getTxtNote().getText());
            JOptionPane.showMessageDialog(null, bus.addCustomer(c));
            loadData();
        });

        // Nút Thoát
        view.getBtnExit().addActionListener(e -> System.exit(0));
        com.pethotel.utils.NavigationHelper.attachMenuEvents(
                view.getBtnMenuBooking(),
                view.getBtnMenuPet(),
                view.getBtnMenuCage(),
                view.getBtnMenuService(),
                null,
                view.getBtnMenuAccount(),
                view.getMainPanel()
        );
    }
}