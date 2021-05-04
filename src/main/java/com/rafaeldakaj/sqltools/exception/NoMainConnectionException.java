package com.rafaeldakaj.sqltools.exception;

public class NoMainConnectionException extends RuntimeException{

    public NoMainConnectionException(){
        super("The main connection is not defined");
    }
    
}
