package com.example.bloodbanksystem;

public class donor {
    private String name;
    private String phone;
    private String bloodGroup;
    private String email;
    private String fcmToken; // ✅ Added FCM Token

    // Default constructor (required for Firebase)
    public donor() {}

    // Constructor including FCM Token
    public donor(String name, String phone, String bloodGroup, String email, String fcmToken) {
        this.name = name;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.fcmToken = fcmToken;
    }

    // Getters
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getBloodGroup() { return bloodGroup; }
    public String getEmail() { return email; }
    public String getFcmToken() { return fcmToken; } // ✅ Getter for FCM Token

    // Setter for FCM Token (Optional, if needed)
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
}
