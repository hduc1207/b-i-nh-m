package com.pethotel.dto;

public class PetDTO {
    private int petId;
    private String petName;
    private String petCode;
    private String customerCode;
    private String Species;
    private String breed;
    private double weight;
    private String healthStatus;
    private int customerId;

    public PetDTO() {}
    public PetDTO(int petId, String petName, String species, String breed, double weight, String healthStatus, int customerId) {
        this.petId = petId;
        this.petName = petName;
        this.Species = species;
        this.breed = breed;
        this.weight = weight;
        this.healthStatus = healthStatus;
        this.customerId = customerId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return Species;
    }

    public void setSpecies(String species) {
        this.Species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPetCode() {
        return petCode;
    }

    public void setPetCode(String petCode) {
        this.petCode = petCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}