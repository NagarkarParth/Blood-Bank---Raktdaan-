package com.example.bloodbanksystem;

import java.io.Serializable;

public class BloodCamp implements Serializable { // Implements Serializable for easy data transfer
    private String name;
    private String email;
    private String phone;
    private String address;
    private String campDate;
    private String imageUrl; // New field for storing image URL

    // Default constructor required for Firebase
    public BloodCamp() { }

    // Constructor with image URL
    public BloodCamp(String name, String email, String phone, String address, String campDate, String imageUrl) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.campDate = campDate;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getCampDate() { return campDate; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setCampDate(String campDate) { this.campDate = campDate; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // ✅ Optional: Data Validation Methods
    public boolean isValid() {
        return name != null && !name.isEmpty() &&
                email != null && !email.isEmpty() &&
                phone != null && !phone.isEmpty() &&
                address != null && !address.isEmpty() &&
                campDate != null && !campDate.isEmpty();
    }
}
