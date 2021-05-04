package com.rafaeldakaj.sqltools.exception;

public class NoMainDatabaseException extends RuntimeException{

    public NoMainDatabaseException(){
        super("The main database is not defined");
    }
    
}
