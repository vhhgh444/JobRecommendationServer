package com.example.JobRecomendationserver.service;

import java.util.Collections;
import java.util.List;

import com.example.JobRecomendationserver.DTO.MLResponse;
import com.example.JobRecomendationserver.DTO.ResumeResponseDTO;
import com.example.JobRecomendationserver.Parser.ResumeParser;
import com.example.JobRecomendationserver.entity.Resume;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.JobRecomendationserver.repository.ResumeRepo;
import com.example.JobRecomendationserver.utils.FileUtils;

//import lombok.Value;

import org.springframework.beans.factory.annotation.Value;

//import java.net.http.HttpHeaders;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.UUID;

import com.example.JobRecomendationserver.extractor.SkillExtractor;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeParser resumeParser;
    private final RestTemplate restTemplate;
    private final ResumeRepo resumeRepo;
    private final SkillExtractor skillExtractor;
     @Value("${ml.api.url}")
       private String url;
     //private final SkillExtraction skillExtraction;
//    private final RoleRecommendation roleRecommendation;


    public ResumeServiceImpl(ResumeParser resumeParser,
                             RestTemplate restTemplate,
                             ResumeRepo resumeRepo,
                            SkillExtractor skillExtractor) {
        this.resumeParser = resumeParser;
//        this.skillExtraction = skillExtraction;
//        this.roleRecommendation = roleRecommendation;
        this.restTemplate=restTemplate;
        this.resumeRepo=resumeRepo;
        this.skillExtractor = skillExtractor;
    }

    @Override
    public ResumeResponseDTO processResume(MultipartFile multipartFile) {
        try {
            // if (multipartFile == null || multipartFile.isEmpty()) {
            //     throw new IllegalArgumentException("File is empty");
            // }
            FileUtils.validateFile(multipartFile);

            // Extract text
            String text = resumeParser.parseResume(multipartFile.getInputStream());
            if(text == null || text.isBlank()){
              throw new IllegalStateException(
               "No text extracted from resume"
                );
              }
            System.out.println("Final Text Length= "+text.length());

            List<String> skills = skillExtractor.extractSkills(text);
            System.out.println("Skills Found = " + skills);

            // if(text == null || text.isBlank()){
            //   throw new IllegalStateException(
            //    "No text extracted from resume"
            //     );
            //   }

            // API calling
           // String url="http://localhost:8000/predict";
           
           // @Value("${ml.api.url}")
           // private String url;
            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of("text", text);
            HttpEntity<Map<String, String>> request =
                    new HttpEntity<>(body, headers);
           // String requestBody="{\"text\":\""+text.replace("\"","\\\"")+"\"}";
           // HttpEntity<String>request=new HttpEntity<>(requestBody,headers);

            // Calling Python Api
            ResponseEntity<MLResponse> response = restTemplate.exchange(url,org.springframework.http.HttpMethod.POST, request, MLResponse.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalStateException("ML service error");
            }

            MLResponse ml = response.getBody();

            if (ml == null|| ml.getRole()==null || ml.getRole().isBlank()) {
             throw new IllegalStateException("No response received from ML service");
             }
            // List<String> skills = ml.getSkills() != null ? ml.getSkills() : Collections.emptyList();
            

// Save to database
            // if (ml.getSkills() == null) {
            //  ml.setSkills(java.util.Collections.emptyList());
            // }
            Resume resume = Resume.builder()
                   // .fileName(multipartFile.getOriginalFilename())
                    .fileName(UUID.randomUUID()+"_"+multipartFile.getOriginalFilename())
                    .extractedText(text)
                    .recommendedRole(ml.getRole())
                    .skills(skills)
                    .build();

            resumeRepo.save(resume);

// Return response
            return ResumeResponseDTO.builder()
                    .skills(skills)
                    .recommendedRole(ml.getRole())
                    .recommendedRoles(ml.getRecommendedRoles())
                    .build();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to process resume"+ e.getMessage(),e);
        }
    }
}