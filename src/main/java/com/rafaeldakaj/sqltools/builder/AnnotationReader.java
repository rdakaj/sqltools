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
        return null;
    }

    public static boolean hasSearchableValue(Object object) {
        for(Field f : object.getClass().getDeclaredFields()){
            SQLColumn column = f.getAnnotation(SQLColumn.class);
            if(column != null && column.searchable()) return true;
        }
        return false;
    }


    public static String getSearchableValueFieldName(Object object) {
        for(Field f : object.getClass().getDeclaredFields()){
            SQLColumn column = f.getAnnotation(SQLColumn.class);
            if(column != null && column.searchable()) return f.getName();
        }
        return null;
    }


    
}
