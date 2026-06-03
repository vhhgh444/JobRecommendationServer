package com.example.JobRecomendationserver.service;

import org.springframework.stereotype.Service;

import com.example.JobRecomendationserver.DTO.RecommendationResponse;

@Service
public class RecomendationServiceImpl implements RecommendationService {

@Override
public RecommendationResponse getRecommendation(Long resumeId) {

    RecommendationResponse response = new RecommendationResponse();

    // TODO:
    // Call Python model
    // Fetch recommended jobs
    // Set response values

    return response;
}


}
