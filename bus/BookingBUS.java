package com.pethotel.bus;

import com.pethotel.dao.BookingDAO;
import com.pethotel.dao.BookingServiceDetailDAO;
import com.pethotel.dto.BookingDTO;
import com.pethotel.dto.BookingServiceDetailDTO;
import com.pethotel.dto.ServiceDTO;
import com.pethotel.dao.ServiceDAO;

import java.util.List;

public class BookingBUS {
    private BookingDAO bookingDAO = new BookingDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();
    private BookingServiceDetailDAO detailDAO = new BookingServiceDetailDAO();

    public BookingBUS() {
        this.bookingDAO = new BookingDAO();
    }

    public List<BookingDTO> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    public String addBooking(BookingDTO booking) {
        if (booking.getCheckInDate() == null) {
            return "Ngày Check-in không được để trống!";
        }
        if (booking.getCheckOutDate() != null && booking.getCheckOutDate().before(booking.getCheckInDate())) {
            return "Ngày Check-out phải sau ngày Check-in!";
        }
        if (bookingDAO.insertBooking(booking)) {
            return "Thêm Booking thành công!";
        }
        return "Thêm Booking thất bại!";
    }

    public String updateBooking(BookingDTO booking) {
        if (bookingDAO.updateBooking(booking)) {
            return "Cập nhật thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String deleteBooking(int bookingId) {
        if (bookingDAO.deleteBooking(bookingId)) {
            return "Xóa thành công!";
        }
        return "Xóa thất bại!";
    }
    public BookingDTO getBookingById(int id) {
        return bookingDAO.getBookingById(id);
    }
    public String addServiceSmart(int bookingId, int serviceId, int quantity) {
        if (quantity <= 0) return "Số lượng phải lớn hơn 0";

        // 1. Dùng biến 'serviceDAO' đã khai báo ở trên để gọi hàm
        ServiceDTO service = serviceDAO.getServiceById(serviceId);

        if (service == null) return "Dịch vụ không tồn tại!";
        double price = service.getPrice();

        // 2. Tạo đối tượng chi tiết
        BookingServiceDetailDTO detail = new BookingServiceDetailDTO();
        detail.setBookingId(bookingId);
        detail.setServiceId(serviceId);
        detail.setQuantity(quantity);
        detail.setPriceAtBooking(price);

        // 3. Dùng biến 'detailDAO' để lưu
        if (detailDAO.insertServiceDetail(detail)) {
            BookingDTO booking = bookingDAO.getBookingById(bookingId);
            if (booking != null) {
                double newTotal = booking.getTotalPrice() + (price * quantity);
                booking.setTotalPrice(newTotal);
                bookingDAO.updateBooking(booking);
            }
            return "Thêm dịch vụ thành công!";
        }
        return "Lỗi khi lưu dữ liệu!";
    }
    public List<BookingDTO> getRevenueStats(java.sql.Timestamp from, java.sql.Timestamp to) {
        return bookingDAO.getRevenueByDate(from, to);
    }
    public List<BookingDTO> getRevenueByDate(java.sql.Timestamp from, java.sql.Timestamp to) {
        return bookingDAO.getRevenueByDate(from, to);
    }
}
