package com.Application.repository;



import com.Application.entity.Notes;
import com.Application.entity.Patient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {

	List<Notes> findByPatient(Patient patient);
    // You can define custom queries or methods here if needed
}

