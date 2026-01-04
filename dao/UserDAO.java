package com.pethotel.dao;

import com.pethotel.utils.DBConnection;
import com.pethotel.dto.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // 1. Thêm user mới
    public boolean insert(UserDTO user) {
        String sql = """
                INSERT INTO Users (Username, Password, FullName, Role)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); // password đã hash
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Cập nhật user
    public boolean update(UserDTO user) {
        String sql = """
                UPDATE Users
                SET Username = ?, Password = ?, FullName = ?, Role = ?
                WHERE UserID = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Xóa user
    public boolean delete(int userId) {
        String sql = "DELETE FROM Users WHERE UserID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Lấy user theo ID
    public UserDTO getById(int userId) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        UserDTO user = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new UserDTO(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        rs.getString("Role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // 5. Lấy danh sách user
    public List<UserDTO> getAll() {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                UserDTO user = new UserDTO(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        rs.getString("Role")
                );
                list.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6. Đăng nhập (RẤT QUAN TRỌNG)
    public UserDTO login(String username, String passwordHash) {
        String sql = """
                SELECT * FROM Users
                WHERE Username = ? AND Password = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UserDTO(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        rs.getString("Role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
