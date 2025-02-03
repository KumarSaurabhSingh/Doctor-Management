package com.Application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Application.entity.PatientRequest;
import com.Application.service.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/history")
    public ResponseEntity<?> getPatientNotesHistory(@RequestBody PatientRequest patientRequest) {
        return patientService.getPatientNotesHistory(patientRequest.getName(), patientRequest.getContact());
    }
}

