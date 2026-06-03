package com.example.JobRecomendationserver.extractor;

import java.util.List;

public interface  SkillExtractor {

    List<String> extractSkills(String resumeText);
}
