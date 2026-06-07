package com.example.JobRecomendationserver.DTO;

import lombok.Data;

import java.util.List;

@Data
public class JobSearchResponse {
    private List<JobData>data;
}
