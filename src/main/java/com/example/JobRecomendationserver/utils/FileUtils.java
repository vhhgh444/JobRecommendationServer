package com.example.JobRecomendationserver.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class FileUtils {


    public static void validateFile(MultipartFile file){
        if(file==null || file.isEmpty()){
            throw new RuntimeException("File is Empty");
        }
        String contentType=file.getContentType();
        if(contentType==null || (!contentType.equals("application/pdf") && !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))){
            throw new RuntimeException("Only PDF and DOCX files are allowed");
        }
    }

    public static InputStream getInputStream(MultipartFile file){
        try{
            return file.getInputStream();
        }catch (IOException e){
            throw new RuntimeException("Error reading file");
        }
    }

}
