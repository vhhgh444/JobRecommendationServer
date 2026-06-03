package com.example.JobRecomendationserver.service;

import com.example.JobRecomendationserver.DTO.RecommendationResponse;

public interface RecommendationService {


RecommendationResponse getRecommendation(Long resumeId);

}
