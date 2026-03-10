package com.hotel.entity;

import jakarta.persistence.*;

@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roomNumber;
    private double foodTotal;
    private double roomRent;
    private double finalTotal;

    public Bill() {}

    public Bill(int roomNumber, double foodTotal, double roomRent, double finalTotal) {
        this.roomNumber = roomNumber;
        this.foodTotal = foodTotal;
        this.roomRent = roomRent;
        this.finalTotal = finalTotal;
    }

    public Long getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getFoodTotal() {
        return foodTotal;
    }

    public double getRoomRent() {
        return roomRent;
    }

    public double getFinalTotal() {
        return finalTotal;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setFoodTotal(double foodTotal) {
        this.foodTotal = foodTotal;
    }

    public void setRoomRent(double roomRent) {
        this.roomRent = roomRent;
    }

    public void setFinalTotal(double finalTotal) {
        this.finalTotal = finalTotal;
    }
}