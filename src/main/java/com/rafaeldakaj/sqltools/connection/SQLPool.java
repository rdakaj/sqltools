package com.rafaeldakaj.sqltools.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rafaeldakaj.sqltools.SQLDatabase;
import com.rafaeldakaj.sqltools.SQLQuery;
import com.rafaeldakaj.sqltools.SQLStatement;
import com.rafaeldakaj.sqltools.thread.SQLQueryTask;
import com.rafaeldakaj.sqltools.thread.SQLStatementTask;

public class SQLPool implements SQLConnectionBase{
    
    private List<Connection> Pool = new ArrayList<>();
    private List<Connection> OutPool = new ArrayList<>();
    
    private SQLDatabase database;
    private String schema;
    private int size;

    public SQLPool(SQLDatabase db, String schema, int size){
        this.database = db;
        this.schema = schema;
        this.size = size;
        topOff();
    }

    public synchronized Connection getConnection(){
        if(Pool.isEmpty()) topOff();
        Connection conn = Pool.get(0);
        checkout(conn);
        return conn;
    }

    private synchronized boolean checkout(Connection conn){
        if(Pool.contains(conn)){
            Pool.remove(conn);
            OutPool.add(conn);
            return true;
        }else return false;
    }

    public synchronized boolean finish(Connection conn){
        if(OutPool.contains(conn)){
            OutPool.remove(conn);
            Pool.add(conn);
            return true;
        }else return false;
    }

    public synchronized void topOff(){
        while(Pool.size() < size) spawn();
    }

    public void destroy(){
        for(Connection conn : OutPool) finish(conn);
        for(Connection conn : Pool) closeConnection(conn);
    }


    private void closeConnection(Connection conn){
        if(Pool.contains(conn)) Pool.remove(conn);
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean spawn(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://" + database.getHost() + ":3306/" + schema
            + "?autoReconnect=true&useSSL=false", database.getUsername(), database.getPassword());
            
        } catch(SQLException e1){
            e1.printStackTrace();
        }
        if(conn != null){
            Pool.add(conn);
            return true;
        } else return false;
    }

    public ResultSet sendQuery(SQLQuery query){
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

    public int sendStatement(SQLStatement statement){
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
