package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    
    public PreparedStatement prepareStatement(String query, Object[] params)
    {
        PreparedStatement sql = null;
        try {
            sql = conn.prepareStatement(query);
            int i = 1;
            for (Object obj: params) {
                sql.setObject(i++, obj);
            }
        } catch (Exception ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return sql;
    }
    
    public ResultSet executeQuery(String script, Object[] params)
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
    
    public boolean executeUpdate(String script, Object[] params)
    {
        boolean res = false;
        try {
            PreparedStatement sql = prepareStatement(script, params);
            res = sql.executeUpdate() > 0;
        } catch(Exception ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return res;
    }
    
    @Override
    protected void finalize() throws SQLException
    {
        conn.close();
    }
}
