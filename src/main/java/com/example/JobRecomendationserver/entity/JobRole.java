package com.example.JobRecomendationserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="job_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
    private String company;
    private String domain;
    private String location;
    @Column(columnDefinition = "TEXT")
    private String description;
   // @ElementCollection(fetch = FetchType.EAGER)
    @ElementCollection
    @CollectionTable(name = "job_role_required_skills",joinColumns = @JoinColumn(name = "job_role_id"))
    @Column(name = "skill")
    private List<String> requiredSkills=new ArrayList<>();

}
