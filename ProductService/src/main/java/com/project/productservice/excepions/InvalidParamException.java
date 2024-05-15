package com.project.productservice.excepions;

public class InvalidParamException extends Exception{
    public InvalidParamException(String message){
        super(message);
    }
}
