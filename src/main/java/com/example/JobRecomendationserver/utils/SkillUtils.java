package com.example.JobRecomendationserver.utils;

import java.util.Arrays;
import java.util.List;

public class SkillUtils {

    public static String listToString(List<String>skills){
        return String.join(",",skills);
    }

    public static List<String >stringToList(String skills){
        return Arrays.asList(skills.split(","));
    }

}
