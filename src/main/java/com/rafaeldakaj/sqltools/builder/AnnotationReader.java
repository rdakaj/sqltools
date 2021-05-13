package com.rafaeldakaj.sqltools.builder;

import java.lang.reflect.Field;

import com.rafaeldakaj.sqltools.annotation.SQLColumn;

public class AnnotationReader {

    public static boolean hasUniqueValue(Object object){
        for(Field f : object.getClass().getDeclaredFields()){
            SQLColumn column = f.getAnnotation(SQLColumn.class);
            if(column != null && column.unique()) return true;
        }
        return false;
    }

    public static String getUniqueValueFieldName(Object object) {
        for(Field f : object.getClass().getDeclaredFields()){
            SQLColumn column = f.getAnnotation(SQLColumn.class);
            if(column != null && column.unique()) return f.getName();
        }
        throw new RuntimeException("No unique column inside the class " + object.getClass().getName());
    }
    
}
