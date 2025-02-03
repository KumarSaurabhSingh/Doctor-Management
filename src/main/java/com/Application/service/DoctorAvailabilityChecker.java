package com.Application.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.Application.entity.Doctor;
import com.Application.entity.DoctorSchedule;

public class DoctorAvailabilityChecker {

    // Method to check if a doctor is available at the given date and time
    public boolean isDoctorAvailable(Doctor doctor, DayOfWeek dayOfWeek, LocalTime appointmentTime) {
        List<DoctorSchedule> weeklySchedule = doctor.getWeeklySchedule();

        // Find the schedule for the requested day of the week
        DoctorSchedule scheduleForRequestedDay = null;
        for (DoctorSchedule schedule : weeklySchedule) {
            if (schedule.getDayOfWeek().equalsIgnoreCase(dayOfWeek.toString())) {
                scheduleForRequestedDay = schedule;
                break;
            }
        }

        // If the doctor doesn't work on the requested day, they are not available
        if (scheduleForRequestedDay == null) {
            return false;
        }

        // Check if the requested time falls within the doctor's working hours
        LocalTime startTime = scheduleForRequestedDay.getStartTime();
        LocalTime endTime = scheduleForRequestedDay.getEndTime();

        return !appointmentTime.isBefore(startTime) && !appointmentTime.isAfter(endTime);
    }
    

    public boolean isDoctorAvailable(Doctor doctor, LocalDate date) {
        List<DoctorSchedule> weeklySchedule = doctor.getWeeklySchedule();

        // Get the day of the week from the specified date
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // Check if the doctor has a schedule for the specified day of the week
        for (DoctorSchedule schedule : weeklySchedule) {
            if (schedule.getDayOfWeek().equalsIgnoreCase(dayOfWeek.toString())) {
                return true; // The doctor is available on the specified day
            }
        }

        return false; // The doctor is not available on the specified day
    }
}

