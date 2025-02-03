package com.Application.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.Application.entity.Appointment;
import com.Application.entity.AppointmentRequest;
import com.Application.entity.Doctor;
import com.Application.repository.DoctorRepository;
import com.Application.service.AppointmentService;
import com.Application.service.DoctorService;


import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private DoctorService doctorService;
    
    
    
    

    // Endpoint to create an appointment for a specific doctor
    @PostMapping("/{id}/bookAppointments")
    public ResponseEntity<Object> createAppointmentForDoctor(
            @PathVariable Long id,
            @RequestBody AppointmentRequest appointmentRequest
    ) {
        return appointmentService.createAppointmentForDoctor(id, appointmentRequest);
    }

    @GetMapping("/{id}/appointments/upcoming")
    public ResponseEntity<Object> getUpcomingAppointmentsForDoctor(@PathVariable Long id) {
        ResponseEntity<Object> response = appointmentService.getUpcomingAppointmentsForDoctor(id);
        
        
        // If the response status is 2xx, return the response as-is
        return response;
    }
    
    
    // Other endpoints for CRUD operations or additional functionalities...
}
