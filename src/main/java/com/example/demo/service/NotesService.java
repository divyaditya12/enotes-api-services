package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.NotesDto;
import com.example.demo.entity.FileDetails;
import com.example.demo.exception.ResourceNotFound;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface NotesService {

    public Boolean saveNotes(NotesDto notesDto) throws ResourceNotFound, Exception;

    public List<NotesDto> getAllNotes();

    public Boolean saveNotesWithFile(String notes, MultipartFile file)
            throws Exception;

    public FileDetails getFileDetails(Integer id) throws Exception;

    public byte[] downloadFile(FileDetails fileDetails) throws Exception;
}
