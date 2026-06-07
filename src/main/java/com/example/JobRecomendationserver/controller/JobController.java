package com.example.JobRecomendationserver.controller;

import com.example.JobRecomendationserver.entity.JobRole;
import com.example.JobRecomendationserver.repository.JobRepo;
import com.example.JobRecomendationserver.service.JobSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobRepo jobRepo;
    private final JobSearchService jobSearchService;

    public JobController(JobRepo jobRepo,JobSearchService jobSearchService) {
        this.jobRepo = jobRepo;
        this.jobSearchService = jobSearchService;
    }

    // 🔥 THIS IS WHERE POST MAPPING GOES
    @PostMapping
    public JobRole createJob(@RequestBody JobRole job) {
        if (job.getRequiredSkills() != null) {
            List<String> cleanedSkills = job.getRequiredSkills().stream()
                    .filter(Objects::nonNull)
                    .map(s -> s.toLowerCase().trim())
                    .distinct()
                    .toList();

            job.setRequiredSkills(cleanedSkills);
        }
        boolean exists = jobRepo.existsByRoleNameAndCompany(
                job.getRoleName(),
                job.getCompany()
        );
        if (exists) {
            throw new RuntimeException("Job already exists");
        }

        return jobRepo.save(job);
    }

    // optional test endpoint
    @GetMapping("/{id}")
    public JobRole getJob(@PathVariable Long id) {
        return jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }
    @GetMapping("/search")
    public List<JobRole> searchJobs(@RequestParam String keyword) {
        return jobRepo.findAll().stream()
                .filter(job ->
                        job.getRoleName().toLowerCase().contains(keyword.toLowerCase()) ||
                                job.getDescription().toLowerCase().contains(keyword.toLowerCase())
                )
                .toList();
    }
    @GetMapping("/import")
    public String importJobs(@RequestParam String query) {
        jobSearchService.fetchAndSaveJobs(query);
        return "Jobs imported successfully";
    }
}
