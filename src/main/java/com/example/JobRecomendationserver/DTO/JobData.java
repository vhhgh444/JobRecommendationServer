package com.example.JobRecomendationserver.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobData {
    @JsonProperty("job_title")
    private String job_title;

    @JsonProperty("employer_name")
    private String company;

    @JsonProperty("job_city")
    private String job_city;

    @JsonProperty("job_country")
    private String job_country;

    @JsonProperty("job_description")
    private String job_description;

    @JsonProperty("job_employment_type")
    private String category;
}
