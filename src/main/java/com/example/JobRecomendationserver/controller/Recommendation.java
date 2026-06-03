package com.example.JobRecomendationserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JobRecomendationserver.DTO.RecommendationResponse;
import com.example.JobRecomendationserver.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins="*")
public class Recommendation {

    private final RecommendationService recommendationService;

    public Recommendation(RecommendationService recommendationService){
        this.recommendationService=recommendationService;
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<RecommendationResponse>getRecommendation(@PathVariable Long resumeId){
        RecommendationResponse response=recommendationService.getRecommendation(resumeId);
        return ResponseEntity.ok(response);
    }

}
