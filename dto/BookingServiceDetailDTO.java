package com.pethotel.dto;

public class BookingServiceDetailDTO {
    private int detailId;
    private int bookingId;
    private int serviceId;
    private int quantity;
    private double priceAtBooking;

    public BookingServiceDetailDTO() {
    }

    public BookingServiceDetailDTO(int detailId, int bookingId, int serviceId, int quantity, double priceAtBooking) {
        this.detailId = detailId;
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.priceAtBooking = priceAtBooking;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtBooking() {
        return priceAtBooking;
    }

    public void setPriceAtBooking(double priceAtBooking) {
        this.priceAtBooking = priceAtBooking;
    }
}