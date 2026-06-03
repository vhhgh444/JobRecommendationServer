package com.example.JobRecomendationserver.repository;

import com.example.JobRecomendationserver.entity.Resume;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResumeRepo extends JpaRepository<Resume,Long> {

    List<Resume>findByRecommendedRole(String recommendedRole);

    @Query("SELECT r FROM Resume r WHERE :skill MEMBER OF r.skills")
    List<Resume> findBySkill(@Param("skill") String skill);

   Optional<Resume> findByFileName(String fileName);
}
