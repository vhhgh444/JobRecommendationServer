package com.example.JobRecomendationserver.Parser;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ResumeParser {

    private static final Logger logger = LoggerFactory.getLogger(ResumeParser.class);
    private final Tika tika = new Tika();

    public String parseResume(InputStream inputStream) {
        try (inputStream) {
            String text = tika.parseToString(inputStream);
            System.out.println("Raw Test: ");
            System.out.println(text);
            System.out.println("Text Length:"+text.length());
            return text.replaceAll("\\s+", " ").trim();
        } catch (Exception e) {
            logger.error("Error parsing resume file", e);
            throw new RuntimeException("Error parsing resume file", e);
        }
    }
}