package com.example.JobRecomendationserver.service;

import java.util.*;

import java.util.Map;
import java.util.stream.Collectors;

import com.example.JobRecomendationserver.DTO.MLResponse;
import com.example.JobRecomendationserver.DTO.ResumeResponseDTO;
import com.example.JobRecomendationserver.DTO.SkillGapResponse;
import com.example.JobRecomendationserver.Parser.ResumeParser;
import com.example.JobRecomendationserver.entity.JobRole;
import com.example.JobRecomendationserver.entity.Resume;

import com.example.JobRecomendationserver.repository.JobRepo;
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

import com.example.JobRecomendationserver.extractor.SkillExtractor;
import com.example.JobRecomendationserver.DTO.RolePrediction;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeParser resumeParser;
    private final RestTemplate restTemplate;
    private final ResumeRepo resumeRepo;
    private final SkillExtractor skillExtractor;
    private final JobRepo jobRepo;
    private final SkillGapService skillGapService;
     @Value("${ml.api.url}")
       private String url;
     //private final SkillExtraction skillExtraction;
//    private final RoleRecommendation roleRecommendation;


    public ResumeServiceImpl(ResumeParser resumeParser,
                             RestTemplate restTemplate,
                             ResumeRepo resumeRepo,
                            SkillExtractor skillExtractor,
                             JobRepo jobRepo,
                             SkillGapService skillGapService) {
        this.resumeParser = resumeParser;
//        this.skillExtraction = skillExtraction;
//        this.roleRecommendation = roleRecommendation;
        this.restTemplate=restTemplate;
        this.resumeRepo=resumeRepo;
        this.skillExtractor = skillExtractor;
        this.jobRepo=jobRepo;
        this.skillGapService=skillGapService;
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
            System.out.println("ML Roles = " + ml.getRecommendedRoles());
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
//            List<String> roles = ml.getRecommendedRoles()
//                    .stream()
//                    .map(RolePrediction::getRole)   // extract role string
//                    .toList();

            // roles from ML
            List<String> roles = ml.getRecommendedRoles()
                    .stream()
                    .map(RolePrediction::getRole)
                    .filter(Objects::nonNull)
                    .toList();

// normalize
            List<String> normalizedRoles = roles.stream()
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(s -> !s.isBlank())
                    .toList();

// fetch jobs
            List<JobRole> jobs = new ArrayList<>();

            for (String role : normalizedRoles) {

                // split into words for better matching
                String[] words = role.split(" ");

                for (String word : words) {
                    if (!word.isBlank()) {
                        jobs.addAll(jobRepo.searchByRoleName(word));
                    }
                }
            }
            jobs = jobs.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(
                                    j -> j.getRoleName() + "_" + j.getCompany(),
                                    j -> j,
                                    (j1, j2) -> j1
                            ),
                            m -> new ArrayList<>(m.values())
                    ));

// remove duplicates
//            jobs = jobs.stream()
//                    .distinct()
//                    .toList();

            List<Map<String, Object>> jobMatches = new ArrayList<>();

            // 6. Skill gap analysis for each job
            for (JobRole job : jobs) {

                SkillGapResponse gap =
                        skillGapService.analyze(skills, job.getRequiredSkills());

                Map<String, Object> map = new HashMap<>();
                map.put("role", job.getRoleName());
                map.put("company", job.getCompany());
                map.put("domain", job.getDomain());
                map.put("location", job.getLocation());

                map.put("matchPercentage", gap.getMatchPercentage());
                map.put("matchedSkills", gap.getMatchedSkills());
                map.put("missingSkills", gap.getMissingSkills());

                jobMatches.add(map);
            }

// Return response
            return ResumeResponseDTO.builder()
                    .skills(skills)
                    .recommendedRole(ml.getRole())
                    .recommendedRoles(ml.getRecommendedRoles())
                    .jobMatches(jobMatches)
                    .build();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to process resume"+ e.getMessage(),e);
        }
    }
}