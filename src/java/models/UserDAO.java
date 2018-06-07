package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.DAO;


public class UserDAO extends DAO
{
    public boolean insert(User user)
    {
        boolean canInsert = !userExists(user);
        if (canInsert){
            simpleInsert("users", user.getName(), user.getUsername(), user.getPassword().getHash());
        }
        return canInsert;
    }
    
    public int insert(User... users)
    {
        Object[][] args = new Object[users.length][4];
        int i = 0;
        for (User user: users){
            args[i][0] = user.getName();
            args[i][1] = user.getUsername();
            args[i++][2] = user.getPassword().getHash();
        }
        return simpleInsert("users", args);
    }

    public void delete(User user)
    {
        delete(user.getUsername());
    }

    public void delete(String username)
    {
        simpleDelete("users", "username = ?", new Object[]{ username });
    }
    
    public boolean userExists(User user)
    {
        return userExists(user.getUsername());
    }
    
    public boolean userExists(String username)
    {
        ResultSet res = simpleSelect("users", new String[]{"id"}, "username = ?", username);
        
        try {
            if (res.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        
        return false;
    }

    public boolean userExists(String... usernames)
    {
        String where = "username in (";
        for (int i = 0; i < usernames.length; i++) {
            if (i != usernames.length - 1) where += "?, ";
            else where += "?";
        }
        where += ")";
        
        ResultSet res = simpleSelect("users", new String[]{"id"}, where, (Object[]) usernames);
        
        try {
            if (res.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        
        return false;
    }

    
    public User findByUsername(String username)
    {
        User user = null;
        ResultSet res = simpleSelect("users", new String[]{"username"}, "username = ?", username);
        try {
            if (res.next()) {
                user = new User(res);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        return user;
    }
   
    public ArrayList<User> findAll()
    {
        ArrayList<User> users = new ArrayList<>();
        ResultSet res = simpleSelect("users", "id", "name", "username", "password");
        try {
            while (res.next()) {
                User user = new User(res);
                users.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        
        return users;
    }
}
