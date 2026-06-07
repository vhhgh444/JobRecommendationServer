package com.example.JobRecomendationserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillGapResponse {
    private List<String>matchedSkills;
    private List<String> missingSkills;
    private List<String> extraSkills;
    private double matchPercentage;
}
