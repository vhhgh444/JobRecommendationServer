package com.example.JobRecomendationserver.repository;

import com.example.JobRecomendationserver.entity.JobRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<JobRole,Long> {
    List<JobRole> findByDescriptionContainingIgnoreCase(String keyword);

    List<JobRole> findByDomain(String domain);

    List<JobRole> findByRoleNameContainingIgnoreCase(String roleName);

    List<JobRole> findByCompanyContainingIgnoreCase(String company);

    List<JobRole> findByLocationContainingIgnoreCase(String location);
}
