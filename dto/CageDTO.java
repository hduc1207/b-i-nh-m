package com.pethotel.dto;

public class CageDTO {
    private int cageId;
    private String cageName;
    private String type;
    private double pricePerDay;
    private String status;

    public CageDTO() {
    }

    public CageDTO(int cageId, String cageName, String type, double pricePerDay, String status) {
        this.cageId = cageId;
        this.cageName = cageName;
        this.type = type;
        this.pricePerDay = pricePerDay;
        this.status = status;
    }

    public int getCageId() {
        return cageId;
    }

    public void setCageId(int cageId) {
        this.cageId = cageId;
    }

    public String getCageName() {
        return cageName;
    }

    public void setCageName(String cageName) {
        this.cageName = cageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}