package com.Application.entity;

import java.time.LocalDateTime;

public class AppointmentRequest {
    private LocalDateTime dateTime;
    
    private String name;
    
    private String contact;

    // Default constructor (required by Jackson for deserialization)
    public AppointmentRequest() {
    }

    // Constructor with dateTime parameter
    public AppointmentRequest(LocalDateTime dateTime, String name, String contact) {
        this.dateTime = dateTime;
        this.name=name;
        this.contact=contact;
    }

    // Getter and Setter for dateTime
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
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
}

