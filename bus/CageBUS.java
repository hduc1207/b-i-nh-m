package com.pethotel.bus;

import com.pethotel.dao.CageDAO;
import com.pethotel.dto.CageDTO;
import java.util.List;

public class CageBUS {
    private CageDAO cageDAO;

    public CageBUS() {
        this.cageDAO = new CageDAO();
    }

    public List<CageDTO> getAllCages() {
        return cageDAO.getAllCages();
    }


    public List<CageDTO> getAvailableCages() {
        return cageDAO.getCagesByStatus("Trống");
    }

    public String addCage(CageDTO c) {
        if (c.getCageName() == null || c.getCageName().isEmpty()) {
            return "Tên chuồng không được để trống!";
        }
        if (cageDAO.insertCage(c)) {
            return "Thêm chuồng thành công!";
        }
        return "Thêm chuồng thất bại!";
    }

    public String updateCage(CageDTO c) {
        if (cageDAO.updateCage(c)) {
            return "Cập nhật chuồng thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String deleteCage(int id) {
        if (cageDAO.deleteCage(id)) {
            return "Xóa chuồng thành công!";
        }
        return "Xóa thất bại!";
    }

    public CageDTO getCageById(int id) {
        return cageDAO.getCageById(id);
    }
}