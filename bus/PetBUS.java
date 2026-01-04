package com.pethotel.bus;

import com.pethotel.dao.PetDAO;
import com.pethotel.dto.PetDTO;
import java.util.List;

public class PetBUS {
    private PetDAO petDAO;

    public PetBUS() {
        this.petDAO = new PetDAO();
    }

    public List<PetDTO> getAllPets() {
        return petDAO.getAllPets();
    }

    public String addPet(PetDTO p) {
        if (p.getPetName() == null || p.getPetName().isEmpty()) {
            return "Tên thú cưng không được để trống!";
        }
        if (p.getCustomerId() <= 0) {
            return "Thú cưng phải thuộc về một khách hàng hợp lệ!";
        }

        if (petDAO.insertPet(p)) {
            return "Thêm thú cưng thành công!";
        }
        return "Thêm thú cưng thất bại!";
    }

    public String updatePet(PetDTO p) {
        if (petDAO.updatePet(p)) {
            return "Cập nhật thú cưng thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String deletePet(int id) {
        if (petDAO.deletePet(id)) {
            return "Xóa thú cưng thành công!";
        }
        return "Xóa thất bại!";
    }

    public PetDTO getPetById(int id) {
        return petDAO.getPetById(id);
    }
}