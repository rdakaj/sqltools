package com.rafaeldakaj.sqltools;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rafaeldakaj.sqltools.annotation.SQLColumn;
import com.rafaeldakaj.sqltools.annotation.SQLTable;

public class SQLQuery {

    private String query;
    private ResultSet set;
    Class<?> type;

    public SQLQuery(String table){
        this.query = "select * from " + table;
    }

    public SQLQuery(String table, String column, Object value){
        String sValue = value instanceof String ? "'" + value + "'" : value.toString();
        this.query = "select * from " + table + " where " + column + "=" + sValue;
    }

    public <V> SQLQuery(Class<V> classType, String column, Object value){
        SQLTable table = classType.getAnnotation(SQLTable.class);
        this.type = classType;
        String sValue = value instanceof String ? "'" + value + "'" : value.toString();
        this.query = "select * from " + table.value() + " where " + column + "=" + sValue;
    }

    public <V> SQLQuery(Class<V> classType){
        SQLTable table = classType.getAnnotation(SQLTable.class);
        this.type = classType;
        if(table != null) this.query = "select * from " + table.value();        
    }

    public <V> SQLQuery(Class<V> classType, Object value){
        this.type = classType;
        SQLTable table = classType.getAnnotation(SQLTable.class);
        for(Field f : classType.getFields()){
            SQLColumn column = f.getAnnotation(SQLColumn.class);
            String sValue = value instanceof String ? "'" + value + "'" : value.toString();
            if(column.searchable()) this.query = "select * from " + table.value() + " where " + column.value() + "=" + sValue; 
        }     
    }
    
    public ResultSet send(Connection conn){
        try {
            this.set = conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.set;
    }

    public boolean next(){
        try {
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getResultSet(){
        return set;
    }

}
