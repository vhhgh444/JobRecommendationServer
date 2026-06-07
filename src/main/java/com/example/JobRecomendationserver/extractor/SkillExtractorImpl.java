package com.example.JobRecomendationserver.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.JobRecomendationserver.entity.Skill;
import com.example.JobRecomendationserver.repository.SkillRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class SkillExtractorImpl implements SkillExtractor {


    private final SkillRepository skillRepository;
    private List<String> knownSkills;

    public SkillExtractorImpl(SkillRepository skillRepository) {
        this.skillRepository=skillRepository;
    }
    @PostConstruct
    public void init(){
        knownSkills=skillRepository.findAll()
                .stream()
                .map(Skill::getSkillName)
                .toList();
        System.out.println("Load "+knownSkills.size()+"skills from database");
    }

//    private List<String> loadSkills() {
//        List<String> skills = new ArrayList<>();
//
//        try {
//            ClassPathResource resource =
//                    new ClassPathResource("skills.txt");
//
//            BufferedReader reader =
//                    new BufferedReader(
//                            new InputStreamReader(resource.getInputStream()));
//
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//
//                line = line.trim();
//
//                if (!line.isEmpty()) {
//                    skills.add(line);
//                }
//            }
//
//            reader.close();
//
//        } catch (IOException e) {
//            throw new RuntimeException(
//                    "Failed to load skills.txt",
//                    e
//            );
//        }
//
//        return skills;
//    }

    @Override
    public List<String> extractSkills(String resumeText) {

        List<String> extractedSkills = new ArrayList<>();

       // String lowerCaseResume = resumeText.toLowerCase();
        if(resumeText==null || resumeText.isEmpty()){
            return extractedSkills;
        }

        String nomalizedResume=resumeText.toLowerCase()
                .replace("-"," ")
                .replace("_"," ")
                .replace("/", " ")
                .replace("&", " ")
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        for (String skill : knownSkills) {

            if (nomalizedResume.contains(skill.toLowerCase())) {
                extractedSkills.add(skill);
            }
        }

        System.out.println("Skills Found = " + extractedSkills);

        return extractedSkills;
    }
}


