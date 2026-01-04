package com.pethotel.dao;

import com.pethotel.dto.CustomerDTO;
import com.pethotel.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // 1. Lấy danh sách tất cả Customer
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Customers";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                CustomerDTO customer = new CustomerDTO();
                customer.setCustomerId(rs.getInt("CustomerID"));
                customer.setCustomerCode(rs.getString("CustomerCode"));
                customer.setFullName(rs.getString("FullName"));
                customer.setPhoneNumber(rs.getString("PhoneNumber"));
                customer.setEmail(rs.getString("Email"));
                customer.setAddress(rs.getString("Address"));
                customer.setNote(rs.getString("Note"));

                list.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm Customer mới
    public boolean insertCustomer(CustomerDTO customer) {
        String sql = """
                INSERT INTO Customers (FullName, PhoneNumber, Email, Address, Note)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getNote());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Cập nhật Customer
    public boolean updateCustomer(CustomerDTO customer) {
        String sql = """
                UPDATE Customers
                SET FullName = ?, PhoneNumber = ?, Email = ?, Address = ?, Note = ?
                WHERE CustomerID = ?
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getPhoneNumber());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getNote());
            ps.setInt(6, customer.getCustomerId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Xóa Customer theo ID
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customers WHERE CustomerID = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Tìm Customer theo ID
    public CustomerDTO getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customers WHERE CustomerID = ?";
        CustomerDTO customer = null;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                customer = new CustomerDTO();
                customer.setCustomerId(rs.getInt("CustomerID"));
                customer.setFullName(rs.getString("FullName"));
                customer.setPhoneNumber(rs.getString("PhoneNumber"));
                customer.setEmail(rs.getString("Email"));
                customer.setAddress(rs.getString("Address"));
                customer.setNote(rs.getString("Note"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }
}
