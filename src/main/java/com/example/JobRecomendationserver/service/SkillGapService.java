package com.example.JobRecomendationserver.service;

import com.example.JobRecomendationserver.DTO.SkillGapResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SkillGapService {
    private String normalize(String s){
        return s.toLowerCase()
                .replace("."," ")
                .replace("-"," ")
                .replace(","," ")
                .trim();
    }
    public SkillGapResponse analyze(List<String>resumeSkills,List<String>jobSkills){
        resumeSkills = resumeSkills == null ? List.of() : resumeSkills;
        jobSkills = jobSkills == null ? List.of() : jobSkills;
        Set<String> resumeSet = resumeSkills.stream()
                .filter(Objects::nonNull)
                .map(this::normalize)
                .collect(Collectors.toSet());

        List<String> normalizedJobSkills = jobSkills.stream()
                .filter(Objects::nonNull)
                .map(this::normalize)
                .toList();


//        List<String> matched = jobSet.stream()
//                .filter(resumeSet::contains)
//                .toList();
//
//        List<String> missing = jobSet.stream()
//                .filter(skill -> !resumeSet.contains(skill))
//                .toList();
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (String skill : normalizedJobSkills) {
            if (resumeSet.contains(skill)) {
                matched.add(skill);
            } else {
                missing.add(skill);
            }
        }

        List<String> extra = resumeSet.stream()
                .filter(skill -> !normalizedJobSkills.contains(skill))
                .toList();

        double matchPercentage = normalizedJobSkills.isEmpty()
                ? 0.0
                : (matched.size() * 100.0 / normalizedJobSkills.size());
        System.out.println("RESUME SKILLS = " + resumeSet);
        System.out.println("JOB SKILLS = " + normalizedJobSkills);
        System.out.println("MATCHED = " + matched);
        return new SkillGapResponse(matched,missing,extra,matchPercentage);
    }
}
