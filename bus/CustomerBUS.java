package com.pethotel.bus;

import com.pethotel.dao.CustomerDAO;
import com.pethotel.dto.CustomerDTO;
import java.util.List;

public class CustomerBUS {
    private CustomerDAO customerDAO;

    public CustomerBUS() {
        this.customerDAO = new CustomerDAO();
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public String addCustomer(CustomerDTO c) {
        if (c.getFullName() == null || c.getFullName().isEmpty()) {
            return "Tên khách hàng không được để trống!";
        }
        if (c.getPhoneNumber() == null || c.getPhoneNumber().isEmpty()) {
            return "Số điện thoại không được để trống!";
        }

        if (customerDAO.insertCustomer(c)) {
            return "Thêm khách hàng thành công!";
        }
        return "Thêm khách hàng thất bại!";
    }

    public String updateCustomer(CustomerDTO c) {
        if (customerDAO.updateCustomer(c)) {
            return "Cập nhật thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String deleteCustomer(int id) {
        if (customerDAO.deleteCustomer(id)) {
            return "Xóa thành công!";
        }
        return "Xóa thất bại!";
    }

    public CustomerDTO getCustomerById(int id) {
        return customerDAO.getCustomerById(id);
    }
}