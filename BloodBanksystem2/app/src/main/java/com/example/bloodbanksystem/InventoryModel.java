package com.example.bloodbanksystem;

public class InventoryModel {
    private String id;
    private String bloodGroup;
    private int quantity;
    private String dateOfCollection;
    private String dateOfExpiration;

    public InventoryModel() {
        // Default constructor required for Firebase
    }

    public InventoryModel(String id, String bloodGroup, int quantity, String dateOfCollection, String dateOfExpiration) {
        this.id = id;
        this.bloodGroup = bloodGroup;
        this.quantity = quantity;
        this.dateOfCollection = dateOfCollection;
        this.dateOfExpiration = dateOfExpiration;
    }

    public String getId() { return id; }
    public String getBloodGroup() { return bloodGroup; }
    public int getQuantity() { return quantity; }
    public String getDateOfCollection() { return dateOfCollection; }
    public String getDateOfExpiration() { return dateOfExpiration; }

    public void setId(String id) { this.id = id; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setDateOfCollection(String dateOfCollection) { this.dateOfCollection = dateOfCollection; }
    public void setDateOfExpiration(String dateOfExpiration) { this.dateOfExpiration = dateOfExpiration; }
}
