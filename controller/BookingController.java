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
import com.pethotel.gui.RevenueDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingController {
    private quanlybooking view;
    private BookingBUS bus;
    private DefaultTableModel tableModel;
    private CustomerBUS customerBUS;
    private PetBUS petBUS;
    private CageBUS cageBUS;
    private RevenueDialog revenueDialog;

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
        view.getCboStatus().removeAllItems();
        String[] statuses = {"Pending", "Confirmed", "Checked-in", "Checked-out", "Cancelled"};
        for (String s : statuses) view.getCboStatus().addItem(s);

        view.getCboPaymentStatus().removeAllItems();
        String[] payments = {"Pending", "Paid", "Refunded"};
        for (String p : payments) view.getCboPaymentStatus().addItem(p);

        view.getCboUserID().removeAllItems();
        List<CustomerDTO> customers = customerBUS.getAllCustomers();
        for (CustomerDTO c : customers) view.getCboUserID().addItem(c.getCustomerId() + " - " + c.getFullName());

        view.getCboPetID().removeAllItems();
        List<PetDTO> pets = petBUS.getAllPets();
        for (PetDTO p : pets) view.getCboPetID().addItem(p.getPetId() + " - " + p.getPetName());

        view.getCboCageID().removeAllItems();
        List<CageDTO> cages = cageBUS.getAllCages();

        for (CageDTO c : cages) {
            view.getCboCageID().addItem(c.getCageId() + " - " + c.getCageName() + " (" + c.getStatus() + ")");
        }
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<BookingDTO> list = bus.getAllBookings();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (BookingDTO b : list) {
            Object[] row = {
                    b.getBookingId(),
                    b.getCustomerName(),
                    b.getPetName(),
                    b.getCageName(),
                    b.getCheckInDate() != null ? sdf.format(b.getCheckInDate()) : "",
                    b.getCheckOutDate() != null ? sdf.format(b.getCheckOutDate()) : "",
                    b.getStatus(),
                    String.format("%,.0f", b.getTotalPrice()),
                    b.getPaymentStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void initEventHandlers() {
        // --- 2. CLICK BẢNG  ---
        view.getTable1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable1().getSelectedRow();
                if (row >= 0) {
                    try {
                        setSelectedComboBoxItem(view.getCboUserID(), tableModel.getValueAt(row, 1).toString());
                        setSelectedComboBoxItem(view.getCboPetID(), tableModel.getValueAt(row, 2).toString());
                        setSelectedComboBoxItem(view.getCboCageID(), tableModel.getValueAt(row, 3).toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        String inStr = tableModel.getValueAt(row, 4).toString();
                        if (!inStr.isEmpty()) view.getPlnCheckinDate().setDate(sdf.parse(inStr));
                        String outStr = tableModel.getValueAt(row, 5).toString();
                        if (!outStr.isEmpty()) view.getPlnCheckOutDate().setDate(sdf.parse(outStr));
                        view.getCboStatus().setSelectedItem(tableModel.getValueAt(row, 6).toString());
                        String priceStr = tableModel.getValueAt(row, 7).toString().replace(",", "").replace(".", "");
                        view.getTxtTotalPrice().setText(priceStr);

                        if (tableModel.getValueAt(row, 8) != null) {
                            view.getCboPaymentStatus().setSelectedItem(tableModel.getValueAt(row, 8).toString());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // --- 3. TỰ ĐỘNG TÍNH TIỀN ---
        view.getCboCageID().addActionListener(e -> calculateTotalPrice());
        view.getPlnCheckinDate().addPropertyChangeListener("date", evt -> calculateTotalPrice());
        view.getPlnCheckOutDate().addPropertyChangeListener("date", evt -> calculateTotalPrice());

        view.getBtnAdd().addActionListener(e -> {
            String selectedCage = view.getCboCageID().getSelectedItem().toString();
            if (selectedCage.contains("Occupied") || selectedCage.contains("Đang ở")) {
                JOptionPane.showMessageDialog(view.getMainPanel(),
                        "Chuồng này đang có khách! Vui lòng chọn chuồng khác.",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            BookingDTO dto = getBookingFromForm();
            if (dto != null) {
                String result = bus.addBooking(dto);
                JOptionPane.showMessageDialog(view.getMainPanel(), result);
                loadDataToTable();
                loadComboBoxData();
            }
        });
        view.getBtnEdit().addActionListener(e -> {
            int row = view.getTable1().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Chọn dòng để sửa!");
                return;
            }

            // Lấy ID chuồng hiện tại đang chọn trên form
            String selectedCageStr = view.getCboCageID().getSelectedItem().toString();
            int selectedCageId = Integer.parseInt(selectedCageStr.split(" - ")[0]);
            BookingDTO dto = getBookingFromForm();
            if (dto != null) {
                dto.setBookingId(Integer.parseInt(tableModel.getValueAt(row, 0).toString()));
                String result = bus.updateBooking(dto);
                JOptionPane.showMessageDialog(view.getMainPanel(), result);
                loadDataToTable();
                loadComboBoxData();
            }
        });

        view.getBtnDelete().addActionListener(e -> {
            int row = view.getTable1().getSelectedRow();
            if (row != -1 && JOptionPane.showConfirmDialog(view.getMainPanel(), "Bạn chắc chắn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                String result = bus.deleteBooking(id);
                JOptionPane.showMessageDialog(view.getMainPanel(), result);
                loadDataToTable();
            }
        });

        view.getBtnServiceAdd().addActionListener(e -> {
            int row = view.getTable1().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Vui lòng chọn đơn đặt phòng!");
                return;
            }
            new ServiceDialog(view, Integer.parseInt(tableModel.getValueAt(row, 0).toString()), () -> {
                loadDataToTable();
                view.getTable1().setRowSelectionInterval(row, row);
            }).setVisible(true);
        });

        // Xử lý nút thống kê
        try {
            if (view.getBtnThongKe() != null) {
                view.getBtnThongKe().addActionListener(e -> showRevenueDialog());
            }
        } catch (Exception ex) {}

        view.getBtnExit().addActionListener(e -> System.exit(0));
        com.pethotel.utils.NavigationHelper.attachMenuEvents(null, view.getBtnMenuPet(), view.getBtnMenuCage(), view.getBtnMenuService(), view.getBtnMenuCustomer(), view.getBtnMenuAccount(), view.getMainPanel());
    }

    private void showRevenueDialog() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view.getMainPanel());
        revenueDialog = new RevenueDialog(parent);
        revenueDialog.addBtnThongKeListener(e -> handleRevenueLogic());
        revenueDialog.setVisible(true);
    }

    private void handleRevenueLogic() {
        // 1. Lấy ngày từ Dialog
        Date uFrom = revenueDialog.getFromDate();
        Date uTo = revenueDialog.getToDate();

        // 2. Kiểm tra dữ liệu đầu vào
        if (uFrom == null || uTo == null) {
            revenueDialog.showMessage("Vui lòng chọn đầy đủ ngày tháng!");
            return;
        }
        if (uFrom.after(uTo)) {
            revenueDialog.showMessage("Ngày bắt đầu không được lớn hơn ngày kết thúc!");
            return;
        }

        // 3. Chuyển đổi Date -> Timestamp
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(uFrom);
        calFrom.set(Calendar.HOUR_OF_DAY, 0); calFrom.set(Calendar.MINUTE, 0);
        calFrom.set(Calendar.SECOND, 0); calFrom.set(Calendar.MILLISECOND, 0);

        Calendar calTo = Calendar.getInstance();
        calTo.setTime(uTo);
        calTo.set(Calendar.HOUR_OF_DAY, 23); calTo.set(Calendar.MINUTE, 59);
        calTo.set(Calendar.SECOND, 59); calTo.set(Calendar.MILLISECOND, 999);

        // 4. GỌI BUS ĐỂ LẤY DỮ LIỆU
        List<BookingDTO> list = bus.getRevenueByDate(new Timestamp(calFrom.getTimeInMillis()), new Timestamp(calTo.getTimeInMillis()));

        // 5. Đổ dữ liệu lên bảng
        revenueDialog.setTableData(list);
    }

    // --- 4. LẤY DỮ LIỆU TỪ FORM ---
    private BookingDTO getBookingFromForm() {
        try {
            BookingDTO dto = new BookingDTO();
            if (view.getCboUserID().getSelectedItem() != null) dto.setCustomerId(Integer.parseInt(view.getCboUserID().getSelectedItem().toString().split(" - ")[0]));
            if (view.getCboPetID().getSelectedItem() != null) dto.setPetId(Integer.parseInt(view.getCboPetID().getSelectedItem().toString().split(" - ")[0]));
            if (view.getCboCageID().getSelectedItem() != null) dto.setCageId(Integer.parseInt(view.getCboCageID().getSelectedItem().toString().split(" - ")[0]));
            if (view.getPlnCheckinDate().getDate() == null) throw new Exception("Chưa chọn ngày Check-in");
            dto.setCheckInDate(new Timestamp(view.getPlnCheckinDate().getDate().getTime()));

            if (view.getPlnCheckOutDate().getDate() != null) {
                dto.setCheckOutDate(new Timestamp(view.getPlnCheckOutDate().getDate().getTime()));
            }

            dto.setStatus(view.getCboStatus().getSelectedItem().toString());
            dto.setPaymentStatus(view.getCboPaymentStatus().getSelectedItem().toString());
            String price = view.getTxtTotalPrice().getText().isEmpty() ? "0" : view.getTxtTotalPrice().getText().replace(",", "");
            dto.setTotalPrice(Double.parseDouble(price));

            return dto;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getMainPanel(), "Lỗi nhập liệu: " + ex.getMessage());
            return null;
        }
    }

    // --- 5. TÍNH TOÁN TIỀN ---
    private void calculateTotalPrice() {
        try {
            Date inDate = view.getPlnCheckinDate().getDate();
            Date outDate = view.getPlnCheckOutDate().getDate();

            if (inDate == null || outDate == null || view.getCboCageID().getSelectedItem() == null) return;

            int cageId = Integer.parseInt(view.getCboCageID().getSelectedItem().toString().split(" - ")[0]);
            CageDTO cage = cageBUS.getCageById(cageId);
            if (cage != null) {
                long diff = outDate.getTime() - inDate.getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                if (days <= 0) days = 0;
                view.getTxtTotalPrice().setText(String.valueOf(days * cage.getPricePerDay()));
            }
        } catch (Exception e) {}
    }

    private void setSelectedComboBoxItem(JComboBox cbo, String idValue) {
        for (int i = 0; i < cbo.getItemCount(); i++) {
            if (cbo.getItemAt(i).toString().startsWith(idValue + " -") || cbo.getItemAt(i).toString().equals(idValue)) {
                cbo.setSelectedIndex(i);
                return;
            }
        }
    }
}