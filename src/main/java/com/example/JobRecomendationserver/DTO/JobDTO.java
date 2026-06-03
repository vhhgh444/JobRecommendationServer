package com.example.JobRecomendationserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {

    private Long id;
    private String title;
    private String company;
    private String location;
    private String domain;
    private String description;
    private String requiredSkills;
    private Double matchScore;
}
