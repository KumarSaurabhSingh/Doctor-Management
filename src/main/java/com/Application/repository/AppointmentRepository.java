package com.Application.repository;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Application.entity.Appointment;
import com.Application.entity.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDoctorIdAndDateTimeBetween(Long doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime);

	List<Appointment> findAllByDoctorIdAndDateTimeAfter(Long id, LocalDateTime currentDateTime);

	List<Appointment> findAllByDoctorIdAndDateTimeBefore(Long id, LocalDateTime currentDateTime);
	
	@Query("SELECT MAX(a.dateTime) FROM Appointment a WHERE a.patient.patientId = :patientId AND a.doctor.id = :doctorId")
    LocalDateTime findLatestAppointmentDateTime(Long patientId, Long doctorId);

	List<Appointment> findAllByPatientAndDateTime(Patient patient, LocalDateTime appointmentDateTime);
}
