package com.rafaeldakaj.sqltools.builder;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rafaeldakaj.sqltools.annotation.SQLColumn;
import com.rafaeldakaj.sqltools.connection.Connectable;
import com.rafaeldakaj.sqltools.connection.SQLConnection;

@SuppressWarnings("unchecked")
public class ObjectFactory {

    Class<?> type;

    public <V> ObjectFactory(Class<V> type){
        this.type = type;
    }

    public <V> V createObject(ResultSet set){
        V object = null;
        try {
            if(set.next()){
                object = (V) type.getConstructor().newInstance();
                for(Field field : type.getDeclaredFields()) {
                    SQLColumn column = field.getAnnotation(SQLColumn.class);
                    if(column != null) field.set(object, set.getObject(column.value()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public <V> List<V> createObjectList(ResultSet set){
        List<V> objects = new ArrayList<>();
        try {
            while(set.next()){
                V object = (V) type.getConstructor().newInstance();
                for(Field field : type.getDeclaredFields()) {
                    SQLColumn column = field.getAnnotation(SQLColumn.class);
                    if(column != null) {
                        boolean wasAccessible = field.isAccessible();
                        if(!wasAccessible) field.setAccessible(true);
                        field.set(object, set.getObject(column.value()));
                        if(!wasAccessible) field.setAccessible(false);
                    }
                }
                objects.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    public <V> V createObject(SQLQuery query){
        return createObject(SQLConnection.getMainConnection().sendQuery(query));
    }

    public <V> V createObject(SQLQuery query, Connectable sql){
        return createObject(sql.sendQuery(query));
    }

    public <V> List<V> createObjectList(SQLQuery query){
        return createObjectList(SQLConnection.getMainConnection().sendQuery(query));
    }

    public <V> List<V> createObjectList(SQLQuery query, Connectable sql){
        return createObjectList(sql.sendQuery(query));
    }

}
