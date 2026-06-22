package com.example.a1221858_1220137_courseproject;

import androidx.annotation.NonNull;

public class Trip {
    private int id;
    private String destination;
    private String country;
    private int durationDays;
    private double price;
    private String description;
    private double rating;
    private String imageUrl;

    public Trip() {}

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
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

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public int getDurationDays() {
        return durationDays;
    }
    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public void setDurationF(String duration) {
        if (duration == null || duration.isEmpty()) {
            this.durationDays = 0;
            return;
        }
        try {
            String numericPart = duration.replaceAll("[^0-9]", "");
            this.durationDays = numericPart.isEmpty() ? 0 : Integer.parseInt(numericPart);
        } catch (NumberFormatException e) {
            this.durationDays = 0;
        }
    }

    public String getDuration() {
        return durationDays + " Days";
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

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    @NonNull
    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", country='" + country + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                '}';
    }
}
