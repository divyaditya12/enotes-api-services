package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.NotesDto;
import com.example.demo.entity.Notes;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.service.NotesService;
import com.example.demo.util.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @PostMapping("/save-notes")
    public ResponseEntity<?> saveNotes(@RequestBody NotesDto notesDto) throws ResourceNotFound {
        Boolean saveNotes = notesService.saveNotes(notesDto);
        if (saveNotes) {
            return CommonUtils.createBuildResponseMessage("Notes saved Successfully", HttpStatus.CREATED);
        } else {
            return CommonUtils.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save-notes-with-file")
    public ResponseEntity<?> saveNotesWithFile(@RequestParam String notes,
            @RequestParam(required = false) MultipartFile file)
            throws Exception {
        Boolean saveNotes = notesService.saveNotesWithFile(notes, file);
        if (saveNotes) {
            return CommonUtils.createBuildResponseMessage("Notes saved Successfully", HttpStatus.CREATED);
        } else {
            return CommonUtils.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getNotes")
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> notes = notesService.getAllNotes();
        if (CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        } else {
            return CommonUtils.createBuildResponse(notes, HttpStatus.OK);
        }
    }

}
