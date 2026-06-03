package com.example.JobRecomendationserver.entity;

//import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_name")
    private String skillName;

}
