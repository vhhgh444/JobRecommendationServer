package com.example.JobRecomendationserver.service;

import com.example.JobRecomendationserver.DTO.JobData;
import com.example.JobRecomendationserver.DTO.JobSearchResponse;
import com.example.JobRecomendationserver.entity.JobRole;
import com.example.JobRecomendationserver.extractor.SkillExtractor;
import com.example.JobRecomendationserver.repository.JobRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JobSearchService {

    @Value("${rapidapi.key}")
    private String apiKey;
    @Value("${rapidapi.host}")
    private String apiHost;
    private final RestTemplate restTemplate;
    private final JobRepo jobRepo;
    private final SkillExtractor skillExtractor;
    public JobSearchService(RestTemplate restTemplate,JobRepo jobRepo,SkillExtractor skillExtractor){
        this.restTemplate=restTemplate;
        this.jobRepo=jobRepo;
        this.skillExtractor=skillExtractor;
    }
    public List<JobData> fetchJobs(String query){
        System.out.println("API KEY = [" + apiKey + "]");
        System.out.println("API HOST = [" + apiHost + "]");
        String url="https://jsearch.p.rapidapi.com/search?query="
                +query+"&page=1&num_pages=1";
        HttpHeaders headers=new HttpHeaders();
        headers.set("X-RapidAPI-Key",apiKey);
        headers.set("X-RapidAPI-Host",apiHost);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String>entity=new HttpEntity<>(headers);
        ResponseEntity<JobSearchResponse> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        JobSearchResponse.class
                );
        return response.getBody().getData();
    }
    public void fetchAndSaveJobs(String query){
        List<JobData> jobs = fetchJobs(query);

        for (JobData job : jobs) {

            String title = job.getJob_title();
            String company = job.getCompany();

            if (title == null || company == null ||
                    title.isBlank() || company.isBlank()) {
                continue;
            }

            boolean exists = jobRepo
                    .existsByRoleNameAndCompany(title, company);

            if (exists) continue;

            JobRole entity = new JobRole();
            entity.setRoleName(title);
            entity.setCompany(company);
            entity.setLocation(
                    job.getJob_city() != null ? job.getJob_city() : "Unknown"
            );
            entity.setDomain(
                    job.getCategory() != null ? job.getCategory() : "Unknown"
            );
            entity.setDescription(
                    job.getJob_description() != null ? job.getJob_description() : ""
            );
            String text = title + " " +
                    (entity.getDescription() != null ? entity.getDescription() : "");

            List<String> skills = skillExtractor.extractSkills(text);

            entity.setRequiredSkills(skills);

            jobRepo.save(entity);
        }
    }
}
