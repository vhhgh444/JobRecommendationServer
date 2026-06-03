package com.example.JobRecomendationserver.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponse {

    private String predictedDomain;
    private Double confidence;
    private List<JobDTO> recommendedJobs;
}
