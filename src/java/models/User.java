package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import util.Password;


public class User {

    int id;
    String name, username;
    Password password;

    public User()
    {
    }

    User(ResultSet res) throws SQLException
    {
        id = res.getInt("id");
        name = res.getString("name");
        username = res.getString("username");
        password = Password.fromHash(res.getString("password"));
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = new Password(password);
    }
    
    public void save()
    {
        new UserDAO().insert(this);
    }
}
