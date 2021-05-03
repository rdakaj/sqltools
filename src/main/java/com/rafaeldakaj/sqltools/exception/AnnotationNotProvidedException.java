package com.rafaeldakaj.sqltools.exception;

public class AnnotationNotProvidedException extends RuntimeException{

    public AnnotationNotProvidedException(Class<?> annotation, Class<?> object){
        super("Cannot find the required annotation \"" + annotation.getName() + 
        "\" inside of the class \"" + object.getName() + "\"");
    }
    
}
