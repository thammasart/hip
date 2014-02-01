package models;

public class UserForm {
    public String username;
    public String password;
    
    public String validate() {
        if (User.authenticate(username, password) == null) {
          return "Invalid user or password";
        }
        return null;
    }
}