package com.rafaeldakaj.sqltools.thread;

import java.sql.Connection;
import java.sql.ResultSet;

import com.rafaeldakaj.sqltools.builder.SQLQuery;

public class SQLQueryTask implements Runnable{

    private volatile ResultSet result;
    private Connection conn;
    private SQLQuery query;

    public SQLQueryTask(Connection conn, SQLQuery query){
        this.conn = conn;
        this.query = query;
    }


    @Override
    public void run() {
        this.result = query.send(conn);
    }

    public ResultSet getResult(){
        return result;
    }
    
}
