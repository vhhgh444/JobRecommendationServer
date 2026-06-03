package com.example.JobRecomendationserver.service;

import com.example.JobRecomendationserver.DTO.ResumeResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {
    ResumeResponseDTO processResume(MultipartFile multipartFile);
}
