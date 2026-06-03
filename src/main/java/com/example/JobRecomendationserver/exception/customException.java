package com.example.JobRecomendationserver.exception;

public class customException extends RuntimeException{
    public customException(String message){
        super(message);
    }
    public customException(String message,Throwable cause){
        super(message,cause);
    }
}
