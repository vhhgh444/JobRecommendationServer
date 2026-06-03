package com.example.JobRecomendationserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class MLResponse {

    private List<String> skills;
    private List<RolePrediction>recommendedRoles;
    private String role;


}
