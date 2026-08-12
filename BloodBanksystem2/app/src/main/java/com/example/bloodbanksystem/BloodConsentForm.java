package com.example.bloodbanksystem;

public class BloodConsentForm {
    private String fullName, Phonenumber, bloodGroup, donatedPreviously, discomfortAfterDonation, healthIssues;

    // Empty constructor required for Firebase
    public BloodConsentForm() {}

    public BloodConsentForm(String fullName, String date, String bloodGroup, String donatedPreviously, String discomfortAfterDonation, String healthIssues) {
        this.fullName = fullName;
        this.Phonenumber = Phonenumber;
        this.bloodGroup = bloodGroup;
        this.donatedPreviously = donatedPreviously;
        this.discomfortAfterDonation = discomfortAfterDonation;
        this.healthIssues = healthIssues;
    }

    // Getters (Required for Firebase)
    public String getFullName() { return fullName; }
    public String getPhonenumber() { return Phonenumber; }
    public String getBloodGroup() { return bloodGroup; }
    public String getDonatedPreviously() { return donatedPreviously; }
    public String getDiscomfortAfterDonation() { return discomfortAfterDonation; }
    public String getHealthIssues() { return healthIssues; }
}