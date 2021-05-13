package com.rafaeldakaj.sqltools.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rafaeldakaj.sqltools.builder.SQLQuery;
import com.rafaeldakaj.sqltools.builder.SQLStatement;
import com.rafaeldakaj.sqltools.thread.SQLQueryTask;
import com.rafaeldakaj.sqltools.thread.SQLStatementTask;

public class SQLConnection {
    
    private Connection conn;
    

    public SQLConnection(SQLDatabase database, String schema){
        try{
            this.conn = DriverManager.getConnection("jdbc:mysql://" + database.getHost() + ":3306/" + schema
            + "?autoReconnect=true&useSSL=false", database.getUsername(), database.getPassword());
        } catch(SQLException e1){
            e1.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void destroy() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet sendQuery(SQLQuery query) {
        SQLQueryTask task = new SQLQueryTask(this.getConnection(), query);
        Thread thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return task.getResult();
    }

    public int sendStatement(SQLStatement statement) {
        SQLStatementTask task = new SQLStatementTask(this.getConnection(), statement);
        Thread thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return task.getResult();  
    }
    
}
