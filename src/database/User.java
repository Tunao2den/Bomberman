package database;

import lombok.Getter;
import lombok.Setter;
import com.auth0.jwt.JWT;

@Getter
@Setter
public class User {
    private String username;
    private String password;
    private String jwt;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}