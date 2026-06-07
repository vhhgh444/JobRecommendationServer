package com.example.JobRecomendationserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeResponseDTO {

    private List<String>skills;
    private String recommendedRole;
    private List<RolePrediction> recommendedRoles;
    private List<Map<String, Object>> jobMatches;

}
