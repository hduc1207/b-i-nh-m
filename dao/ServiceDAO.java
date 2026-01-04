package com.pethotel.dao;

import com.pethotel.dto.ServiceDTO;
import com.pethotel.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    // 1. Lấy tất cả dịch vụ
    public List<ServiceDTO> getAllServices() {
        List<ServiceDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Services";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ServiceDTO s = new ServiceDTO();
                s.setServiceId(rs.getInt("ServiceID"));
                s.setServiceName(rs.getString("ServiceName"));
                s.setPrice(rs.getDouble("Price"));
                s.setUnit(rs.getString("Unit"));

                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy dịch vụ theo ID
    public ServiceDTO getServiceById(int serviceId) {
        String sql = "SELECT * FROM Services WHERE ServiceID = ?";
        ServiceDTO s = null;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, serviceId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                s = new ServiceDTO();
                s.setServiceId(rs.getInt("ServiceID"));
                s.setServiceName(rs.getString("ServiceName"));
                s.setPrice(rs.getDouble("Price"));
                s.setUnit(rs.getString("Unit"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    // 3. Thêm dịch vụ mới
    public boolean insertService(ServiceDTO s) {
        String sql = """
            INSERT INTO Services (ServiceName, Price, Unit)
            VALUES (?, ?, ?)
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, s.getServiceName());
            ps.setDouble(2, s.getPrice());
            ps.setString(3, s.getUnit());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật dịch vụ
    public boolean updateService(ServiceDTO s) {
        String sql = """
            UPDATE Services SET
                ServiceName = ?,
                Price = ?,
                Unit = ?
            WHERE ServiceID = ?
        """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, s.getServiceName());
            ps.setDouble(2, s.getPrice());
            ps.setString(3, s.getUnit());
            ps.setInt(4, s.getServiceId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Xóa dịch vụ
    public boolean deleteService(int serviceId) {
        String sql = "DELETE FROM Services WHERE ServiceID = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, serviceId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
