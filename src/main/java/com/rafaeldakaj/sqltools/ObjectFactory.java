package com.rafaeldakaj.sqltools;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rafaeldakaj.sqltools.annotation.SQLColumn;
import com.rafaeldakaj.sqltools.connection.SQLConnectionBase;

@SuppressWarnings("unchecked")
public class ObjectFactory {

    Class<?> type;
    SQLConnectionBase sql;

    public <V> ObjectFactory(Class<V> type, SQLConnectionBase sql){
        this.type = type;
        this.sql = sql;
    }

    public <V> V createObject(ResultSet set){
        V object = null;
        try {
            if(set.next()){
                object = (V) type.getConstructor().newInstance();
                for(Field field : type.getFields()) {
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
                for(Field field : type.getFields()) {
                    SQLColumn column = field.getAnnotation(SQLColumn.class);
                    if(column != null) field.set(object, set.getObject(column.value()));
                }
                objects.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    public <V> V createObject(SQLQuery query){
        return createObject(sql.sendQuery(query));
    }

    public <V> List<V> createObjectList(SQLQuery query){
        return createObjectList(sql.sendQuery(query));
    }

}
