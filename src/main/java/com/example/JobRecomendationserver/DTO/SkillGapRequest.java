package com.example.JobRecomendationserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillGapRequest {
    private Long resumeId;
    private Long jobId;
}
