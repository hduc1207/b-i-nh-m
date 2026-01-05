package com.pethotel.gui;

import com.toedter.calendar.JDateChooser;
import com.pethotel.dto.BookingDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RevenueDialog extends JDialog {
    private JPanel contentPane;
    private JButton btnThongKe; // Tên biến nút trong form
    private JTable tblKetQua;   // Tên biến bảng trong form
    private JLabel lblTongTien; // Tên biến label trong form
    private JPanel pnlFromDate; // Panel rỗng 1
    private JPanel pnlToDate;   // Panel rỗng 2

    private JDateChooser dateChooserFrom;
    private JDateChooser dateChooserTo;
    private DefaultTableModel tableModel;

    public RevenueDialog(JFrame parent) {
        super(parent, "Thống Kê Doanh Thu", true);
        setContentPane(contentPane);
        setSize(900, 600);
        setLocationRelativeTo(parent);

        setupCalendars(); // Hàm tự viết để nhúng lịch
        tableModel = new DefaultTableModel(new Object[]{"ID", "Check-in", "Trạng thái", "Tổng tiền"}, 0);
        tblKetQua.setModel(tableModel);
    }

    private void setupCalendars() {
        pnlFromDate.setLayout(new BorderLayout());
        dateChooserFrom = new JDateChooser();
        dateChooserFrom.setDateFormatString("dd/MM/yyyy");
        pnlFromDate.add(dateChooserFrom, BorderLayout.CENTER);

        pnlToDate.setLayout(new BorderLayout());
        dateChooserTo = new JDateChooser();
        dateChooserTo.setDateFormatString("dd/MM/yyyy");
        pnlToDate.add(dateChooserTo, BorderLayout.CENTER);
        this.validate();
    }

    public Date getFromDate() { return dateChooserFrom.getDate(); }
    public Date getToDate() { return dateChooserTo.getDate(); }

    public void setTableData(List<BookingDTO> list) {
        tableModel.setRowCount(0);
        double total = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (BookingDTO b : list) {
            tableModel.addRow(new Object[]{
                    b.getBookingId(),
                    b.getCheckInDate() != null ? sdf.format(b.getCheckInDate()) : "",
                    b.getPaymentStatus(),
                    String.format("%,.0f VNĐ", b.getTotalPrice())
            });
            // Chỉ cộng tiền nếu đã thanh toán (Paid)
            if ("Paid".equalsIgnoreCase(b.getPaymentStatus())) {
                total += b.getTotalPrice();
            }
        }
        lblTongTien.setText("TỔNG THỰC THU (PAID): " + String.format("%,.0f VNĐ", total));
    }

    public void addBtnThongKeListener(java.awt.event.ActionListener listener) {
        btnThongKe.addActionListener(listener);
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    private void createUIComponents() { /* Để trống */ }
}