package models;

import java.sql.ResultSet;
import models.DAO;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO extends DAO
{
    public void insert(User user)
    {
        if (!user.getUsername().equals("")) {
            if (findByUsername(user.getUsername()) == null){
                
            }
        }
    }

    public User findByUsername(String username)
    {
        User user = null;
        String query = "select id, name, username, password from users where username = ?";
        Object[] params = { username };
        ResultSet res = executeQuery(query, params);
        
        try {
            if (res.next()) {
                user.setId(res.getInt("id"));
                user.setName(res.getString("name"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("username"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;
    }

    public ArrayList<User> findAll()
    {
        ArrayList<User> users = new ArrayList<>();
        
        return users;
    }
}
