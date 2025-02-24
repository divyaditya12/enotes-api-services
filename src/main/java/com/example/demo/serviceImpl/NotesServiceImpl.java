package com.example.demo.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.NotesDto;
import com.example.demo.entity.FileDetails;
import com.example.demo.entity.Notes;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.repository.FileRepository;
import com.example.demo.repository.NotesRepository;
import com.example.demo.service.NotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private FileRepository fileRepository;

    @Value("${file.upload.path}")
    private String filePath;

    @Override
    public Boolean saveNotes(NotesDto notesDto) throws ResourceNotFound {

        // validating notes

        Integer categoryId = notesDto.getCategory().getId();
        if (categoryId == null) {
            throw new ResourceNotFound("invalid category id");
        }
        Notes notes = mapper.map(notesDto, Notes.class);
        Notes saveNotes = notesRepository.save(notes);
        if (!ObjectUtils.isEmpty(saveNotes)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean saveNotesWithFile(String notes, MultipartFile file)
            throws Exception {
        ObjectMapper om = new ObjectMapper();
        NotesDto notesDto = om.readValue(notes, NotesDto.class);
        Integer categoryId = notesDto.getCategory().getId();

        // Category validation
        if (categoryId == null) {
            throw new ResourceNotFound("invalid category id");
        }
        Notes notesMap = mapper.map(notesDto, Notes.class);

        FileDetails fileDetails = saveFileDetails(file);
        if (!ObjectUtils.isEmpty(fileDetails)) {
            notesMap.setFileDetails(fileDetails);
        } else {
            notesMap.setFileDetails(null);
        }

        Notes saveNotes = notesRepository.save(notesMap);
        if (!ObjectUtils.isEmpty(saveNotes)) {
            return true;
        }
        return false;
    }

    private FileDetails saveFileDetails(MultipartFile file) throws IOException {

        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new IOException("Uploaded file does not have a valid name.");
            }
            String extention = FilenameUtils.getExtension(originalFileName);
            List<String> allowedExtensions = Arrays.asList("pdf", "xlsx", "jpg", "png");
            if (!allowedExtensions.contains(extention)) {
                throw new IllegalArgumentException("Invalid file formate!!");
            }

            String randomString = UUID.randomUUID().toString();

            String uploadFileName = randomString + "." + extention;

            File saveFile = new File(filePath);
            if (!saveFile.exists()) {
                saveFile.mkdir();
            }
            String sotrePath = filePath.concat(uploadFileName);

            // upload file
            long upload = Files.copy(file.getInputStream(), Paths.get(sotrePath));
            if (upload != 0) {
                FileDetails fileDetails = new FileDetails();
                fileDetails.setOriginalFileName(originalFileName);
                fileDetails.setDisplayFileName(getDisplayFileName(originalFileName));
                fileDetails.setUploadFileName(uploadFileName);
                fileDetails.setFileSize(file.getSize());
                fileDetails.setFilePath(sotrePath);
                FileDetails savedFile = fileRepository.save(fileDetails);
                return savedFile;
            }
        }
        return null;
    }

    private String getDisplayFileName(String originalFileName) {

        String extention = FilenameUtils.getExtension(originalFileName);
        String fileName = FilenameUtils.removeExtension(originalFileName);
        if (fileName.length() > 8) {
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName + "." + extention;
        return fileName;
    }

    @Override
    public List<NotesDto> getAllNotes() {

        return notesRepository.findAll().stream()
                .map(note -> mapper.map(note, NotesDto.class)).toList();

    }

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws Exception {
        InputStream io = new FileInputStream(fileDetails.getFilePath());
        return StreamUtils.copyToByteArray(io);
    }

    @Override
    public FileDetails getFileDetails(Integer id) throws Exception {
        FileDetails fileDetails = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("File is not available"));
        return fileDetails;
    }

}
