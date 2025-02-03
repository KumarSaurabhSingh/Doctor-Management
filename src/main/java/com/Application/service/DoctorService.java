package com.Application.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Application.entity.Appointment;
import com.Application.entity.Doctor;
import com.Application.entity.DoctorLeave;
import com.Application.entity.DoctorSchedule;
import com.Application.repository.AppointmentRepository;
import com.Application.repository.DoctorLeaveRepository;
import com.Application.repository.DoctorRepository;


@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DoctorLeaveRepository doctorLeaveRepository;
    
  
    
    @Autowired
    private  AppointmentRepository appointmentRepository;

    public ResponseEntity<Object> createDoctor(Doctor newDoctor) {
    	
    	if(newDoctor.getEmail()==null|| newDoctor.getName()==null|| newDoctor.getSpecialization()==null||newDoctor.getEmail().isBlank()||newDoctor.getName().isBlank()|| newDoctor.getSpecialization().isBlank()) {
    		 Map<String, Object> response = new HashMap<>();
             response.put("success",false);
             response.put("message", "Either of the details in the input is empty or null");
             return ResponseEntity.badRequest().body(response);
    	}
        // Check if the email is unique
        if (doctorRepository.existsByEmail(newDoctor.getEmail())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Doctor with this email already exists.");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate the weekly schedule before saving the doctor
        List<DoctorSchedule> weeklySchedule = newDoctor.getWeeklySchedule();
        if (weeklySchedule == null || weeklySchedule.isEmpty() || weeklySchedule.stream().anyMatch(this::isInvalidSchedule)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Weekly schedule is required and should contain valid schedule entries.");
            return ResponseEntity.badRequest().body(response);
        }

        // Here, you can add more validation logic for the weekly schedule, e.g., checking if the time slots are valid.

        Doctor doctor = doctorRepository.save(newDoctor);

        // Create the response map
        Map<String, Object> response = new LinkedHashMap<>();
        
        response.put("id", doctor.getId()); // Include ID separately if required
        response.put("name", doctor.getName());
        response.put("email", doctor.getEmail());
        response.put("specialization", doctor.getSpecialization());
        response.put("weeklySchedule",doctor.getWeeklySchedule());
         // Add the average rating to the response

        return ResponseEntity.ok(response);
    }

    private boolean isInvalidSchedule(DoctorSchedule schedule) {
        return schedule.getDayOfWeek() == null || schedule.getStartTime() == null || schedule.getEndTime() == null;
    }

    
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
 // Service method to get a doctor by ID
    public ResponseEntity<Map<String, Object>> getDoctorById(Long id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();

            // Calculate the average rating of the doctor (if available)
           

            // Here you can implement additional logic for fetching appointments.

            // Create the response map
            Map<String, Object> response = new LinkedHashMap<>();
            
            response.put("id", doctor.getId()); // Include ID separately if required
            response.put("name", doctor.getName());
            response.put("email", doctor.getEmail());
            response.put("specialization", doctor.getSpecialization());
            response.put("weeklySchedule",doctor.getWeeklySchedule());
             // Add the average rating to the response

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Doctor not found with ID: " + id);

            return ResponseEntity.badRequest().body(response);
        }
    }

   

    
 // Service method to update doctor details
    public ResponseEntity<Object> updateDoctorDetails(Long id, Doctor updatedDoctor) {
    	 // Check if the doctor exists with the given ID
        if (!doctorRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Doctor not found with ID: " + id);
            return ResponseEntity.badRequest().body(response);
        }
    	if(updatedDoctor.getEmail()==null|| updatedDoctor.getName()==null|| updatedDoctor.getSpecialization()==null||updatedDoctor.getEmail().isBlank()||updatedDoctor.getName().isBlank()|| updatedDoctor.getSpecialization().isBlank()) {
   		 Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Either of the details in the input is empty or null");
            return ResponseEntity.badRequest().body(response);
   	}
       

        // Check if the updated email is already used by another doctor
        if (doctorRepository.existsByEmailAndIdNot(updatedDoctor.getEmail(), id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Email already used by another doctor.");
            return ResponseEntity.badRequest().body(response);
        }

        // Fetch the existing doctor from the database
        Doctor existingDoctor = doctorRepository.findById(id).get();
        
        
        if(updatedDoctor.getWeeklySchedule()==null|| updatedDoctor.getWeeklySchedule().isEmpty() || updatedDoctor.getWeeklySchedule().stream().anyMatch(this::isInvalidSchedule)) {
        	updatedDoctor.setWeeklySchedule(existingDoctor.getWeeklySchedule());
        }
        if (isDoctorUnchanged(updatedDoctor, existingDoctor)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "No changes found. Doctor details remain the same.");
            return ResponseEntity.ok(response);
        }

        // Update the details of the existing doctor
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setWeeklySchedule(updatedDoctor.getWeeklySchedule()); // Set the updated weekly schedule

        // Save the updated doctor
        Doctor doctor=doctorRepository.save(existingDoctor);

     // Create the response map
        Map<String, Object> response = new LinkedHashMap<>();
        
        response.put("id", doctor.getId()); // Include ID separately if required
        response.put("name", doctor.getName());
        response.put("email", doctor.getEmail());
        response.put("specialization", doctor.getSpecialization());
        response.put("weeklySchedule",doctor.getWeeklySchedule());
         // Add the average rating to the response

        return ResponseEntity.ok(response);
    }
    
    private boolean isDoctorUnchanged(Doctor updatedDoctor, Doctor existingDoctor) {
        return updatedDoctor.getName().equals(existingDoctor.getName()) &&
                updatedDoctor.getEmail().equals(existingDoctor.getEmail()) &&
                updatedDoctor.getSpecialization().equals(existingDoctor.getSpecialization()) &&
                Objects.equals(updatedDoctor.getWeeklySchedule(), existingDoctor.getWeeklySchedule());
    }

    // Service method to delete a doctor
    public ResponseEntity<Object> deleteDoctor(Long id) {
        // Check if the doctor exists with the given ID
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (!optionalDoctor.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Doctor not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Doctor doctor = optionalDoctor.get();

        // Check if the doctor has upcoming appointments
        boolean hasUpcomingAppointments = checkForUpcomingAppointments(doctor);

        if (hasUpcomingAppointments) {
            Map<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message", "Cannot delete the doctor. The doctor has upcoming appointments.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // If the doctor has no upcoming appointments, delete the doctor, past appointments, and doctor reviews
        doctorRepository.delete(doctor);
        deletePastAppointmentsForDoctor(doctor);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success",true);
        return ResponseEntity.ok(response);
    }

 // Helper method to check if the doctor has upcoming appointments
    private boolean checkForUpcomingAppointments(Doctor doctor) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Appointment> upcomingAppointments = appointmentRepository.findAllByDoctorIdAndDateTimeAfter(doctor.getId(), currentDateTime);
        return !upcomingAppointments.isEmpty();
    }


    // Helper method to delete past appointments for a doctor
    private void deletePastAppointmentsForDoctor(Doctor doctor) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Appointment> pastAppointments = appointmentRepository.findAllByDoctorIdAndDateTimeBefore(doctor.getId(), currentDateTime);
        appointmentRepository.deleteAll(pastAppointments);
    }    
    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        List<Doctor> results = new ArrayList<>();
        List<Doctor> doctors=doctorRepository.findAll();
        for (Doctor doctor : doctors) {
            // Perform filtering based on the provided specialization
            if (doctor.getSpecialization().equalsIgnoreCase(specialization)) {
                results.add(doctor);
            }
        }

        return results;
    }
    

    public Optional<Doctor> getDoctorById1(Long id) {
    	Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
    	return optionalDoctor;
    }
    
 // Method to get doctor availability for a specific date and time
    public Map<String, Object> getDoctorAvailability(Long id, LocalDateTime dateTime) {
        Map<String, Object> response = new HashMap<>();

        // Fetch the doctor by ID from the repository
        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if (doctor == null) {
            response.put("success",false);
            response.put("message", "Doctor not found.");
            return response;
        }

        // Get the day of the week and time from the dateTime parameter
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        LocalTime appointmentTime = dateTime.toLocalTime();

        // Use DoctorAvailabilityChecker to check the doctor's availability
        DoctorAvailabilityChecker availabilityChecker = new DoctorAvailabilityChecker();
        boolean isAvailable = availabilityChecker.isDoctorAvailable(doctor, dayOfWeek, appointmentTime);
        
        if(isAvailable){
        	// Check if the doctor is on leave at the specified date and time
           
        	Map<String, Object> availabilityStatus = checkDoctorAvailability(doctor, dateTime);

            if (!(boolean) availabilityStatus.get("availability")) {
                response.put("success",false);
                response.put("message", availabilityStatus.get("message"));
                return response;
            }
        	LocalDateTime endDateTime = dateTime.plusMinutes(30);
        	List<Appointment> existingAppointments = appointmentRepository.findAllByDoctorIdAndDateTimeBetween(
                    id, dateTime, endDateTime);

            if (!existingAppointments.isEmpty()) {
                response.put("success",false);
                response.put("message", "Doctor is available on the date but no available slot for the given date and time for the appointment with the doctor.");
                return response;
            }

        response.put("success",true);
        response.put("message", "yes doctor is available at give date and time: "+dateTime);
        return response;
    }
        else {
        	response.put("success",false);
            response.put("message", "no doctor is not available at give date and time: "+dateTime);
            return response;
        }
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


    
    public Doctor getDoctorById2(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        return optionalDoctor.orElse(null);
    }

    // Other service methods for CRUD operations or business logic related to doctors...
}

