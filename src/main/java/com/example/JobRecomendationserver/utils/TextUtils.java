package com.example.JobRecomendationserver.utils;

public class TextUtils {

    public static String cleanText(String text){
        if(text==null){
            return "";
        }
        return text.toLowerCase()
                .replaceAll("[^a-zA-Z0-9]"," ")
                .replaceAll("\\s+"," ")
                .trim();
    }
}
