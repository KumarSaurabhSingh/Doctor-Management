package com.Application.service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.*;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Application.entity.Appointment;
import com.Application.entity.AppointmentRequest;
import com.Application.entity.Doctor;
import com.Application.entity.DoctorAppointmentsResponse;
import com.Application.entity.DoctorLeave;
import com.Application.entity.DoctorSchedule;
import com.Application.entity.Patient;
import com.Application.repository.AppointmentRepository;
import com.Application.repository.DoctorLeaveRepository;
import com.Application.repository.DoctorRepository;
import com.Application.repository.PatientRepository;

@Service
public class AppointmentService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorLeaveRepository doctorLeaveRepository;

 // Service method to create an appointment for a specific doctor
    public ResponseEntity<Object> createAppointmentForDoctor(Long doctorId, AppointmentRequest appointmentRequest) {
        Map<String, Object> response = new LinkedHashMap<>();

        // Check if the doctor exists with the given ID
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (!optionalDoctor.isPresent()) {
            response.put("success",false);
            response.put("message", "Doctor not found with ID: " + doctorId);
            return ResponseEntity.badRequest().body(response);
        }
        
        if(appointmentRequest.getName()==null || appointmentRequest.getContact()==null ||appointmentRequest.getName().isBlank()||appointmentRequest.getContact().isBlank() ||
        	appointmentRequest.getDateTime()==null){
        	response.put("success",false);
            response.put("message", "Check the Input , as either of the input is empty or null" );
            return ResponseEntity.badRequest().body(response);
        }

        Doctor doctor = optionalDoctor.get();
        LocalDateTime appointmentDateTime = appointmentRequest.getDateTime();

        // Check if the appointment date and time are in the future
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            response.put("success",false);
            response.put("message", "Appointment date and time should be in the future.");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if the doctor is available on the specified date
        DoctorAvailabilityChecker availabilityChecker = new DoctorAvailabilityChecker();
        boolean isAvailable = availabilityChecker.isDoctorAvailable(doctor, appointmentDateTime.toLocalDate());

        if (!isAvailable) {
            response.put("success",false);
            response.put("message", "Doctor is not available on the specified date.");
            return ResponseEntity.badRequest().body(response);
        }

        // Get the working hours for the specified day from the doctor's weekly schedule
        DayOfWeek dayOfWeek = appointmentDateTime.getDayOfWeek();
        LocalTime startTime = null;
        LocalTime endTime = null;

        for (DoctorSchedule schedule : doctor.getWeeklySchedule()) {
            if (schedule.getDayOfWeek().equalsIgnoreCase(dayOfWeek.toString())) {
                startTime = schedule.getStartTime();
                endTime = schedule.getEndTime();
                break;
            }
        }

        if (startTime == null || endTime == null) {
            response.put("success",false);
            response.put("message", "Working hours for the doctor on the specified day not found.");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if the appointment time is within the doctor's working hours
        LocalTime appointmentTime = appointmentDateTime.toLocalTime();

        if (appointmentTime.isBefore(startTime) || appointmentTime.plusMinutes(30).isAfter(endTime)) {
            response.put("success",false);
            response.put("message", "Appointments are available from " + startTime + " to " + endTime +
                    ", and each appointment lasts for 30 minutes.");
            return ResponseEntity.badRequest().body(response);
        }

       
        
        Map<String, Object> availabilityStatus = checkDoctorAvailability(doctor, appointmentDateTime);

        if (!(boolean) availabilityStatus.get("availability")) {
            response.put("success",false);
            response.put("message", (String) availabilityStatus.get("message"));
            return ResponseEntity.badRequest().body(response);
        }

     // Check if the patient exists with the provided contact information
        String contact = appointmentRequest.getContact();
        Optional<Patient> optionalPatient = patientRepository.findByContact(contact);

        // Create or update patient based on contact information
        Patient patient;
        if (optionalPatient.isPresent()) {
            // Patient with contact already exists
            patient = optionalPatient.get();
            if (!patient.getName().equalsIgnoreCase(appointmentRequest.getName())) {
                // Patient name does not match, create a new patient entry
                patient = new Patient();
                patient.setName(appointmentRequest.getName());
                patient.setContact(contact);
                patient = patientRepository.save(patient);
            }
        } else {
            // Patient with contact does not exist, create a new patient entry
            patient = new Patient();
            patient.setName(appointmentRequest.getName());
            patient.setContact(contact);
            patient = patientRepository.save(patient);
        }

        // Update lastVisitedDoctorId for the patient
        patient.setLastVisitedDoctorId(doctorId);
        patientRepository.save(patient);
        
 List<Appointment> existingAppointments1 = appointmentRepository.findAllByPatientAndDateTime(patient, appointmentDateTime);
        
        for (Appointment appointment : existingAppointments1) {
            if (appointment.getDoctor().getId().equals(doctorId)) {
                response.put("success",false);
                response.put("message", "Patient already has an appointment  on the specified date and time.");
                return ResponseEntity.badRequest().body(response);
            }
        }

        // Check if the doctor has an available slot for the given appointment time
        LocalDateTime endDateTime = appointmentDateTime.plusMinutes(30);

        List<Appointment> existingAppointments = appointmentRepository.findAllByDoctorIdAndDateTimeBetween(
                doctorId, appointmentDateTime, endDateTime);

        if (!existingAppointments.isEmpty()) {
            response.put("success",false);
            response.put("message", "The slot  is booked for the given  time for the appointment with the doctor.Please choose other slot for the date");
            return ResponseEntity.badRequest().body(response);
        }
        // Add your appointment booking logic here
        // ...

        // Create the appointment entity and save it to the database
        Appointment newAppointment = new Appointment(doctor, patient, appointmentDateTime);
        newAppointment = appointmentRepository.save(newAppointment);

      
        response.put("AppointmentId", newAppointment.getId());
        response.put("DoctorId", doctor.getId());
        response.put("DoctorName", doctor.getName());
        response.put("DoctorEmail", doctor.getEmail());
        response.put("DoctorSpecialization", doctor.getSpecialization());
        response.put("PatientId", patient.getPatientId());
        response.put("PatientName", patient.getName());
        response.put("PatientContact", patient.getContact());
        response.put("dateTime", newAppointment.getDateTime());

        return ResponseEntity.ok(response);
    }
   
   

    public Map<String, Object> checkDoctorAvailability(Doctor doctor, LocalDateTime appointmentDateTime) {
        LocalDate appointmentDate = appointmentDateTime.toLocalDate();
        LocalTime appointmentTime = appointmentDateTime.toLocalTime();

        Optional<DoctorLeave> optionalDoctorLeave = doctorLeaveRepository.findByDoctorAndLeaveDate(doctor, appointmentDate);
        if (optionalDoctorLeave.isPresent()) {
            DoctorLeave leave = optionalDoctorLeave.get();
            Map<String, Object> availabilityStatus = new HashMap<>();

            if (leave.getStartTime() == null && leave.getEndTime() == null) {
                // Whole day leave
                availabilityStatus.put("availability", false);
                availabilityStatus.put("message", "Doctor is on leave for the whole day.");
                return availabilityStatus;
            }

            if (leave.getStartTime() != null && leave.getEndTime() != null) {
                // Leave with start and end time
                if (appointmentTime.isAfter(leave.getStartTime()) && appointmentTime.isBefore(leave.getEndTime())) {
                    availabilityStatus.put("availability", false);
                    availabilityStatus.put("message", "Doctor is on leave from " + leave.getStartTime() + " to " + leave.getEndTime());
                    return availabilityStatus;
                }
            }

            if (leave.getStartTime() != null && leave.getEndTime() == null) {
                // Leave with only start time
                if (appointmentTime.isAfter(leave.getStartTime())) {
                    availabilityStatus.put("availability", false);
                    availabilityStatus.put("message", "Doctor is on leave from " + leave.getStartTime() + " onwards");
                    return availabilityStatus;
                }
            }
        }

        // Doctor is available for the given appointment date and time
        Map<String, Object> availabilityStatus = new HashMap<>();
        availabilityStatus.put("availability", true);
        availabilityStatus.put("message", "Doctor is available for the appointment.");
        return availabilityStatus;
    }


    public ResponseEntity<Object> getUpcomingAppointmentsForDoctor(Long doctorId) {
        // Check if the doctor exists with the given ID
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (!optionalDoctor.isPresent()) {
        	Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Doctor not found with ID: " + doctorId);
            return ResponseEntity.badRequest().body(response);
        }
        Doctor doctor = optionalDoctor.get();
        List<Appointment> upcomingAppointments = appointmentRepository.findAllByDoctorIdAndDateTimeAfter(doctor.getId(), LocalDateTime.now());
        
        if(upcomingAppointments.isEmpty()) {
        	Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "No upcoming appointment for doctor with id :"+doctorId);
            return ResponseEntity.badRequest().body(response);
        }

        // Create a map to group upcoming appointment date and time strings by date
        Map<String, List<String>> groupedAppointments = new HashMap<>();
        for (Appointment appointment : upcomingAppointments) {
            String date = appointment.getDateTime().toLocalDate().toString();
            groupedAppointments.computeIfAbsent(date, key -> new ArrayList<>()).add(appointment.getDateTime().toLocalTime().toString());
        }

        // Create the response using the custom DTO
        DoctorAppointmentsResponse response = new DoctorAppointmentsResponse(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialization(),
                groupedAppointments
        );
        response.sortAppointments();

        // Return the response
        return ResponseEntity.ok(response);
    }
}
