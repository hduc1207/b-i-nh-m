package com.pethotel.gui;

import com.pethotel.bus.BookingBUS;
import com.pethotel.bus.ServiceBUS;
import com.pethotel.dao.BookingServiceDetailDAO;
import com.pethotel.dto.BookingServiceDetailDTO;
import com.pethotel.dto.ServiceDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class ServiceDialog extends JDialog {
    // Các biến này IntelliJ tự sinh ra từ file .form, ĐỪNG XÓA
    private JPanel contentPane;
    private JTable tblDetails;
    private JComboBox<String> cboService;
    private JTextField txtQuantity;
    private JButton btnAdd;
    private JButton btnClose;

    // Các biến xử lý dữ liệu
    private int bookingId;
    private Runnable onDataChanged; // Callback báo tin cho màn hình chính
    private BookingBUS bookingBUS = new BookingBUS();
    private ServiceBUS serviceBUS = new ServiceBUS();
    private BookingServiceDetailDAO detailDAO = new BookingServiceDetailDAO();
    private DefaultTableModel tableModel;

    public ServiceDialog(JFrame parent, int bookingId, Runnable onDataChanged) {
        super(parent); // Gọi constructor cha
        this.bookingId = bookingId;
        this.onDataChanged = onDataChanged;

        setTitle("Quản lý Dịch vụ - Booking #" + bookingId);
        setContentPane(contentPane); // Gắn giao diện vừa kéo thả vào
        setModal(true); // Bắt buộc xử lý xong cửa sổ này mới được bấm cái khác
        getRootPane().setDefaultButton(btnAdd); // Bấm Enter là tự thêm

        // 1. Cấu hình bảng
        String[] headers = {"Tên dịch vụ", "Số lượng", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(headers, 0);
        tblDetails.setModel(tableModel);

        // 2. Load dữ liệu
        loadServiceToCombo();
        loadDetailsToTable();

        // 3. Bắt sự kiện nút Thêm
        btnAdd.addActionListener(e -> onAdd());

        // 4. Bắt sự kiện nút Đóng
        btnClose.addActionListener(e -> onCancel());

        // Xử lý khi bấm nút X góc trên cùng
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // Thoát khi bấm ESC
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack(); // Tự co giãn kích thước
        setSize(700, 450); // Set kích thước cố định cho đẹp
        setLocationRelativeTo(parent); // Ra giữa màn hình cha
    }

    private void loadServiceToCombo() {
        cboService.removeAllItems();
        // Lấy danh sách từ MENU (ServiceBUS) đổ vào đây để chọn
        List<ServiceDTO> services = serviceBUS.getAllServices();
        for (ServiceDTO s : services) {
            cboService.addItem(s.getServiceId() + " - " + s.getServiceName());
        }
    }

    private void loadDetailsToTable() {
        tableModel.setRowCount(0);
        // Lấy danh sách chi tiết của Booking này đổ vào bảng
        List<BookingServiceDetailDTO> list = detailDAO.getServicesByBookingId(bookingId);
        for (BookingServiceDetailDTO d : list) {
            tableModel.addRow(new Object[]{
                    d.getServiceId(),
                    d.getQuantity(),
                    String.format("%,.0f", d.getPriceAtBooking()),
                    String.format("%,.0f", d.getQuantity() * d.getPriceAtBooking())
            });
        }
    }

    private void onAdd() {
        try {
            // Lấy ID dịch vụ từ ComboBox (Cắt chuỗi "1 - Tắm rửa")
            String selected = (String) cboService.getSelectedItem();
            if (selected == null) return;
            int serviceId = Integer.parseInt(selected.split(" - ")[0]);

            // Lấy số lượng
            int quantity = Integer.parseInt(txtQuantity.getText());

            // GỌI HÀM THÔNG MINH Ở BƯỚC 1
            String result = bookingBUS.addServiceSmart(bookingId, serviceId, quantity);

            JOptionPane.showMessageDialog(this, result);

            if (result.contains("thành công")) {
                loadDetailsToTable(); // Load lại bảng hiện tại
                txtQuantity.setText(""); // Xóa trắng ô nhập

                // BÁO CHO MÀN HÌNH CHÍNH BIẾT ĐỂ CẬP NHẬT TỔNG TIỀN
                if (onDataChanged != null) {
                    onDataChanged.run();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void onCancel() {
        dispose();
    }
}