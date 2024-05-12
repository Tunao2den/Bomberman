package database;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class DatabaseMockup {
    private ArrayList<User> users = new ArrayList<>();


    public void register(String username, String password) {
        if (!exists(username)) {
            users.add(new User(username, password));
        }
    }

    public boolean exists(String username) {
        for (User user : this.users) {
            if (user.getUsername().compareTo(username) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean validateUser(String username, String password) {
        for (User user : this.users) {
            if (user.getUsername().compareTo(username) == 0 && user.getPassword().compareTo(password) == 0) {
                return true;
            }
        }
        return false;
    }


    public User getUser(String username) {
        for (User user : this.users) {
            if (user.getUsername().compareTo(username) == 0) {
                return user;
            }
        }
        return null;
    }
}
