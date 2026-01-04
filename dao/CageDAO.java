package com.pethotel.dao;

import com.pethotel.dto.CageDTO;
import com.pethotel.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CageDAO {

    // 1. Lấy tất cả chuồng
    public List<CageDTO> getAllCages() {
        List<CageDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Cages";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToCage(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lọc chuồng theo trạng thái
    public List<CageDTO> getCagesByStatus(String status) {
        List<CageDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Cages WHERE Status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToCage(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Thêm chuồng mới
    public boolean insertCage(CageDTO c) {
        String sql = "INSERT INTO Cages (CageName, Type, PricePerDay, Status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCageName());
            ps.setString(2, c.getType());
            ps.setDouble(3, c.getPricePerDay());
            ps.setString(4, c.getStatus());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật chuồng
    public boolean updateCage(CageDTO c) {
        String sql = "UPDATE Cages SET CageName=?, Type=?, PricePerDay=?, Status=? WHERE CageId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCageName());
            ps.setString(2, c.getType());
            ps.setDouble(3, c.getPricePerDay());
            ps.setString(4, c.getStatus());
            ps.setInt(5, c.getCageId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Xóa chuồng
    public boolean deleteCage(int id) {
        String sql = "DELETE FROM Cages WHERE CageId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. Lấy chuồng theo ID (Hỗ trợ hiển thị khi sửa)
    public CageDTO getCageById(int id) {
        String sql = "SELECT * FROM Cages WHERE CageId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCage(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm phụ trợ mapping để code gọn hơn
    private CageDTO mapResultSetToCage(ResultSet rs) throws Exception {
        CageDTO c = new CageDTO();
        c.setCageId(rs.getInt("CageId"));
        c.setCageName(rs.getString("CageName"));
        c.setType(rs.getString("Type"));
        c.setPricePerDay(rs.getDouble("PricePerDay"));
        c.setStatus(rs.getString("Status"));
        return c;
    }
}