package com.rafaeldakaj.sqltools.connection;

import java.sql.Connection;
import java.sql.ResultSet;

import com.rafaeldakaj.sqltools.SQLQuery;
import com.rafaeldakaj.sqltools.SQLStatement;

public interface SQLConnectionBase {

    public Connection getConnection();

    public void destroy();

    public ResultSet sendQuery(SQLQuery query);

    public int sendStatement(SQLStatement statement);
    
}
