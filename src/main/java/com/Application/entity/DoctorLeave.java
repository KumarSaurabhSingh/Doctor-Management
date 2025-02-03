package com.Application.entity;


import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor_leaves")
public class DoctorLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    

    @Column(name = "leave_date", nullable = false)
    private LocalDate leaveDate;

    @JsonIgnore
    @Column(name = "start_time")
    private LocalTime startTime;

    @JsonIgnore
    @Column(name = "end_time")
    private LocalTime endTime;

    public DoctorLeave() {
    }

    public DoctorLeave(Doctor doctor, LocalDate leaveDate, LocalTime startTime, LocalTime endTime) {
        this.doctor = doctor;
        this.leaveDate = leaveDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(LocalDate leaveDate) {
        this.leaveDate = leaveDate;
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
}

