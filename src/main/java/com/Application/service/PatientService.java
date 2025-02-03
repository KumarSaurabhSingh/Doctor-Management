package com.Application.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Application.entity.Doctor;
import com.Application.entity.Notes;
import com.Application.entity.Patient;
import com.Application.repository.NotesRepository;
import com.Application.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private NotesRepository notesRepository;

    public ResponseEntity<?> getPatientNotesHistory(String name, String contact) {
        Patient patient = patientRepository.findByNameAndContact(name, contact).orElse(null);

        if (patient == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Patient with name " + name + " and contact " + contact + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<Notes> notesList = notesRepository.findByPatient(patient);

//        if (notesList.isEmpty()) {
//            Map<String, String> response = new HashMap<>();
//            response.put("status", "failure");
//            response.put("message", "No notes  found for patient with name " + name + " and contact " + contact + ".");
//            return ResponseEntity.ok(response);
//        }

     // Create the custom response object
        Map<String, Object> response = new LinkedHashMap<>();

        // Add patient details to the response
        
        response.put("PatientId", patient.getPatientId());
        response.put("PatientName", patient.getName());
        response.put("PatientContact", patient.getContact());
        

        // Create a list to hold doctor visit history
        List<Map<String, Object>> historyList = new ArrayList<>();

        // Create a map to hold notes for each doctor
        Map<Doctor, List<Map<String, Object>>> doctorNotesMap = new HashMap<>();

        // Group notes by doctor
        for (Notes notes : notesList) {
            Doctor doctor = notes.getDoctor();
            Map<String, Object> visit = new LinkedHashMap<>();
            visit.put("notes", notes.getNotes());
            visit.put("visitedDateTime", notes.getVisitedDateTime());

            if (doctorNotesMap.containsKey(doctor)) {
                doctorNotesMap.get(doctor).add(visit);
            } else {
                List<Map<String, Object>> doctorNotesList = new ArrayList<>();
                doctorNotesList.add(visit);
                doctorNotesMap.put(doctor, doctorNotesList);
            }
        }

        // Add each doctor's details and notes to the history list
        for (Map.Entry<Doctor, List<Map<String, Object>>> entry : doctorNotesMap.entrySet()) {
            
            Doctor doctor = entry.getKey();
            

            Map<String, Object> doctorVisit = new HashMap<>();
            doctorVisit.put("DoctorId", doctor.getId());
            doctorVisit.put("DoctorName", doctor.getName());
            doctorVisit.put("DoctorEmail", doctor.getEmail());
            doctorVisit.put("DoctorSpecialization", doctor.getSpecialization());
            doctorVisit.put("noteList", entry.getValue());

            historyList.add(doctorVisit);
        }

        
        // Add history list to the response
        response.put("history", historyList);
        
        
        return ResponseEntity.ok(response);
        
    }
}
