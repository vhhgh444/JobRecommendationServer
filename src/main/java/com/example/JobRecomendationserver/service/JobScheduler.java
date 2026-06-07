package com.example.JobRecomendationserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class JobScheduler {
    private final JobSearchService jobSearchService;
    public JobScheduler(JobSearchService jobSearchService){
        this.jobSearchService=jobSearchService;
    }
   // @Scheduled(cron = "0 */10 * * * *")
   @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void syncJobs(){
        log.info("Job Search Started...");
        List<String> keywords=List.of(
                "java developer"
               // "python developer",
               // "data science",
              //  "backend developer",
              //  "machine learning engineer"
        );


//        for (String keyword : keywords) {
//            jobSearchService.fetchAndSaveJobs(keyword);
//
//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
    //    }
        for (String keyword : keywords) {
            try {
                jobSearchService.fetchAndSaveJobs(keyword);
                Thread.sleep(5000); // increase delay
            } catch (Exception e) {
                log.error("API error: {}", e.getMessage());
            }
        }
        log.info("Job Search Completed...");
    }
}
