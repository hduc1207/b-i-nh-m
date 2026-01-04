package com.pethotel.controller;

import com.pethotel.bus.ServiceBUS;
import com.pethotel.dto.ServiceDTO;
import com.pethotel.gui.dichvu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ServiceController {
    private dichvu view;
    private ServiceBUS bus;
    private DefaultTableModel tableModel;

    public ServiceController(dichvu view) {
        this.view = view;
        this.bus = new ServiceBUS();

        initTable();
        loadData();
        initEvents();
    }

    private void initTable() {
        String[] headers = {"ID", "Tên dịch vụ", "Giá", "Đơn vị"};
        tableModel = new DefaultTableModel(headers, 0);
        view.getTblService().setModel(tableModel);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<ServiceDTO> list = bus.getAllServices();
        for (ServiceDTO s : list) {
            tableModel.addRow(new Object[]{
                    s.getServiceId(),
                    s.getServiceName(),
                    s.getPrice(),
                    s.getUnit()
            });
        }
    }

    private void initEvents() {
        // Nút Thêm
        view.getBtnAdd().addActionListener(e -> {
            ServiceDTO s = getServiceFromForm();
            if (s != null) {
                JOptionPane.showMessageDialog(null, bus.addService(s));
                loadData();
                clearForm();
            }
        });

        // Nút Sửa
        view.getBtnEdit().addActionListener(e -> {
            int row = view.getTblService().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Chọn dịch vụ cần sửa!");
                return;
            }
            ServiceDTO s = getServiceFromForm();
            if (s != null) {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                s.setServiceId(id);
                JOptionPane.showMessageDialog(null, bus.updateService(s));
                loadData();
                clearForm();
            }
        });

        // Nút Xóa
        view.getBtnDelete().addActionListener(e -> {
            int row = view.getTblService().getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                if (JOptionPane.showConfirmDialog(null, "Xóa dịch vụ này?") == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, bus.deleteService(id));
                    loadData();
                    clearForm();
                }
            }
        });

        // Click bảng
        view.getTblService().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblService().getSelectedRow();
                if (row >= 0) {
                    view.getTxtServiceID().setText(tableModel.getValueAt(row, 0).toString());
                    view.getTxtServiceName().setText(tableModel.getValueAt(row, 1).toString());
                    view.getTxtPrice().setText(tableModel.getValueAt(row, 2).toString());
                    if (tableModel.getValueAt(row, 3) != null) {
                        view.getCboUnit().setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    }
                }
            }
        });

        view.getBtnExit().addActionListener(e -> System.exit(0));
        com.pethotel.utils.NavigationHelper.attachMenuEvents(
                view.getBtnMenuBooking(),
                view.getBtnMenuPet(),
                view.getBtnMenuCage(),
                null,
                view.getBtnMenuCustomer(),
                view.getBtnMenuAccount(),
                view.getMainPanel()
        );
    }

    private ServiceDTO getServiceFromForm() {
        try {
            ServiceDTO s = new ServiceDTO();
            s.setServiceName(view.getTxtServiceName().getText());
            s.setPrice(Double.parseDouble(view.getTxtPrice().getText()));
            s.setUnit(view.getCboUnit().getSelectedItem().toString());
            return s;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Giá phải là số!");
            return null;
        }
    }

    private void clearForm() {
        view.getTxtServiceID().setText("");
        view.getTxtServiceName().setText("");
        view.getTxtPrice().setText("");
    }
}