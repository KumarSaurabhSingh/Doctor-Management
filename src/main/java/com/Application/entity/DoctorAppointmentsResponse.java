package com.Application.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DoctorAppointmentsResponse {
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private Map<String, List<String>> upcomingAppointments; // Group appointments by date

    // Constructor
    public DoctorAppointmentsResponse(Long doctorId, String doctorName, String specialization, Map<String, List<String>> upcomingAppointments) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.upcomingAppointments = upcomingAppointments;
    }

    // Getter for doctorId
    public Long getDoctorId() {
        return doctorId;
    }

    // Setter for doctorId
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    // Getter for doctorName
    public String getDoctorName() {
        return doctorName;
    }

    // Setter for doctorName
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    // Getter for specialization
    public String getSpecialization() {
        return specialization;
    }

    // Setter for specialization
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Getter for upcomingAppointments
    public Map<String, List<String>> getUpcomingAppointments() {
        return upcomingAppointments;
    }

    // Setter for upcomingAppointments
    public void setUpcomingAppointments(Map<String, List<String>> upcomingAppointments) {
        this.upcomingAppointments = upcomingAppointments;
    }

    // You can add other methods as needed...

    @Override
    public String toString() {
        return "DoctorAppointmentsResponse{" +
                "doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", specialization='" + specialization + '\'' +
                ", upcomingAppointments=" + upcomingAppointments +
                '}';
    }
    public void sortAppointments() {
        // Sort the dates in ascending order
        Map<String, List<String>> sortedAppointments = new TreeMap<>(upcomingAppointments);

        // Sort the appointment times within each date
        for (Map.Entry<String, List<String>> entry : sortedAppointments.entrySet()) {
            List<String> appointmentTimes = entry.getValue();
            Collections.sort(appointmentTimes);
        }

        // Update the upcomingAppointments with the sorted data
        upcomingAppointments = sortedAppointments;
    }
}
