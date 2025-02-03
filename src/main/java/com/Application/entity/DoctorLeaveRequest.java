package com.Application.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorLeaveRequest {
    private LocalDate leaveDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public DoctorLeaveRequest() {
    }

    public DoctorLeaveRequest(LocalDate leaveDate, LocalTime startTime, LocalTime endTime) {
        this.leaveDate = leaveDate;
        this.startTime = startTime;
        this.endTime = endTime;
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
