package com.Application.controller;

import java.time.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Application.entity.Doctor;
import com.Application.entity.DoctorInfoDTO;
import com.Application.entity.DoctorLeave;
import com.Application.entity.DoctorLeaveRequest;
import com.Application.entity.DoctorSchedule;
import com.Application.repository.DoctorLeaveRepository;
import com.Application.repository.DoctorRepository;
import com.Application.service.DoctorService;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DoctorLeaveRepository doctorLeaveRepository;
    
    // Endpoint to create a new doctor
    @PostMapping("/create")
    public ResponseEntity<Object> createDoctor(@RequestBody Doctor newDoctor) {
    	try {
        return doctorService.createDoctor(newDoctor);
    	}catch(Exception e) {
    		Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
    	}
    }

    @GetMapping
    public ResponseEntity<Object> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Doctor not found for hospital.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Create a list of DoctorInfoDTO by mapping Doctor entities to DoctorInfoDTO
        List<DoctorInfoDTO> doctorInfoDTOList = doctors.stream()
                .map(this::convertToDoctorInfoDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(doctorInfoDTOList);
    }

    // Helper method to convert Doctor entity to DoctorInfoDTO
    private DoctorInfoDTO convertToDoctorInfoDTO(Doctor doctor) {
        DoctorInfoDTO doctorInfoDTO = new DoctorInfoDTO();
        doctorInfoDTO.setId(doctor.getId());
        doctorInfoDTO.setName(doctor.getName());
        doctorInfoDTO.setEmail(doctor.getEmail());
        doctorInfoDTO.setSpecialization(doctor.getSpecialization());
        return doctorInfoDTO;
    }
    @GetMapping("/search")
    public ResponseEntity<Object> searchDoctorsBySpecialization(@RequestParam String specialization) {
        // Perform the doctor search based on the provided specialization
    	try {
        List<Doctor> doctors = doctorService.searchDoctorsBySpecialization(specialization);

        if (doctors.isEmpty()) {
            // Return a response with status and message if no doctors match the specialization
        	Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "No Doctors found  matching the specialization "+ specialization);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
     // Create a list of DoctorInfoDTO by mapping Doctor entities to DoctorInfoDTO
        List<DoctorInfoDTO> doctorInfoDTOList = doctors.stream()
                .map(this::convertToDoctorInfoDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(doctorInfoDTOList);
    	}catch(Exception e) {
    		Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
    	}
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }
    
 // Endpoint to update doctor details
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateDoctorDetails(
            @PathVariable Long id,
            @RequestBody Doctor updatedDoctor
    ) {
        return doctorService.updateDoctorDetails(id, updatedDoctor);
    }

    // Endpoint to delete a doctor
    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable Long id) {
        return doctorService.deleteDoctor(id);
    }
    
 
    
    @PostMapping("/{id}/leave")
    public ResponseEntity<Object> addDoctorLeave(
            @PathVariable Long id, @RequestBody DoctorLeaveRequest leaveRequest) {
        Map<String, Object> response = new HashMap<>();

        // Check if the doctor exists with the provided ID
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (!optionalDoctor.isPresent()) {
            response.put("success", false);
            response.put("message", "Doctor not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Doctor doctor = optionalDoctor.get();
        LocalDate leaveDate = leaveRequest.getLeaveDate();
        LocalTime startTime = leaveRequest.getStartTime();
        LocalTime endTime = leaveRequest.getEndTime();

        // Check if the leave date is in the past
        if (leaveDate.isBefore(LocalDate.now())) {
            response.put("success", false);
            response.put("message", "Leave date cannot be in the past.");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if the leave date is in the doctor's weekly schedule
        boolean isLeaveDayInSchedule = doctor.getWeeklySchedule().stream()
                .anyMatch(schedule -> schedule.getDayOfWeek().equalsIgnoreCase(leaveDate.getDayOfWeek().toString()));

        if (!isLeaveDayInSchedule) {
            response.put("success", false);
            response.put("message", "Doctor is not scheduled to work on the specified leave date.");
            return ResponseEntity.badRequest().body(response);
        }

        if (startTime == null && endTime != null) {
            response.put("success", false);
            response.put("message", "You have entered only endTime, please enter start time also.");
            return ResponseEntity.badRequest().body(response);
        }

        if (startTime != null && endTime != null && endTime.isBefore(startTime)) {
            response.put("success", false);
            response.put("message", "End time cannot be before start time.");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if the doctor already has a leave entry for the specified leave date
        Optional<DoctorLeave> optionalLeaveEntry = doctorLeaveRepository.findByDoctorAndLeaveDate(doctor, leaveDate);
        if (optionalLeaveEntry.isPresent()) {
            // Update the existing leave entry
            DoctorLeave existingLeave = optionalLeaveEntry.get();
            existingLeave.setStartTime(startTime);
            existingLeave.setEndTime(endTime);
            doctorLeaveRepository.save(existingLeave);
    
        } else {
            // Create and save a new doctor leave entry
            DoctorLeave leave = new DoctorLeave();
            leave.setDoctor(doctor);
            leave.setLeaveDate(leaveDate);
            leave.setStartTime(startTime);
            leave.setEndTime(endTime);
            doctorLeaveRepository.save(leave);
            
        }

        response.put("DoctoId", doctor.getId());
        
        response.put("LeaveId", doctor.getId());
        response.put("LeaveDate", leaveDate.toString());
        response.put("StartTime", startTime != null ? startTime.toString() : "Not specified");
        response.put("EndTime", endTime != null ? endTime.toString() : "Not specified");
        
        

        return ResponseEntity.ok(response);
    }


    
    
    @PostMapping("/{id}/deleteLeave")
    public ResponseEntity<Object> deleteDoctorLeave(
            @PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate leaveDate) {
        Map<String, Object> response = new HashMap<>();
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (!optionalDoctor.isPresent()) {
            response.put("success", false);
            response.put("message", "Doctor not found with ID: " + id);
            return ResponseEntity.badRequest().body(response);
        }
        
        Doctor doctor = optionalDoctor.get();

        // Check if the doctor exists with the provided ID
        Optional<DoctorLeave> optionalDoctorLeave = doctorLeaveRepository.findByDoctorAndLeaveDate(doctor, leaveDate);
        if (!optionalDoctorLeave.isPresent()) {
            response.put("success", false);
            response.put("message", "Doctor leave not found for the specified date.");
            return ResponseEntity.badRequest().body(response);
        }

        

        // Check if the leave date is in the past
        if (leaveDate.isBefore(LocalDate.now())) {
            response.put("success", false);
            response.put("message", "Cannot delete the leaveDate as it is in the past.");
            return ResponseEntity.badRequest().body(response);
        }
        doctorLeaveRepository.delete(optionalDoctorLeave.get());

        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        Map<String, Object> response = doctorService.getDoctorAvailability(id, dateTime);
        return ResponseEntity.ok(response);
    }
        
    }
    // Other endpoints for CRUD operations or additional functionalities...

