package com.example.JobRecomendationserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;

import java.util.List;

@Entity
@Table(name="resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    private String recommendedRole;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "resume_skills",
            joinColumns = @JoinColumn(name = "resume_id")
    )
    @Column(name = "skill")
    private List<String> skills;



}
