package com.example.JobRecomendationserver.repository;

import com.example.JobRecomendationserver.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
