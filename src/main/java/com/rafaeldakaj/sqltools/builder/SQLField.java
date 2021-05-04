package com.rafaeldakaj.sqltools.builder;

public class SQLField {

    private String column;
    private Object value;

    public SQLField(String column, Object value){
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
        
}
