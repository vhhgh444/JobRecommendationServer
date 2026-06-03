package com.example.JobRecomendationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // @Bean
    // public String exampleBean(){
    //     return "This is a sample bean";
    // }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
