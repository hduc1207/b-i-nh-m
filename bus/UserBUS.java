package com.pethotel.bus;

import com.pethotel.dao.UserDAO;
import com.pethotel.dto.UserDTO;
import java.util.List;

public class UserBUS {
    private UserDAO userDAO;

    public UserBUS() {
        this.userDAO = new UserDAO();
    }

    public List<UserDTO> getAllUsers() {
        return userDAO.getAll();
    }

    public String addUser(UserDTO u) {
        if (u.getUsername().isEmpty() || u.getPassword().isEmpty()) {
            return "Username và Password không được để trống!";
        }
        // Kiểm tra logic nghiệp vụ khác
        if (userDAO.insert(u)) {
            return "Thêm tài khoản thành công!";
        }
        return "Thêm thất bại!";
    }

    public String updateUser(UserDTO u) {
        if (userDAO.update(u)) {
            return "Cập nhật tài khoản thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String deleteUser(int id) {
        if (userDAO.delete(id)) {
            return "Xóa tài khoản thành công!";
        }
        return "Xóa thất bại!";
    }
}