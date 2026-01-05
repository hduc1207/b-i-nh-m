package com.pethotel.dao;

import com.pethotel.dto.BookingDTO;
import com.pethotel.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // 1. Lấy tất cả Booking
    public List<BookingDTO> getAllBookings() {
        List<BookingDTO> list = new ArrayList<>();
        String sql = """
                SELECT b.*, c.FullName, p.PetName, cg.CageName 
                FROM Bookings b
                LEFT JOIN Customers c ON b.CustomerID = c.CustomerID
                LEFT JOIN Pets p ON b.PetID = p.PetID
                LEFT JOIN Cages cg ON b.CageID = cg.CageID
                ORDER BY b.BookingID DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BookingDTO b = new BookingDTO();
                b.setBookingId(rs.getInt("BookingID"));
                b.setCustomerId(rs.getInt("CustomerID"));
                b.setPetId(rs.getInt("PetID"));
                b.setCageId(rs.getInt("CageID"));
                b.setCheckInDate(rs.getTimestamp("CheckInDate"));
                b.setCheckOutDate(rs.getTimestamp("CheckOutDate"));
                b.setStatus(rs.getString("Status"));
                b.setPaymentStatus(rs.getString("PaymentStatus"));
                b.setTotalPrice(rs.getDouble("TotalPrice"));
                b.setCreatedDate(rs.getTimestamp("CreatedDate"));
                b.setCustomerName(rs.getString("FullName")); // Lấy tên khách
                b.setPetName(rs.getString("PetName"));       // Lấy tên thú cưng
                b.setCageName(rs.getString("CageName"));     // Lấy tên chuồng

                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm Booking
    public boolean insertBooking(BookingDTO b) {
        String sql = """
            INSERT INTO Bookings(
                CustomerID, PetID, CageID,
                CheckInDate, CheckOutDate,
                Status, PaymentStatus, TotalPrice
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, b.getCustomerId());
            ps.setInt(2, b.getPetId());
            ps.setInt(3, b.getCageId());
            ps.setTimestamp(4, b.getCheckInDate());

            if (b.getCheckOutDate() != null) {
                ps.setTimestamp(5, b.getCheckOutDate());
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            ps.setString(6, b.getStatus());
            ps.setString(7, b.getPaymentStatus());
            ps.setDouble(8, b.getTotalPrice());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Cập nhật Booking
    public boolean updateBooking(BookingDTO b) {
        String sql = """
            UPDATE Bookings SET
                CustomerID = ?, PetID = ?, CageID = ?,
                CheckInDate = ?, CheckOutDate = ?,
                Status = ?, PaymentStatus = ?, TotalPrice = ?
            WHERE BookingID = ?
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, b.getCustomerId());
            ps.setInt(2, b.getPetId());
            ps.setInt(3, b.getCageId());
            ps.setTimestamp(4, b.getCheckInDate());

            if (b.getCheckOutDate() != null) {
                ps.setTimestamp(5, b.getCheckOutDate());
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            ps.setString(6, b.getStatus());
            ps.setString(7, b.getPaymentStatus());
            ps.setDouble(8, b.getTotalPrice());
            ps.setInt(9, b.getBookingId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Xóa Booking
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM Bookings WHERE BookingID = ?";

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

    // 5. Lấy Booking theo ID
    public BookingDTO getBookingById(int bookingId) {
        String sql = "SELECT * FROM Bookings WHERE BookingID = ?";
        BookingDTO booking = null;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                booking = new BookingDTO();
                booking.setBookingId(rs.getInt("BookingID"));
                booking.setCustomerId(rs.getInt("CustomerID"));
                booking.setPetId(rs.getInt("PetID"));
                booking.setCageId(rs.getInt("CageID"));
                booking.setCheckInDate(rs.getTimestamp("CheckInDate"));
                booking.setCheckOutDate(rs.getTimestamp("CheckOutDate"));
                booking.setStatus(rs.getString("Status"));
                booking.setPaymentStatus(rs.getString("PaymentStatus"));
                booking.setTotalPrice(rs.getDouble("TotalPrice"));
                booking.setCreatedDate(rs.getTimestamp("CreatedDate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return booking;
    }
    public List<BookingDTO> getRevenueByDate(java.sql.Timestamp from, java.sql.Timestamp to) {
        List<BookingDTO> list = new ArrayList<>();
        // Lọc theo CheckInDate và chỉ lấy đơn 'Paid'
        String sql = "SELECT * FROM Bookings WHERE CheckInDate BETWEEN ? AND ? AND PaymentStatus = 'Paid'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, from);
            ps.setTimestamp(2, to);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BookingDTO b = new BookingDTO();
                b.setBookingId(rs.getInt("BookingID"));
                b.setCheckInDate(rs.getTimestamp("CheckInDate"));
                b.setTotalPrice(rs.getDouble("TotalPrice"));
                b.setPaymentStatus(rs.getString("PaymentStatus"));
                // Set các trường chuỗi rỗng để tránh lỗi null khi hiển thị
                b.setCustomerName(""); b.setPetName(""); b.setCageName(""); b.setStatus("");
                list.add(b);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
