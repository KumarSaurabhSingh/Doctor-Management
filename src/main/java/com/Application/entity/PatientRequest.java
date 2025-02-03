package com.Application.entity;

public class PatientRequest {
    private String name;
    private String contact;

    // Constructors
    public PatientRequest() {
        // Empty constructor
    }

    public PatientRequest(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // You can override the toString() method or add other methods as needed
}

