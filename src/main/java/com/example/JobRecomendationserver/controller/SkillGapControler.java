package com.example.JobRecomendationserver.controller;

import com.example.JobRecomendationserver.DTO.SkillGapResponse;
import com.example.JobRecomendationserver.entity.JobRole;
import com.example.JobRecomendationserver.entity.Resume;
import com.example.JobRecomendationserver.extractor.SkillExtractor;
import com.example.JobRecomendationserver.repository.JobRepo;
import com.example.JobRecomendationserver.repository.ResumeRepo;
import com.example.JobRecomendationserver.service.SkillGapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skill-gap")
public class SkillGapControler {
    private final SkillGapService skillGapService;
    private final ResumeRepo resumeRepo;
    private final JobRepo jobRepo;
    private final SkillExtractor skillExtractor;

    public SkillGapControler(SkillGapService skillGapService,
                             ResumeRepo resumeRepo,
                             JobRepo jobRepo, SkillExtractor skillExtractor) {
        this.skillGapService = skillGapService;
        this.resumeRepo = resumeRepo;
        this.jobRepo = jobRepo;
        this.skillExtractor=skillExtractor;
    }

    @GetMapping("/{resumeId}/{jobId}")
    public SkillGapResponse analyzedSkillGap(@PathVariable Long resumeId,@PathVariable Long jobId){
        Resume resume=resumeRepo.findById(resumeId).orElseThrow(()->new RuntimeException("Resume Not Found"));
        JobRole job=jobRepo.findById(jobId).orElseThrow(()->new RuntimeException("Job Not Found"));
        //return skillGapService.analyze(resume.getSkills(),job.getRequiredSkills());
        List<String>resumeSkills=skillExtractor.extractSkills(resume.getExtractedText());
        System.out.println("JOB SKILLS = " + job.getRequiredSkills());
        System.out.println("RESUME SKILLS = " + resume.getSkills());
        return skillGapService.analyze(resumeSkills,job.getRequiredSkills());

    }
}
