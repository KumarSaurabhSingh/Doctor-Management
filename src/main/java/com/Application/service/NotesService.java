package com.Application.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.Application.entity.Doctor;
import com.Application.entity.Notes;
import com.Application.entity.NotesRequest;
import com.Application.entity.Patient;
import com.Application.repository.AppointmentRepository;
import com.Application.repository.DoctorRepository;
import com.Application.repository.NotesRepository;
import com.Application.repository.PatientRepository;

@Service
public class NotesService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotesRepository notesRepository;

 // Service method to add notes for a patient
    public ResponseEntity<Object> addNotesForPatient(Long doctorId, NotesRequest notesRequest) {
        Map<String, Object> response = new LinkedHashMap<>();
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if(doctor ==null) {
        	response.put("success", false);
            response.put("message", "Doctor not found with id:"+doctorId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (notesRequest.getName() == null || notesRequest.getContact() == null || notesRequest.getNotes() == null || notesRequest.getName().isBlank() || notesRequest.getContact().isBlank() || notesRequest.getNotes().isBlank()) {
            response.put("success", false);
            response.put("message", "Either of the input data is null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
     // Check if the patient exists with the provided name and contact
        Optional<Patient> optionalPatient = patientRepository.findByNameAndContact(
                notesRequest.getName(), notesRequest.getContact());

        if (!optionalPatient.isPresent()) {
            response.put("success", false);
            response.put("message", "Patient with name " + notesRequest.getName() +
                    " and contact " + notesRequest.getContact() + " does not exist.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Patient patient = optionalPatient.get();

        // Check if the lastVisitedDoctorId and doctorId match
        if (!patient.getLastVisitedDoctorId().equals(doctorId)) {
            response.put("success", false);
            response.put("message", "Last visited doctor ID does not match the provided doctor ID.");
            return ResponseEntity.badRequest().body(response);
        }

        // Get the latest appointment date and time for the patient and doctor from the appointment table
        LocalDateTime latestAppointmentDateTime = appointmentRepository.findLatestAppointmentDateTime(
                patient.getPatientId(), doctorId);

        // Check if the latest appointment date and time has already passed
        if (latestAppointmentDateTime.isAfter(LocalDateTime.now())) {
            response.put("success", false);
            response.put("message", "Notes can only be added after the appointment has taken place.");
            return ResponseEntity.badRequest().body(response);
        }

        // Create the notes entity and save it to the database
        Notes newNotes = new Notes(patient, doctorRepository.findById(doctorId).get(),
                latestAppointmentDateTime, notesRequest.getNotes());

        Notes savedNotes = notesRepository.save(newNotes);

        // Build the desired response format
        Map<String, Object> patientDetails = new LinkedHashMap<>();
        patientDetails.put("patientId", patient.getPatientId());
        patientDetails.put("name", patient.getName());
        patientDetails.put("contact", patient.getContact());

        Map<String, Object> doctorDetails = new LinkedHashMap<>();
        
        if (doctor != null) {
            doctorDetails.put("id", doctor.getId());
            doctorDetails.put("name", doctor.getName());
            doctorDetails.put("email", doctor.getEmail());
            doctorDetails.put("specialization", doctor.getSpecialization());
        }

        Map<String, Object> responseNotes = new LinkedHashMap<>();
        responseNotes.put("noteId", savedNotes.getNoteId());
        responseNotes.put("PatientId", patient.getPatientId());
        responseNotes.put("PatientName", patient.getName());
        responseNotes.put("PatientContact", patient.getContact());
        responseNotes.put("DoctorId", doctor.getId());
        responseNotes.put("DoctorName", doctor.getName());
        responseNotes.put("DoctorEmail", doctor.getEmail());
        responseNotes.put("DoctorSpecialization", doctor.getSpecialization());
        responseNotes.put("visitedDateTime", savedNotes.getVisitedDateTime());
        responseNotes.put("notes", savedNotes.getNotes());

        
        

        return ResponseEntity.ok(responseNotes);
    }


}
