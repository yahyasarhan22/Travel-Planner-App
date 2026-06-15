package com.example.a1221858_1220137_courseproject;

import androidx.annotation.NonNull;

public class Trip {
    private int id;
    private String destination;
    private String duration;
    private double price;
    private String description;

    // Constructors
    public Trip() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Trip(int id, String destination, String duration, double price, String description) {
        this.id = id;
        this.destination = destination;
        this.duration = duration;
        this.price = price;
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", price=" + price +
                '}';
    }
}