package com.Application.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Application.entity.NotesRequest;
import com.Application.service.NotesService;

@RestController
@RequestMapping("/doctors")
public class NotesController {
    @Autowired
    private NotesService notesService;

    // Endpoint to add notes for a patient
    @PostMapping("/{doctorId}/addNotes")
    public ResponseEntity<Object> addNotesForPatient(
            @PathVariable Long doctorId,
            @RequestBody NotesRequest notesRequest
    ) {
        return notesService.addNotesForPatient(doctorId, notesRequest);
    }
}

