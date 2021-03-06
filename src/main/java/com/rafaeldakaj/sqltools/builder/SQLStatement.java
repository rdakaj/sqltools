package com.rafaeldakaj.sqltools.builder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rafaeldakaj.sqltools.annotation.SQLColumn;
import com.rafaeldakaj.sqltools.annotation.SQLTable;

public class SQLStatement {

    private String table;
    private SQLFields fields;
    private Object input;
    private boolean insertOnly;
    private String rawStatement;
    private boolean logCommand = false;

    public SQLStatement(String table, SQLField... fields){
        this.table = table;
        this.fields = new SQLFields(fields);
        this.insertOnly = false;
    }

    public SQLStatement(String table, Object input){
        this.table = table;
        this.fields = new SQLFields();
        this.insertOnly = false;
        for(Field f : input.getClass().getDeclaredFields()){
            SQLColumn column = f.getAnnotation(SQLColumn.class);
            try {
                if(column != null) {
                    boolean wasAccessible = f.isAccessible();
                    if(!wasAccessible) f.setAccessible(true);
                    fields.add(new SQLField(column.value(), f.get(input)));
                    if(!wasAccessible) f.setAccessible(false);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public SQLStatement(Object input){
        this.table = input.getClass().getAnnotation(SQLTable.class).value();
        this.fields = new SQLFields();
        this.insertOnly = false;
        for(Field f : input.getClass().getDeclaredFields()){
            SQLColumn column = f.getAnnotation(SQLColumn.class);
            try {
                if(column != null) {
                    Object obj = f.get(input);
                    if(obj != null){
                        boolean wasAccessible = f.isAccessible();
                        if(!wasAccessible) f.setAccessible(true);
                        fields.add(new SQLField(column.value(), obj));
                        if(!wasAccessible) f.setAccessible(false);
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public SQLStatement(String rawStatement){
        this.rawStatement = rawStatement;
        this.insertOnly = true;
    }

    public static SQLStatement deleteStatement(Class<?> type, String key){
        String table = type.getAnnotation(SQLTable.class).value();
        return new SQLStatement("delete from " + table + " where " +
        AnnotationReader.getSearchableValueFieldName(type) + "='" + key + "'");
    }

    private String getUpdateString(){
        List<String> updateQuery = new ArrayList<>();
        for(SQLField field : fields.getFields()) updateQuery.add(field.getColumn() + "=?");
        return String.join(",", updateQuery);
    }

    private String getInsertString(){
        List<String> qMarks = new ArrayList<>();
        for(int i = 0 ; i < fields.getFields().size() ; i++) qMarks.add("?");
        return "insert into " + table + "(" + String.join(",", fields.getColumns()) + ") values(" +String.join(",", qMarks) + ")";
    }
    
    public int send(Connection conn){
        Integer result = null;
        String updateString = rawStatement == null ? getInsertString() : rawStatement;
        try {
            if(!insertOnly) updateString += " on duplicate key update " + getUpdateString();
            PreparedStatement update = conn.prepareStatement(updateString);
            if(rawStatement == null) for(int i = 0 ; i < fields.getFields().size() ; i++){
                SQLField field = (SQLField) fields.get(i);
                update.setObject(i + 1, field.getValue());
                if(!insertOnly) update.setObject(i + 1 + fields.size(), field.getValue());
            }
            if(logCommand) System.out.println("Sending SQL Statement: " +  updateString);
            result = update.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public SQLStatement insertOnly(){
        this.insertOnly = true;
        return this;
    }

    public SQLStatement logCommand(){
        this.logCommand = true;
        return this;
    }

    public SQLStatement ignoreUniqueValue(){
        for(Field f : input.getClass().getDeclaredFields()){
            SQLColumn column = f.getAnnotation(SQLColumn.class);
            try {
                if(column != null && column.unique()) {
                    boolean wasAccessible = f.isAccessible();
                    if(!wasAccessible) f.setAccessible(true);
                    fields.remove(fields.getFieldByColumn(column.value()));
                    if(!wasAccessible) f.setAccessible(false);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

}
