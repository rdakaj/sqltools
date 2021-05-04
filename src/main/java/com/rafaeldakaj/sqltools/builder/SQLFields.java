package com.rafaeldakaj.sqltools.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLFields {

    private List<SQLField> fields;

    public SQLFields(){
        this.fields = new ArrayList<>();
    }
    public SQLFields(SQLField[] fields){
        this.fields = Arrays.asList(fields);
    }

    public List<String> getColumns(){
        List<String> columns = new ArrayList<>();
        for(SQLField field : fields) columns.add(field.getColumn());
        return columns;
    }

    public List<Object> getObjects(){
        List<Object> objects = new ArrayList<>();
        for(SQLField field : fields) objects.add(field.getValue());
        return objects;
    }

    public List<SQLField> getFields(){
        return fields;
    }

    public SQLField get(int i){
        return fields.get(i);
    }

    public int size(){
        return fields.size();
    }
    
    public SQLFields add(SQLField field){
        fields.add(field);
        return this;
    }

    public SQLFields remove(SQLField field){
        fields.remove(field);
        return this;
    }

    public SQLField getFieldByColumn(String column){
        for(SQLField field : fields) if(field.getColumn().equals(column)) return field;
        return null;
    }
}
