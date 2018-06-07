package util;

import org.mindrot.jbcrypt.BCrypt;


public final class Password {
    private String hash;

    public Password()
    {
        hash = "";
    }

    public Password(String password)
    {
        assign(password);
    }

    public static Password fromHash(String hash)
    {
        Password password = new Password();
        password.hash = hash;
        return password;
    }

    public boolean equals(String password)
    {
        return BCrypt.checkpw(password, hash);
    }
  
    public String getHash()
    {
        return hash;
    }

    @Override
    public String toString()
    {
        return hash;
    }
    
    private void assign(String password)
    {
        String salt = BCrypt.gensalt(10);
        hash = BCrypt.hashpw(password, salt);
    }
}
