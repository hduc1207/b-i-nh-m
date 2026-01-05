package com.pethotel.controller;

import com.pethotel.bus.CageBUS;
import com.pethotel.dto.CageDTO;
import com.pethotel.gui.quanlychuong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CageController {
    private quanlychuong view;
    private CageBUS bus;
    private DefaultTableModel tableModel;

    public CageController(quanlychuong view) {
        this.view = view;
        this.bus = new CageBUS();

        initTable();
        loadComboBoxData();
        loadData();
        initEvents();
    }

    private void initTable() {
        // Cấu hình bảng
        String[] headers = {"ID", "Tên/Số hiệu", "Loại", "Giá/Ngày", "Trạng thái"};
        tableModel = new DefaultTableModel(headers, 0);
        view.getTblCageList().setModel(tableModel);
    }

    private void loadComboBoxData() {
        // Load ComboBox Loại chuồng
        view.getCboType().removeAllItems();
        view.getCboType().addItem("VIP");
        view.getCboType().addItem("Thường");

        // Load ComboBox Trạng thái
        view.getCboStatus().removeAllItems();
        view.getCboStatus().addItem("Trống");
        view.getCboStatus().addItem("Đang ở");
        view.getCboStatus().addItem("Bảo trì");
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<CageDTO> list = bus.getAllCages();
        for (CageDTO c : list) {
            tableModel.addRow(new Object[]{
                    c.getCageId(),
                    c.getCageName(),
                    c.getType(),
                    c.getPricePerDay(),
                    c.getStatus()
            });
        }
    }

    private void initEvents() {
        // Nút Thêm
        view.getBtnAdd().addActionListener(e -> {
            CageDTO c = getCageFromForm();
            if (c != null) {
                JOptionPane.showMessageDialog(view.getMainPanel(), bus.addCage(c));
                loadData();
                clearForm();
            }
        });

        // Nút Sửa
        view.getBtnEdit().addActionListener(e -> {
            int row = view.getTblCageList().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Vui lòng chọn chuồng để sửa!");
                return;
            }
            CageDTO c = getCageFromForm();
            if (c != null) {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                c.setCageId(id);
                JOptionPane.showMessageDialog(view.getMainPanel(), bus.updateCage(c));
                loadData();
                clearForm();
            }
        });

        // Nút Xóa
        view.getBtnDelete().addActionListener(e -> {
            int row = view.getTblCageList().getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(view.getMainPanel(),
                        "Bạn có chắc muốn xóa chuồng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(view.getMainPanel(), bus.deleteCage(id));
                    loadData();
                    clearForm();
                }
            } else {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Vui lòng chọn dòng để xóa!");
            }
        });

        // Click bảng -> Đổ dữ liệu lên form
        view.getTblCageList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblCageList().getSelectedRow();
                if (row >= 0) {
                    Object idObj = tableModel.getValueAt(row, 0);
                    view.getTxtCageID().setText(idObj != null ? idObj.toString() : "");

                    Object nameObj = tableModel.getValueAt(row, 1);
                    view.getTxtSerial().setText(nameObj != null ? nameObj.toString() : "");

                    Object typeObj = tableModel.getValueAt(row, 2);
                    if (typeObj != null) view.getCboType().setSelectedItem(typeObj.toString());

                    Object priceObj = tableModel.getValueAt(row, 3);
                    view.getTxtPrice().setText(priceObj != null ? priceObj.toString() : "0");

                    Object statusObj = tableModel.getValueAt(row, 4);
                    if (statusObj != null) view.getCboStatus().setSelectedItem(statusObj.toString());
                }
            }
        });

        // Nút Thoát
        com.pethotel.utils.NavigationHelper.attachMenuEvents(
                view.getBtnMenuBooking(),
                view.getBtnMenuPet(),
                null,                     // Nút Cage (Đang ở đây -> null)
                view.getBtnMenuService(),
                view.getBtnMenuCustomer(),
                view.getBtnMenuAccount(),
                view.getMainPanel()
        );
    }

    private CageDTO getCageFromForm() {
        try {
            CageDTO c = new CageDTO();
            c.setCageName(view.getTxtSerial().getText());
            c.setType(view.getCboType().getSelectedItem().toString());
            c.setPricePerDay(Double.parseDouble(view.getTxtPrice().getText()));
            c.setStatus(view.getCboStatus().getSelectedItem().toString());
            return c;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view.getMainPanel(), "Giá tiền phải là số!");
            return null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getMainPanel(), "Lỗi nhập liệu: " + ex.getMessage());
            return null;
        }
    }

    private void clearForm() {
        view.getTxtCageID().setText("");
        view.getTxtSerial().setText("");
        view.getTxtPrice().setText("");
        view.getTblCageList().clearSelection();
    }
}