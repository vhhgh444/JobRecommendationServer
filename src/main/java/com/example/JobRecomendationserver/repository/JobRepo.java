package com.example.JobRecomendationserver.repository;

import com.example.JobRecomendationserver.entity.JobRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<JobRole,Long> {
    List<JobRole> findByDescriptionContainingIgnoreCase(String keyword);

    List<JobRole> findByDomain(String domain);

    List<JobRole> findByRoleNameContainingIgnoreCase(String roleName);

    List<JobRole> findByCompanyContainingIgnoreCase(String company);

    List<JobRole> findByLocationContainingIgnoreCase(String location);
    boolean existsByRoleNameAndCompany(String roleName, String company);
    boolean existsByRoleNameAndCompanyAndLocation(
            String roleName,
            String company,
            String location
    );
    @Query("SELECT j FROM JobRole j WHERE " +
            "LOWER(j.roleName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.domain) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<JobRole> searchByRoleName(@Param("keyword") String keyword);
    @Query("SELECT j FROM JobRole j WHERE :skill MEMBER OF j.requiredSkills")
    List<JobRole> findByRequiredSkill(@Param("skill") String skill);
}
