package com.pethotel.dao;

import com.pethotel.dto.BookingServiceDetailDTO;
import com.pethotel.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookingServiceDetailDAO {

    // 1. Lấy tất cả dịch vụ của 1 Booking
    public List<BookingServiceDetailDTO> getServicesByBookingId(int bookingId) {
        List<BookingServiceDetailDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BookingServices WHERE BookingID = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BookingServiceDetailDTO detail = new BookingServiceDetailDTO();
                detail.setDetailId(rs.getInt("DetailID"));
                detail.setBookingId(rs.getInt("BookingID"));
                detail.setServiceId(rs.getInt("ServiceID"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPriceAtBooking(rs.getDouble("PriceAtBooking"));

                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm dịch vụ vào Booking
    public boolean insertServiceDetail(BookingServiceDetailDTO d) {
        String sql = """
            INSERT INTO BookingServices
                (BookingID, ServiceID, Quantity, PriceAtBooking)
            VALUES (?, ?, ?, ?)
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, d.getBookingId());
            ps.setInt(2, d.getServiceId());
            ps.setInt(3, d.getQuantity());
            ps.setDouble(4, d.getPriceAtBooking());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Cập nhật số lượng dịch vụ (hay dùng)
    public boolean updateQuantity(int detailId, int quantity) {
        String sql = "UPDATE BookingServices SET Quantity = ? WHERE DetailID = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, quantity);
            ps.setInt(2, detailId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Xóa 1 dịch vụ khỏi Booking
    public boolean deleteServiceDetail(int detailId) {
        String sql = "DELETE FROM BookingServices WHERE DetailID = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, detailId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Xóa toàn bộ dịch vụ của 1 Booking (ít dùng vì đã có ON DELETE CASCADE)
    public boolean deleteByBookingId(int bookingId) {
        String sql = "DELETE FROM BookingServices WHERE BookingID = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
