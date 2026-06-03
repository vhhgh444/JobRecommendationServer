package com.example.JobRecomendationserver.controller;

import com.example.JobRecomendationserver.utils.FileUtils;

import com.example.JobRecomendationserver.DTO.ResumeResponseDTO;
import com.example.JobRecomendationserver.service.ResumeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeUpload {

    @Autowired
    private ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponseDTO>uploadResume(@RequestParam("file")MultipartFile file){
        FileUtils.validateFile(file);
        ResumeResponseDTO response=resumeService.processResume(file);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/health")
    public ResponseEntity<String>healthCheck(){
        return ResponseEntity.ok("Resume Analyzer API is running....");
    }
}
