package com.pethotel.bus;

import com.pethotel.dao.ServiceDAO;
import com.pethotel.dto.ServiceDTO;
import java.util.List;

public class ServiceBUS {
    private ServiceDAO serviceDAO;

    public ServiceBUS() {
        this.serviceDAO = new ServiceDAO();
    }

    public List<ServiceDTO> getAllServices() {
        return serviceDAO.getAllServices();
    }

    public String addService(ServiceDTO s) {
        if (s.getServiceName() == null || s.getServiceName().isEmpty()) {
            return "Tên dịch vụ không được để trống!";
        }
        if (s.getPrice() < 0) {
            return "Giá dịch vụ không được âm!";
        }

        if (serviceDAO.insertService(s)) {
            return "Thêm dịch vụ thành công!";
        }
        return "Thêm dịch vụ thất bại!";
    }

    public String updateService(ServiceDTO s) {
        if (serviceDAO.updateService(s)) {
            return "Cập nhật dịch vụ thành công!";
        }
        return "Cập nhật thất bại!";
    }

    public String deleteService(int id) {
        if (serviceDAO.deleteService(id)) {
            return "Xóa dịch vụ thành công!";
        }
        return "Xóa thất bại!";
    }

    public ServiceDTO getServiceById(int id) {
        return serviceDAO.getServiceById(id);
    }
}