package com.pethotel.dto;

public class ServiceDTO {
    private int serviceId;
    private String serviceName;
    private double price;
    private String unit;

    public ServiceDTO() {
    }

    public ServiceDTO(int serviceId, String serviceName, double price, String unit) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
        this.unit = unit;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}