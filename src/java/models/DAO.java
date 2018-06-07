package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;



public class DAO {

    Connection conn;

    public DAO()
    {
        this("vidon", "vidon", "asmp32hj26");
    }
    
    public DAO(String database, String user, String password)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ////// prepareStatement
    
    public PreparedStatement prepareStatement(String script, Object[][] params)
    {
        PreparedStatement sql = null;
        try {
            sql = conn.prepareStatement(script);
            int i = 1;
            for (Object[] param: params) {
                for (Object obj: param) {
                    sql.setObject(i++, obj);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return sql;
    }
    
    public PreparedStatement prepareStatement(String script, Object... params)
    {
        return prepareStatement(script, new Object[]{ params });
    }
    
    /////////////////////////////////////////////////////////////////////////////////////
    ////// executeQuery
    
    public ResultSet executeQuery(String script, Object[][] params)
    {
        ResultSet res = null;
        try {
            PreparedStatement sql = prepareStatement(script, params);
            res = sql.executeQuery();
        } catch(Exception ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return res;
    }
    
    public ResultSet executeQuery(String script, Object... params)
    {
        return executeQuery(script, new Object[]{ params });
    }
    
    public ResultSet executeQuery(String script)
    {
        ResultSet res = null;
        try {
            Statement sql = conn.createStatement();
            res = sql.executeQuery(script);
        } catch(Exception ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return res;
    }


    /////////////////////////////////////////////////////////////////////////////////////
    ////// executeUpdate
    
    public int executeUpdate(String script, Object[][] params)
    {
        int res = -1;
        try {
            PreparedStatement sql = prepareStatement(script, params);
            res = sql.executeUpdate();
        } catch(Exception ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return res;
    }

    public int executeUpdate(String script, Object... params)
    {
        return executeUpdate(script, new Object[]{ params });
    }
    
    public int executeUpdate(String script)
    {
        int res = -1;
        try {
            Statement sql = conn.createStatement();
            res = sql.executeUpdate(script);
        } catch(Exception ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return res;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ////// simpleSelect

    public ResultSet simpleSelect(String table, String... columns)
    {
        String script = "select ";
        
        int i = 1;
        for (String column: columns){
            script = "`" + column + "`";
            if (i++ != columns.length) script += ",";
        }

        script += " from `" + table + "`";
        
        return executeQuery(script);
    }

    public ResultSet simpleSelect(String table, String[] columns, String where, Object... params)
    {
        String script = "select ";
        
        int i = 1;
        for (String column: columns){
            script = "`" + column + "`";
            if (i++ != columns.length) script += ",";
        }

        script += " from `" + table + "` where " + where;
        
        return executeQuery(script, params);
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ////// simpleInsert

    public int simpleInsert(String table, String[] columns, Object[][] values)
    {
        if (values.length == 0 && values[0].length == 0) return 0;
        
        int columnCount = columns.length;
        int valueCount = values[0].length;
        for (Object[] arr: values) {
            if (arr.length != valueCount){
                return -1;
            }
        }

        if (columnCount != 0 && columnCount != valueCount) return -1;

            
        String script = "insert into `" + table + "` ";
        
        if (columns.length > 0) {
            script += "(";
            int i = 1;
            for (String column: columns){
                script = "`" + column + "`";
                if (i++ != columns.length) script += ",";
            }
            script += ") ";
        }
        
        script += " values ";
        
        String[] args_list = new String[columns.length];
        for (int i = 0; i != args_list.length; i++){
            if (i != args_list.length - 1) args_list[i] = "?, ";
            else args_list[i] = "?";
        }
        
        String args = String.join("", args_list);

        for (int i = 0; i != values.length; i++) {
            script += "(" + args;
            if (i != columns.length - 1) script += "), ";
            else script += ")";
        }
        
        return executeUpdate(script, values);
    }

    public int simpleInsert(String table, Object[][] values)
    {
        return simpleInsert(table, new String[0], values);
    }
    
    public int simpleInsert(String table, Object... values)
    {
        return simpleInsert(table, new String[0], new Object[][]{ values });
    }

    /////////////////////////////////////////////////////////////////////////////////////
    ////// simpleDelete
    
    public int simpleDelete(String table, String where, Object... params)
    {
        String script = "delete from `" + table + "` where " + where;
        return executeUpdate(script, params);
    }

    public int simpleDelete(String table, String where)
    {
        String script = "delete from `" + table + "` where " + where;
        return executeUpdate(script);
    }

    public int simpleDelete(String table)
    {
        String script = "delete from `" + table + "`";
        return executeUpdate(script);
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////
    ////// finilize
    
    @Override
    protected void finalize() throws SQLException, Throwable
    {
        try {
            conn.close();
        } finally {
            super.finalize();
        }
    }
}
