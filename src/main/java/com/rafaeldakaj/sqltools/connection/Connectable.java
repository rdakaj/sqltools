package com.rafaeldakaj.sqltools.connection;

import java.sql.Connection;
import java.sql.ResultSet;

import com.rafaeldakaj.sqltools.builder.SQLQuery;
import com.rafaeldakaj.sqltools.builder.SQLStatement;

public interface Connectable {

    public Connection getConnection();

    public void destroy();

    public ResultSet sendQuery(SQLQuery query);

    public int sendStatement(SQLStatement statement);
    
}
