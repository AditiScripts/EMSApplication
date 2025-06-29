package Expense;

/**
 * Write a description of class JDBCExample here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

// For MySQL connectivity
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.*;

public class DBConnection {
    static final String DB_URL = "jdbc:mysql://localhost:3306/Expenses"; //table location
    static final String DB_URL1 = "jdbc:mysql://localhost:3306/";
    static final String USER = "root";
    static final String PASS = "root123";

 
    //Connecting to tge RDBMS with the DriverManager class involves calling the method DriverManager.getConnection.   

    public Connection DBGetConn() {
        // Open a connection
        Connection conn = null;
        //System.out.println("Connecting to Database..."); 
        try
        {   // Registering drivers
            conn = DriverManager.getConnection(DB_URL, USER, PASS);         
           // System.out.println("Inside try block");  
           // System.out.println("Connected to database");
        }
        catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("Inside catch block");
        } 
        //System.out.println("Outside try/catch block"); 
        return conn;
    }

    public static void DBCreate(String DBNAME)
    {
        try(Connection conn = DriverManager.getConnection(DB_URL1, USER, PASS);
        Statement stmt = conn.createStatement();)
        {   
            String sql = "CREATE DATABASE "+ DBNAME +" ";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");  
        }
        catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("Inside catch blockDB CREATE");
        } 
        //System.out.println("Outside try/catch db create block"); 
    }

    public static void DBDel(String DBNAME)
    {
        try(Connection conn1 = DriverManager.getConnection(DB_URL1, USER, PASS);
        Statement stmt = conn1.createStatement();)
        {   
            String sql = "DROP DATABASE "+ DBNAME +" ";
            stmt.executeUpdate(sql);
            System.out.println("Database deleted successfully..."); 

        }
        catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("Inside catch blockDB del");
        } 
        //System.out.println("Outside try/catch db del block"); 
    }

    public static void DBList()
    {
        Statement stmt = null;
        ResultSet resultset = null;

        try {Connection conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn1.createStatement();
            resultset = stmt.executeQuery("SHOW DATABASES;");        

            while (resultset.next()) {
                System.out.println(resultset.getString("Database"));
            }
        }
        catch (SQLException ex){
            // handle any errors
            ex.printStackTrace();
        }
        finally {
            // release resources
            if (resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException sqlEx) { }
                resultset = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { }
                stmt = null;
            }
        }
    }
    
    public static void DBtableCreate()
    {
        try(Connection conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn1.createStatement();
        ) {              
            String sql = "CREATE TABLE IF NOT EXISTS Expenses2022 " +
                "(id INTEGER not NULL AUTO_INCREMENT, " +
                " expense_name VARCHAR(255) NOT NULL, " + 
                " start_date DATE NOT NULL, " + 
                " expiry_date DATE NOT NULL, " + 
                " monthly_payment DECIMAL(10,2), " + 
                " yearly_payment DECIMAL(10,2), " + 
                " PRIMARY KEY ( id ))"; 

            stmt.executeUpdate(sql);
            System.out.println("Created table in the database...");         
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        //System.out.println("Outside try/catch db del block"); 
    }

    public static void DBtableDel()
    {
        // Open a connection
        try(Connection conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = conn1.createStatement();
        ) {              
            String sql = "DROP TABLE expenses2022";
            stmt.executeUpdate(sql);
            System.out.println("Table deleted in the database...");         
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

   
    public static void DBtablelist()
    {
        Statement stmt = null;
        ResultSet resultset = null;
        try {Connection conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn1.createStatement();
            stmt.executeQuery("SHOW tables");

            resultset = stmt.getResultSet();

            while (resultset.next()) {
                System.out.println(resultset.getString(1));
            }
        }
        catch (SQLException ex){
            // handle any errors
            ex.printStackTrace();
        }
        finally {
            // release resources
            if (resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException sqlEx) { }
                resultset = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { }
                stmt = null;
            }
        }
    }
}
