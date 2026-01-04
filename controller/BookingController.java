package com.pethotel.controller;

import com.pethotel.bus.BookingBUS;
import com.pethotel.bus.CustomerBUS;
import com.pethotel.bus.PetBUS;
import com.pethotel.bus.CageBUS;
import com.pethotel.dto.CustomerDTO;
import com.pethotel.dto.PetDTO;
import com.pethotel.dto.CageDTO;
import com.pethotel.dto.BookingDTO;
import com.pethotel.gui.ServiceDialog;
import com.pethotel.gui.quanlybooking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class BookingController {
    private quanlybooking view;
    private BookingBUS bus;
    private DefaultTableModel tableModel;
    private CustomerBUS customerBUS;
    private PetBUS petBUS;
    private CageBUS cageBUS;

    public BookingController(quanlybooking view) {
        this.view = view;
        this.bus = new BookingBUS();
        this.customerBUS = new CustomerBUS();
        this.petBUS = new PetBUS();
        this.cageBUS = new CageBUS();

        initTable();
        loadComboBoxData();
        loadDataToTable();
        initEventHandlers();
    }

    private void initTable() {
        String[] headers = {"ID", "Khách hàng", "Thú cưng", "Chuồng", "Check-in", "Check-out", "Trạng thái", "Tổng tiền", "Thanh toán"};
        tableModel = new DefaultTableModel(headers, 0);
        view.getTable1().setModel(tableModel);
    }

    private void loadComboBoxData() {
        // 1. Load Trạng thái Quy trình
        view.getCboStatus().removeAllItems();
        String[] statuses = {"Pending", "Confirmed", "Checked-in", "Checked-out", "Cancelled"};
        for (String s : statuses) view.getCboStatus().addItem(s);

        //Load Trạng thái Thanh toán (Kết nối với GUI đã sửa)
        view.getCboPaymentStatus().removeAllItems();
        String[] payments = {"Pending", "Paid", "Refunded"}; // Chưa trả, Đã trả, Hoàn tiền
        for (String p : payments) view.getCboPaymentStatus().addItem(p);

        // 2. Load Customer
        view.getCboUserID().removeAllItems();
        List<CustomerDTO> customers = customerBUS.getAllCustomers();
        for (CustomerDTO c : customers) {
            view.getCboUserID().addItem(c.getCustomerId() + " - " + c.getFullName());
        }

        // 3. Load Pet
        view.getCboPetID().removeAllItems();
        List<PetDTO> pets = petBUS.getAllPets();
        for (PetDTO p : pets) {
            view.getCboPetID().addItem(p.getPetId() + " - " + p.getPetName());
        }

        // 4. Load Cage
        view.getCboCageID().removeAllItems();
        List<CageDTO> cages = cageBUS.getAvailableCages();
        for (CageDTO c : cages) {
            view.getCboCageID().addItem(c.getCageId() + " - " + c.getCageName() + " (" + c.getType() + ")");
        }
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<BookingDTO> list = bus.getAllBookings();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (BookingDTO b : list) {
            Object[] row = {
                    b.getBookingId(),
                    b.getCustomerName(),
                    b.getPetName(),
                    b.getCageName(),
                    b.getCheckInDate() != null ? sdf.format(b.getCheckInDate()) : "",
                    b.getCheckOutDate() != null ? sdf.format(b.getCheckOutDate()) : "",
                    b.getStatus(),
                    b.getTotalPrice(),
                    b.getPaymentStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void initEventHandlers() {
        // --- CÁC NÚT CRUD ---
        view.getBtnAdd().addActionListener(e -> {
            BookingDTO dto = getBookingFromForm();
            if (dto != null) {
                String result = bus.addBooking(dto);
                JOptionPane.showMessageDialog(view.getMainPanel(), result);
                if (result.contains("thành công")) loadDataToTable();
            }
        });

        view.getBtnEdit().addActionListener(e -> {
            int selectedRow = view.getTable1().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Vui lòng chọn dòng để sửa!");
                return;
            }
            BookingDTO dto = getBookingFromForm();
            if (dto != null) {
                int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                dto.setBookingId(id);
                String result = bus.updateBooking(dto);
                JOptionPane.showMessageDialog(view.getMainPanel(), result);
                loadDataToTable();
            }
        });

        view.getBtnDelete().addActionListener(e -> {
            int selectedRow = view.getTable1().getSelectedRow();
            if (selectedRow != -1) {
                int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                if (JOptionPane.showConfirmDialog(view.getMainPanel(), "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    String result = bus.deleteBooking(id);
                    JOptionPane.showMessageDialog(view.getMainPanel(), result);
                    loadDataToTable();
                }
            }
        });

        // --- SỰ KIỆN CLICK BẢNG ---
        view.getTable1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable1().getSelectedRow();
                if (row >= 0) {
                    setSelectedComboBoxItem(view.getCboUserID(), tableModel.getValueAt(row, 1).toString());
                    setSelectedComboBoxItem(view.getCboPetID(), tableModel.getValueAt(row, 2).toString());
                    setSelectedComboBoxItem(view.getCboCageID(), tableModel.getValueAt(row, 3).toString());

                    view.getTxtCheckinDate().setText(tableModel.getValueAt(row, 4).toString());
                    view.getTxtCheckOutDate().setText(tableModel.getValueAt(row, 5).toString());
                    view.getCboStatus().setSelectedItem(tableModel.getValueAt(row, 6).toString());
                    view.getTxtTotalPrice().setText(tableModel.getValueAt(row, 7).toString());

                    //Chọn đúng Payment Status trên ComboBox
                    if (tableModel.getValueAt(row, 8) != null) {
                        view.getCboPaymentStatus().setSelectedItem(tableModel.getValueAt(row, 8).toString());
                    }
                }
            }
        });

        // --- SỰ KIỆN TỰ ĐỘNG TÍNH TIỀN ---
        view.getCboCageID().addActionListener(e -> calculateTotalPrice());

        view.getTxtCheckOutDate().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                calculateTotalPrice();
            }
        });

        view.getTxtCheckinDate().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                calculateTotalPrice();
            }
        });

        // Nút thoát và Menu
        view.getBtnExit().addActionListener(e -> System.exit(0));
        com.pethotel.utils.NavigationHelper.attachMenuEvents(
                null,
                view.getBtnMenuPet(),
                view.getBtnMenuCage(),
                view.getBtnMenuService(),
                view.getBtnMenuCustomer(),
                view.getBtnMenuAccount(),
                view.getMainPanel()
        );
        // Xử lý khi bấm nút "Chọn dịch vụ"
        view.getBtnServiceAdd().addActionListener(e -> {
            int row = view.getTable1().getSelectedRow();

            // Kiểm tra xem đã chọn dòng nào chưa
            if (row == -1) {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Vui lòng chọn đơn đặt phòng trước!");
                return;
            }

            // Lấy ID của Booking đang chọn
            int bookingId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            ServiceDialog dialog = new ServiceDialog(view, bookingId, () -> {

                // --- ĐÂY LÀ HÀM SẼ CHẠY KHI DIALOG BÁO CÓ THAY ĐỔI ---

                // 1. Load lại bảng Booking chính
                loadDataToTable();

                // 2. Chọn lại đúng cái dòng nãy đang chọn
                view.getTable1().setRowSelectionInterval(row, row);

                // 3. Cập nhật ngay lập tức con số ở ô tổng tiền
                view.getTxtTotalPrice().setText(tableModel.getValueAt(row, 7).toString());
            });

            dialog.setVisible(true); // Hiện cửa sổ lên
        });
    }

    private void setSelectedComboBoxItem(JComboBox cbo, String idValue) {
        for (int i = 0; i < cbo.getItemCount(); i++) {
            String item = cbo.getItemAt(i).toString();
            if (item.startsWith(idValue + " -") || item.equals(idValue)) {
                cbo.setSelectedIndex(i);
                return;
            }
        }
    }

    private BookingDTO getBookingFromForm() {
        try {
            BookingDTO dto = new BookingDTO();

            if (view.getCboUserID().getSelectedItem() != null) {
                String s = view.getCboUserID().getSelectedItem().toString();
                dto.setCustomerId(Integer.parseInt(s.split(" - ")[0]));
            }
            if (view.getCboPetID().getSelectedItem() != null) {
                String s = view.getCboPetID().getSelectedItem().toString();
                dto.setPetId(Integer.parseInt(s.split(" - ")[0]));
            }
            if (view.getCboCageID().getSelectedItem() != null) {
                String s = view.getCboCageID().getSelectedItem().toString();
                dto.setCageId(Integer.parseInt(s.split(" - ")[0]));
            }

            dto.setCheckInDate(Timestamp.valueOf(view.getTxtCheckinDate().getText()));
            if (!view.getTxtCheckOutDate().getText().isEmpty()) {
                dto.setCheckOutDate(Timestamp.valueOf(view.getTxtCheckOutDate().getText()));
            }

            dto.setStatus(view.getCboStatus().getSelectedItem().toString());

            //Lấy tổng tiền
            dto.setTotalPrice(Double.parseDouble(view.getTxtTotalPrice().getText()));

            //Lấy trạng thái thanh toán từ ComboBox
            dto.setPaymentStatus(view.getCboPaymentStatus().getSelectedItem().toString());

            return dto;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getMainPanel(), "Lỗi nhập liệu: " + ex.getMessage());
            return null;
        }
    }

    private void calculateTotalPrice() {
        try {
            String inStr = view.getTxtCheckinDate().getText();
            String outStr = view.getTxtCheckOutDate().getText();

            if (inStr.isEmpty() || outStr.isEmpty()) return;
            if (view.getCboCageID().getSelectedItem() == null) return;

            String cageString = view.getCboCageID().getSelectedItem().toString();
            int cageId = Integer.parseInt(cageString.split(" - ")[0]);

            CageDTO cage = cageBUS.getCageById(cageId);
            if (cage == null) return;

            double pricePerDay = cage.getPricePerDay();

            Timestamp inDate = Timestamp.valueOf(inStr);
            Timestamp outDate = Timestamp.valueOf(outStr);

            long diffTime = outDate.getTime() - inDate.getTime();
            long days = diffTime / (1000 * 60 * 60 * 24);

            if (days <= 0) days = 0;

            double total = days * pricePerDay;

            view.getTxtTotalPrice().setText(String.valueOf(total));

        } catch (Exception e) {
        }
    }
}