package com.enquero_dbUtils;

import java.sql.*;

public class DbUtils {
    public Connection con;
    public Statement stmt;
    public ResultSet rsltset;

    public Statement getConnection(String driver,String connection,String userName,String password) throws ClassNotFoundException, SQLException {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(connection, userName, password);
            stmt = con.createStatement();
            return stmt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stmt;
    }

    public void insertData(String query,String driver,String connection,String userName,String password) throws ClassNotFoundException, SQLException{
        Statement sta = getConnection( driver, connection, userName, password);
        sta.executeUpdate(query);
    }

    public ResultSet getData(String query,String driver,String connection,String userName,String password) throws ClassNotFoundException, SQLException{
        rsltset = getConnection( driver, connection, userName, password).executeQuery(query);
        return rsltset;
    }

    public void updateData(String query,String driver,String connection,String userName,String password) throws ClassNotFoundException, SQLException{
        getConnection( driver, connection, userName, password).executeUpdate(query);

    }

    public void closeConnection() throws ClassNotFoundException, SQLException{
        con.close();
        stmt.close();
        rsltset.close();


    }
}

