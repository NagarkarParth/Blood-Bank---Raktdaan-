package com.example.bloodbanksystem;

public class BloodBank {

    private String id; // Unique ID for Firebase reference
    private String bloodBankName, contactPerson, phoneNumber, email, address;
    private String password; // 🔐 New field for authentication

    // Empty constructor required for Firebase
    public BloodBank() {
    }

    public BloodBank(String id, String bloodBankName, String contactPerson, String phoneNumber, String email, String address, String password) {
        this.id = id;
        this.bloodBankName = bloodBankName;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getBloodBankName() {
        return bloodBankName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setBloodBankName(String bloodBankName) {
        this.bloodBankName = bloodBankName;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString method for easy debugging
    @Override
    public String toString() {
        return "BloodBank{" +
                "id='" + id + '\'' +
                ", bloodBankName='" + bloodBankName + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", password=''" + // hiding actual password for safety
                '}';
    }
}