package com.pethotel.dao;

import com.pethotel.dto.PetDTO;
import com.pethotel.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    // 1.Lấy tất cả Pet + Mã KH
    public List<PetDTO> getAllPets() {
        List<PetDTO> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CustomerCode " +
                "FROM Pets p " +
                "LEFT JOIN Customers c ON p.CustomerID = c.CustomerID";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                PetDTO pet = new PetDTO();
                pet.setPetId(rs.getInt("PetID"));
                pet.setPetCode(rs.getString("PetCode"));
                pet.setPetName(rs.getString("PetName"));
                pet.setSpecies(rs.getString("Species"));
                pet.setBreed(rs.getString("Breed"));
                pet.setWeight(rs.getDouble("Weight"));
                pet.setHealthStatus(rs.getString("HealthStatus"));
                pet.setCustomerId(rs.getInt("CustomerID"));
                pet.setCustomerCode(rs.getString("CustomerCode"));

                list.add(pet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2.Lấy Pet theo CustomerID
    public List<PetDTO> getPetsByCustomerId(int customerId) {
        List<PetDTO> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CustomerCode " +
                "FROM Pets p " +
                "LEFT JOIN Customers c ON p.CustomerID = c.CustomerID " +
                "WHERE p.CustomerID = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PetDTO pet = new PetDTO();
                pet.setPetId(rs.getInt("PetID"));
                pet.setPetCode(rs.getString("PetCode")); // Lấy mã TC
                pet.setPetName(rs.getString("PetName"));
                pet.setSpecies(rs.getString("Species"));
                pet.setBreed(rs.getString("Breed"));
                pet.setWeight(rs.getDouble("Weight"));
                pet.setHealthStatus(rs.getString("HealthStatus"));
                pet.setCustomerId(rs.getInt("CustomerID"));
                pet.setCustomerCode(rs.getString("CustomerCode")); // Lấy mã KH

                list.add(pet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Thêm Pet mới
    public boolean insertPet(PetDTO pet) {
        String sql = """
                INSERT INTO Pets (PetName, Species, Breed, Weight, HealthStatus, CustomerID)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, pet.getPetName());
            ps.setString(2, pet.getSpecies());
            ps.setString(3, pet.getBreed());
            ps.setDouble(4, pet.getWeight());
            ps.setString(5, pet.getHealthStatus());
            ps.setInt(6, pet.getCustomerId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật Pet
    public boolean updatePet(PetDTO pet) {
        String sql = """
                UPDATE Pets
                SET PetName = ?, Species = ?, Breed = ?, Weight = ?, HealthStatus = ?, CustomerID = ?
                WHERE PetID = ?
                """;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, pet.getPetName());
            ps.setString(2, pet.getSpecies());
            ps.setString(3, pet.getBreed());
            ps.setDouble(4, pet.getWeight());
            ps.setString(5, pet.getHealthStatus());
            ps.setInt(6, pet.getCustomerId());
            ps.setInt(7, pet.getPetId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Xóa Pet
    public boolean deletePet(int petId) {
        String sql = "DELETE FROM Pets WHERE PetID = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, petId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6.Lấy Pet theo ID
    public PetDTO getPetById(int petId) {
        String sql = "SELECT * FROM Pets WHERE PetID = ?";
        PetDTO pet = null;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, petId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pet = new PetDTO();
                pet.setPetId(rs.getInt("PetID"));
                pet.setPetCode(rs.getString("PetCode"));
                pet.setPetName(rs.getString("PetName"));
                pet.setSpecies(rs.getString("Species"));
                pet.setBreed(rs.getString("Breed"));
                pet.setWeight(rs.getDouble("Weight"));
                pet.setHealthStatus(rs.getString("HealthStatus"));
                pet.setCustomerId(rs.getInt("CustomerID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pet;
    }
}