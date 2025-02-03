package com.Application.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Application.entity.Doctor;
import com.Application.entity.DoctorLeave;

@Repository
public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, Long> {
    Optional<DoctorLeave> findByDoctorAndLeaveDate(Doctor doctor, LocalDate leaveDate);
}
