package com.rafaeldakaj.sqltools.thread;

import java.sql.Connection;

import com.rafaeldakaj.sqltools.SQLStatement;

public class SQLStatementTask implements Runnable{

    private volatile int result;
    private Connection conn;
    private SQLStatement statement;

    public SQLStatementTask(Connection conn, SQLStatement statement){
        this.conn = conn;
        this.statement = statement;
    }

    @Override
    public void run() {
        this.result = statement.send(conn);
    }

    public int getResult(){
        return result;
    }
    
}
