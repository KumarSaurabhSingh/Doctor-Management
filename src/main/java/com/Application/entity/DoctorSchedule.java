package com.Application.entity;


import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.*;

@Embeddable
public class DoctorSchedule {
	private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructors
    public DoctorSchedule() {
    }

    public DoctorSchedule(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return "DoctorSchedule{" +
                "dayOfWeek=" + dayOfWeek +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    // Other methods...
}
