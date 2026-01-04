package com.pethotel.controller;

import com.pethotel.bus.CustomerBUS;
import com.pethotel.bus.PetBUS;
import com.pethotel.dto.CustomerDTO;
import com.pethotel.dto.PetDTO;
import com.pethotel.gui.quanlythucung;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PetController {
    private quanlythucung view;
    private PetBUS bus;
    private CustomerBUS customerBUS;
    private DefaultTableModel tableModel;

    public PetController(quanlythucung view) {
        this.view = view;
        this.bus = new PetBUS();
        this.customerBUS = new CustomerBUS();

        initTable();
        loadComboBoxData();
        loadData();
        initEvents();
    }

    private void initTable() {
        // Cấu hình tên cột
        String[] headers = {"Mã TC", "Tên thú cưng", "Giống", "Loài", "Cân nặng", "Mã Chủ", "Tình trạng"};
        tableModel = new DefaultTableModel(headers, 0);
        view.getTblPetList().setModel(tableModel);
    }

    private void loadComboBoxData() {
        // Load danh sách chủ nhân
        view.getCboOwner().removeAllItems();
        List<CustomerDTO> customers = customerBUS.getAllCustomers();
        for (CustomerDTO c : customers) {
            view.getCboOwner().addItem(c.getCustomerId() + " - " + c.getFullName());
        }

        // Load loài
        view.getCboSpecies().removeAllItems();
        view.getCboSpecies().addItem("Chó");
        view.getCboSpecies().addItem("Mèo");
        view.getCboSpecies().addItem("Khác");

        // Load tình trạng sức khỏe (Nếu chưa có trong Properties)
        view.getCboHealth().removeAllItems();
        view.getCboHealth().addItem("Bình thường");
        view.getCboHealth().addItem("Đang ốm");
        view.getCboHealth().addItem("Cần theo dõi");
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<PetDTO> list = bus.getAllPets();
        for (PetDTO p : list) {
            tableModel.addRow(new Object[]{
                    p.getPetCode(),
                    p.getPetName(),
                    p.getBreed(),
                    p.getSpecies(),
                    p.getWeight(),
                    p.getCustomerCode(),
                    p.getHealthStatus()
            });
        }
    }

    private void initEvents() {
        // --- NÚT THÊM ---
        view.getBtnAdd().addActionListener(e -> {
            PetDTO p = getPetFromForm();
            if(p != null) {
                JOptionPane.showMessageDialog(view.getMainPanel(), bus.addPet(p));
                loadData();
                clearForm();
            }
        });

        // --- NÚT SỬA ---
        view.getBtnEdit().addActionListener(e -> {
            int row = view.getTblPetList().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Vui lòng chọn thú cưng để sửa!");
                return;
            }
            PetDTO p = getPetFromForm();
            if(p != null) {
                String codeStr = tableModel.getValueAt(row, 0).toString(); // "TC005"
                try {
                    int id = Integer.parseInt(codeStr.substring(2));
                    p.setPetId(id);

                    JOptionPane.showMessageDialog(view.getMainPanel(), bus.updatePet(p));
                    loadData();
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view.getMainPanel(), "Lỗi xử lý ID: " + ex.getMessage());
                }
            }
        });

        // --- NÚT XÓA ---
        view.getBtnDelete().addActionListener(e -> {
            int row = view.getTblPetList().getSelectedRow();
            if (row != -1) {
                String codeStr = tableModel.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(view.getMainPanel(),
                        "Bạn có chắc muốn xóa thú cưng " + codeStr + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);

                if(confirm == JOptionPane.YES_OPTION){
                    try {
                        int id = Integer.parseInt(codeStr.substring(2));
                        JOptionPane.showMessageDialog(view.getMainPanel(), bus.deletePet(id));
                        loadData();
                        clearForm();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view.getMainPanel(), "Lỗi xóa: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Vui lòng chọn dòng để xóa!");
            }
        });

        // --- CLICK BẢNG ---
        view.getTblPetList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblPetList().getSelectedRow();
                if (row >= 0) {
                    // 1. Mã Thú Cưng
                    Object codeObj = tableModel.getValueAt(row, 0);
                    view.getTxtPetID().setText(codeObj != null ? codeObj.toString() : "");

                    // 2. Tên
                    Object nameObj = tableModel.getValueAt(row, 1);
                    view.getTxtPetName().setText(nameObj != null ? nameObj.toString() : "");

                    // 3. Giống
                    Object breedObj = tableModel.getValueAt(row, 2);
                    view.getTxtBreed().setText(breedObj != null ? breedObj.toString() : "");

                    // 4. Loài (ComboBox)
                    Object speciesObj = tableModel.getValueAt(row, 3);
                    if (speciesObj != null) view.getCboSpecies().setSelectedItem(speciesObj.toString());

                    // 5. Cân nặng
                    Object weightObj = tableModel.getValueAt(row, 4);
                    view.getTxtWeight().setText(weightObj != null ? weightObj.toString() : "0");

                    // 6. Mã Chủ
                    Object ownerCodeObj = tableModel.getValueAt(row, 5); // KH001
                    if (ownerCodeObj != null) {
                        String khCode = ownerCodeObj.toString(); // "KH001"
                        try {
                            int ownerId = Integer.parseInt(khCode.substring(2));
                            setComboBoxByPrefix(view.getCboOwner(), String.valueOf(ownerId));
                        } catch (Exception ex) {
                        }
                    }

                    // 7. Tình trạng
                    Object healthObj = tableModel.getValueAt(row, 6);
                    if (healthObj != null) view.getCboHealth().setSelectedItem(healthObj.toString());
                }
            }
        });

        // Nút Thoát & Menu
        view.getBtnExit().addActionListener(e -> System.exit(0));
        com.pethotel.utils.NavigationHelper.attachMenuEvents(
                view.getBtnMenuBooking(),
                null,
                view.getBtnMenuCage(),
                view.getBtnMenuService(),
                view.getBtnMenuCustomer(),
                view.getBtnMenuAccount(),
                view.getMainPanel()
        );
    }

    private PetDTO getPetFromForm() {
        try {
            PetDTO p = new PetDTO();
            p.setPetName(view.getTxtPetName().getText());
            p.setBreed(view.getTxtBreed().getText());
            p.setSpecies(view.getCboSpecies().getSelectedItem().toString());
            p.setWeight(Double.parseDouble(view.getTxtWeight().getText()));
            p.setHealthStatus(view.getCboHealth().getSelectedItem().toString());

            // Lấy ID chủ từ ComboBox "1 - Nguyễn Văn A"
            if (view.getCboOwner().getSelectedItem() != null) {
                String ownerString = view.getCboOwner().getSelectedItem().toString();
                p.setCustomerId(Integer.parseInt(ownerString.split(" - ")[0]));
            }

            return p;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view.getMainPanel(), "Cân nặng phải là số!");
            return null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getMainPanel(), "Lỗi nhập liệu: " + ex.getMessage());
            return null;
        }
    }

    // Hàm phụ trợ để chọn ComboBox
    private void setComboBoxByPrefix(JComboBox cbo, String prefix) {
        for (int i = 0; i < cbo.getItemCount(); i++) {
            String item = cbo.getItemAt(i).toString();
            if (item.startsWith(prefix + " -")) {
                cbo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void clearForm() {
        view.getTxtPetID().setText("");
        view.getTxtPetName().setText("");
        view.getTxtBreed().setText("");
        view.getTxtWeight().setText("");
        view.getTblPetList().clearSelection();
    }
}